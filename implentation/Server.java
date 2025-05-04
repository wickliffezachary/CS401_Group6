import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class Server {

	// the File fields below are where account information is stored in our file system
	// they will be created by the Server if they do not already exist, and they are accessible by the ClientHandler class

	// Fields:
	private final static File directory = new File(System.getProperty("user.dir"));
	private final static File customerAccounts = new File(directory, "data/customerAccounts/");
	private final static File bankAccounts = new File(directory, "data/bankAccounts/");
	private final static File otherFiles = new File(directory, "data/otherFiles/");
	private final static File tellerAccounts = new File(directory, "data/tellerAccounts/");

	// 'main' method
	public static void main(String[] args) {

		// initialize a ServerSocket variable and set it to NULL
		ServerSocket server = null;

		// attempt to create the directories if they do not already exist, and print out whether they were created or they already exist
		System.out.println("Customer Accounts Directory: " + ((customerAccounts.mkdirs()) ? "Created" : "Exists"));
		System.out.println("Bank Accounts Directory: " + ((bankAccounts.mkdirs()) ? "Created" : "Exists"));
		System.out.println("Teller Accounts Directory: " + ((tellerAccounts.mkdirs())? "Created": "Exists"));
		System.out.println("Other Directory: " + ((otherFiles.mkdirs()) ? "Created" : "Exists"));

		// print out a message stating that the server has booted up
		System.out.println("Server Started");

		try {
			// the server is listening on port 1234
			server = new ServerSocket(1234);
			server.setReuseAddress(true);

			// running infinite loop for getting client request
			while (true) {
				// socket object to receive incoming client requests
				Socket client = server.accept();

				// Displaying that new client is connected to server
				System.out.println("New client connected " + client.getInetAddress().getHostAddress());

				// create a new thread object
				ClientHandler clientSocket = new ClientHandler(client);

				// this thread will handle the client separately
				new Thread(clientSocket).start();
			}
		}
		catch (IOException error) {
			error.printStackTrace();
		}
		finally {
			if (server != null) {
				try {
					server.close();
				}
				catch (IOException error) {
					error.printStackTrace();
				}
			}
		}

	}

	// ClientHandler class
	private static class ClientHandler implements Runnable{

		// Fields:
		private final Socket clientSocket;
		private final ObjectInputStream objectInputStream;
		private final ObjectOutputStream objectOutputStream;

		// ClientHandler - Constructor
		// this will initialize the ClientSocket and the input/output streams
		public ClientHandler(Socket socket) throws IOException {
			this.clientSocket = socket;
			this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
			this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
		}

		public void run() {

			// local variables to hold the data that changes
			Message message;
			Boolean AccessingBankAccount = false;
			Boolean LOGGEDIN = false;			//determines if you are currently accessing a Customer Account
			Boolean VERIFIED = false;			//determines if the client has verified its identity
			Boolean isTeller = false;			//determines if the client is an atm or teller
			String User = "";					//TODO: change this to be based on customer data
	        try {
				//while the connection is still receiving messages
		        while((message = (Message) objectInputStream.readObject()) != null) {
		        	System.out.println("New ClientHandler started: " + Thread.currentThread().getName());
					//print the action requested by the client
		        	System.out.println(
		        			//display ip of client
		        			"Client <" + clientSocket.getInetAddress() + "> "
		        			//if the client is logged in include their name
		        			+ (LOGGEDIN? "[" + User + "]: " : ": ")
		        			//display the message type
		        			+ "Request " + message.getType().name() + " "
		        			//display any extra info sent with the message
		        			+ "with data \"" + message.getData() + "\"");
		        	
		        	//when the client connects it should first send a connection request to verify if it is an atm or teller

		        	if (!VERIFIED) {
		        	    if (message.getType() == Message.Type.LOGINREQTELLER) {
		        	        // AUTHENTICATE TELLER
		        	        String[] credentials = message.getData().split(",");
		        	        String uname = null;
		        	        String pswd = null;
		        	        for (String part : credentials) {
		        	        	if (part.startsWith("username=")) uname = part.substring(9);
		        	        	if (part.startsWith("password=")) pswd = part.substring(9);
		        	        }
		        	        
		        	        System.out.println("Parsed username = " + uname);
		        	        System.out.println("Parsed password = " + pswd);

		        	        boolean valid = false;

		        	        if (uname != null && pswd != null) {
		        	            File file = new File(tellerAccounts, uname + ".txt");
		        	            System.out.println("Reading file: " + file.getAbsolutePath());
		        	            System.out.println(" file.exists()? " + file.exists() + ", isFile()? " + file.isFile());
		        	            System.out.println(System.getProperty("user.dir"));
		        	            if (file.exists()) {
		        	            	
		        	                Scanner scanner = new Scanner(file);
		        	                while (scanner.hasNextLine()) {
		        	                    String line = scanner.nextLine().trim();
		        	                 // inside the while(scanner.hasNextLine()) loop:
		        	                    
		        	                    String[] parts = line.split(":", 2);
		        	                    if (parts.length == 2 && parts[0].trim().equalsIgnoreCase("password")) {
		        	                        String stored = parts[1].trim();
		        	                        System.out.println("Checking password: " + stored);
		        	                        if (stored.equals(pswd)) {
		        	                            valid = true;
		        	                            break;
		        	                        }
		        	                    }
		        	                }
		        	                scanner.close();
		        	            }
		        	        }

		        	        if (valid) {
		        	            isTeller = true;
		        	            VERIFIED = true;
		        	            sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "Teller login successful", Message.Type.LOGINOK));
		        	        } else {
		        	            sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "Teller login failed", Message.Type.LOGINDENIED));
		        	        }
		        	        
		        	        continue;
		        	    }

		        	    else if (message.getType() == Message.Type.LOGINREQATM) {
		        	        // Allow ATM login without authentication
		        	        VERIFIED = true;
		        	        sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "ATM login successful", Message.Type.LOGINOK));
		        	        continue;
		        	    }
		        	    
		        	    else if (message.getType() == Message.Type.LOGOUTREQTELLER || message.getType() == Message.Type.LOGOUTREQATM) {
		        	        sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "Logout ok", Message.Type.LOGOUTOK));
		        	        break; // terminate handler thread cleanly
		        	    }

		        	    // fallback last resort
		        	    sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "Invalid login type", Message.Type.INVALID));
		        	    continue;
		        	}
		        	
		        	if (message.getType() == Message.Type.LOGOUTREQTELLER
		        			 || message.getType() == Message.Type.LOGOUTREQATM) {
		        			    isTeller   = false;
		        			    VERIFIED   = false;
                
                	//if a user is currently being accessed then it needs to be logged out
                  if(LOGGEDIN) {
		        			  //TODO: logout CA, this includes resetting the access modifier
		        		  }
		        			    sendMessage(new Message("Server",clientSocket.getInetAddress().toString(),"Logout successful",Message.Type.LOGOUTOK));
		        			    break;  // exit
		        	}
		        	
		        	
		        	
		        	//once the client is connected it should attempt to access a customer account before being able to access more functionality
		        	if(!LOGGEDIN && message.getType() == Message.Type.ACCESSCAREQ) {
		        		
		        		//attempt to log in
		        		LOGGEDIN = login(message);
		        		//if login was successful
		        		if(LOGGEDIN == true) {
		        			
		        			//set the current user to the username of the account
		        			User = message.getData().split(",")[0];
			        		//respond that the login was successful
			        		sendMessage(
			        				new Message(
			        						"Server", clientSocket.getInetAddress().toString(), "Login successful", Message.Type.ACCESSCAREQGRANTED));
		        		}
		        		else{
		        			sendMessage(
			        				new Message("Server", clientSocket.getInetAddress().toString(), "Login Failed", Message.Type.ACCESSCAREQDENIED));
		        		}
		        		
		        		//go back to waiting for new message
		        		continue;
		        	}	
			        
		        	//if a message is sent but they are not logged in just throw back an error because its not correct
		        	if(!LOGGEDIN) {
		        		//tell them its not valid
		        		sendMessage(
		        				new Message(
		        						"Server", clientSocket.getInetAddress().toString(), "Login First", Message.Type.INVALID));
		        		//go back to waiting for new message
		        		continue;
			        }

					// if they are logged in, then see if they want to log out first
					if(message.getType() == Message.Type.EXITCAREQ) {
						// TODO - log out of customer account
					}


					// Below this is where commands for logged-in users go.
					// To prevent invalid commands like an ATM trying to create accounts,
					// only allow commands based on whether they are a teller or an ATM.

					// commands allowed for Tellers
					if (isTeller) {
						switch(message.getType().name()) {
							case "WITHDRAWREQ": break;
							case "DEPOSITREQ": break;
							default: /*invalid command*/
								sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "Login First", Message.Type.INVALID));
								break;
						}
					}
					// commands allowed for ATMs
					else {
						// ATM will only be able to select a bank account to make transactions from or logout
						if(!AccessingBankAccount && message.getType() == Message.Type.ACCESSBAREQ) {
							// TODO - log-in to financial account
						}
						// ATM should only be able to log out of customer account (handled before) or log in to financial account
						// otherwise invalid
						if(!AccessingBankAccount) {
							// tell them its not valid
							sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "Log in to bank account to access money transfers", Message.Type.INVALID));
							// go back to waiting for new message
							continue;
						}		        		

						switch(message.getType().name()) {
							case "WITHDRAWREQ":break;
							case "DEPOSITREQ":break;
							default: /*invalid command*/
								sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "Login First", Message.Type.INVALID));
								break;
						}
					}

					// this will be below all other request types and should only be reachable
					// if a message with an incorrect or invalid type is sent
					sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "Login First", Message.Type.INVALID));

					objectOutputStream.flush();
					
				} // end 'while'
			} // end 'try'
			catch (SocketException error) {
				System.out.println("Client disconnected.");
			}
			catch(IOException error) {
				error.printStackTrace();
			} catch (ClassNotFoundException error) {
				error.printStackTrace();
			}
		}

		// helper methods for ClientHander:

		// method that sends messages cleanly
		private void sendMessage(Message message) throws IOException {
			objectOutputStream.writeObject(message);
			objectOutputStream.flush();
			System.out.println("Server sent: " + message.getType() + " - " + message.getData());
		}

		// method that is called whenever an action occurs that modifies an account
		private void addTransactionHistory(String account, String action, String details) {
			// TODO - implement this method
			// write(Date.getTime() + " Account " + account + " " + action + " " + details);
		}

		// login method that is synchronized in order to prevent duplicated logins
		private synchronized boolean login(Message msg) throws IOException {
			// account data will be sent as "username,password" so split it
			String args[] = msg.getData().split(",");
			
			// get the list of customer accounts
			File[] list = customerAccounts.listFiles();
			
			// determine if the customer account is valid
			boolean found = false;
			
			// compare each file in the list
			for (File file : list) {
				// do not include folders
				if (file.isFile()) {
					// if the file is found in the list
					if(file.getName().equals(args[0] + ".txt")) {
						// create a scanner to move through the file
						Scanner scanner = new Scanner(file);
						// if the access indicator on Line 1 is "1", then the file is currently in use and log-in is not allowed
						if(scanner.nextLine().equals("1")) {
							scanner.close();
							return false;
						}
						// if the file is not being accessed
						// since the password is located on the 5th line we need to move past the next 3 lines
						for(int i = 0; i < 3; i++) {
							scanner.nextLine();
						}
						// check to see if the password is correct
						if(scanner.nextLine().equals(args[1])) {
							// if it is, then the login is valid

							// update the file-access indicator in the file:
							// first, store the entire file in a string
							String info = new String(Files.readAllBytes(Paths.get(file.toString())));
							// then, replace the first char and append the rest of the string back
							info = "1" + info.substring(1);
							// finally, write the file back out
							Files.write(Paths.get(file.toString()), info.getBytes());

							found = true;
						}
						else {
							// otherwise, it is invalid
							found = false;
						}

						scanner.close();
						return found;

					}
				}
			} // end 'for'
			// if the file isn't found, then the account doesn't exist, so no login
			return found;
		}

		// TODO
		// method that allows a teller to create a new financial account
		private void createBankAccount() {
			
		}
		
		// TODO
		// method that allows a teller to delete a financial account
		private void deleteBankAccount() {
			
		}
		
		// TODO
		private void withdraw() {
			
		}
		
		// TODO
		private void deposit() {
			
		}

	}

	// helper methods for Server:
	// schedules ATM refill to run once daily at midnight, starting from next midnight 
	// refernce: stackoverflow
	private void dailyUpkeep() {
		scheduler = Executors.newScheduledThreadPool(1);
		Long midnight=LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MINUTES);
		scheduler.scheduleAtFixedRate(
			()->{sendMessage(new Message("Server", clientSocket.getInetAddress().toString(),"50000", Message.Type.REFILLATM));},
			midnight, 1440, TimeUnit.MINUTES);
	}
	
	private void monthlyUpdate() {
		// TODO - a separate thread should run this once a month
		// this method is used for things like interest, etc.
	}

}
