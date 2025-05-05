import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;


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
		monthlyUpkeep();
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

	// the monthly (5thof each month at 23:59...deals with variable length months
	// currently used for interest only
	// may also be used to remove old logs etc. or a similar logic with a different time length may be used
	private static void monthlyUpkeep(){		
	    Runnable interestTask = new Runnable() {
	        public void run() {
	            addInterest(); // schedule addInterest monthly
//	            scheduleMonthlyInterest();  // Reschedule for next month
	        }
	    };

	    long delay = computeDelayUntilNext5th2359();
	    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	    scheduler.schedule(interestTask, delay, TimeUnit.MILLISECONDS);
	}

	private static long computeDelayUntilNext5th2359() {
	    LocalDateTime now = LocalDateTime.now();
	    LocalDateTime nextRun = now.withDayOfMonth(5).withHour(23).withMinute(59).withSecond(0).withNano(0);
	
	    if (now.compareTo(nextRun) >= 0) {
	        nextRun = nextRun.plusMonths(1);
	    }
	
	    return Duration.between(now, nextRun).toMillis();
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
			CustomerAccount user = null;		//updated method of tracking current customer
			BankAccount BankAcc = null;			//updated method of tracking current bank account	//TODO: use this
			String BA = "";						//keeps track of current bank account

			dailyUpkeep();
	        try {
				//while the connection is still receiving messages
		        while((message = (Message) objectInputStream.readObject()) != null) {
		        	System.out.println("New ClientHandler started: " + Thread.currentThread().getName());
					//print the action requested by the client
		        	System.out.println(
		        			//display ip of client
		        			"Client <" + clientSocket.getInetAddress() + "> "
		        			//if the client is logged in include their name
		        			+ (LOGGEDIN? "[" + user.getName() + "]: " : ": ")
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
		        		user = login(message);
		        		if(user != null) {
		        			LOGGEDIN = true;
		        		}
		        		//if login was successful
		        		if(LOGGEDIN == true) {

		        			String caData = user.getName() + '\n' + user.getPhoneNumber() + '\n' + user.getAddress() + '\n';
		        			//for every bank account attached to the customer account
		        			for(int i = 1; i <= user.getAssociatedBA().size(); i++) {
		        				//append the account number to the string
		        				caData += user.getAssociatedBA().get(i);
		        				//if the current account is not the final one
		        				if(i != user.getAssociatedBA().size()) {
		        					//also append a comma
		        					caData += ",";
		        				}
		        				
			        		//respond that the login was successful
			        		sendMessage(
			        				new Message(
			        						"Server", clientSocket.getInetAddress().toString(), caData, Message.Type.ACCESSCAREQGRANTED));

		        		}
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
						if(LOGGEDIN) {
							user.switchAccess();	//this also updates to file
		        			user = null;			//we are no longer logged in
		        			LOGGEDIN = false;		//reflect it
		        			sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "Exit CA granted", Message.Type.EXITCAREQGRANTED));
		        			
						}else {
							sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "Exit CA denied", Message.Type.EXITCAREQDENIED));
						}
					}

					// --------------commands allowed for Tellers--------------------------
					if (isTeller) {
						switch(message.getType().name()) {
							case "WITHDRAWREQ": break;
							case "DEPOSITREQ": break;
							default: /*invalid command*/
								sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "Login First", Message.Type.INVALID));
								break;
						}
					}
//					--------------------------------------------------TELLER COMMANDS ABOVE---------------------------------------------------------
					

					// ----------------commands allowed for ATMs------------------------------
					else {
						
						// when logged in first process if a message is a logout request
						if(message.getType() == Message.Type.EXITBAREQ) {
							// TODO - log-out of financial account
							AccessingBankAccount = false;
							BA = "";
							sendMessage(
			        				new Message(
			        						"Server", clientSocket.getInetAddress().toString(), "Login successful", Message.Type.EXITBAREQGRANTED));
							
						}						
						
						// ATM will only be able to select a bank account to make transactions from or logout
						if(!AccessingBankAccount && message.getType() == Message.Type.ACCESSBAREQ) {
							//attempt login
							AccessingBankAccount = loginBank(message);
			        		//if login was successful
			        		if(AccessingBankAccount == true) {
			        			String[] args = message.getData().split(",");
			        			File[] list = bankAccounts.listFiles();
			        			
			        			// determine if the customer account is valid
			        			String build = "";
			        			// compare each file in the list
			        			for (File file : list) {
			        				// do not include folders
			        				if (file.isFile()) {
			        					// if the file is found in the list
			        					if(file.getName().equals(args[0] + ".txt")) {
			        						// create a scanner to move through the file
			        						Scanner scanner = new Scanner(file);
			        						// if the access indicator on Line 1 is "1", then the file is currently in use and log-in is not allowed
			        						while(scanner.hasNextLine()) {
			        							build += scanner.nextLine() + "\n";
			        						}
			        						scanner.close();
			        					}
			        				}
			        			} 
			        			//set the current user to the username of the account
			        			BA = message.getData();
				        		AccessingBankAccount = true;
			        			//respond that the login was successful
				        		sendMessage(
				        				new Message(
				        						"Server", clientSocket.getInetAddress().toString(), build, Message.Type.ACCESSBAREQGRANTED));
			        		}
			        		//if login failed
			        		else{
			        			sendMessage(
				        				new Message("Server", clientSocket.getInetAddress().toString(), "Login Failed", Message.Type.ACCESSBAREQDENIED));
			        		}
			        		
			        		//go back to waiting for new message
			        		continue;
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
							case "WITHDRAWREQ":
								Boolean success = withdraw(BA, message.getData());
								if(success) {
									sendMessage(
					        				new Message(
					        						"Server", clientSocket.getInetAddress().toString(), "Money Withdrawn Successfully", Message.Type.WITHDRAWREQACCEPTED));
								}
								else {
									sendMessage(
					        				new Message(
					        						"Server", clientSocket.getInetAddress().toString(), "Money Failed to Withdraw", Message.Type.WITHDRAWDONE));
								}
								break;
							case "DEPOSITREQ":
								deposit(BA, message.getData());
								sendMessage(
				        				new Message(
				        						"Server", clientSocket.getInetAddress().toString(), "Money Failed to Withdraw", Message.Type.DEPOSITDONE));
								break;
							default: /*invalid command*/
								sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "Login First", Message.Type.INVALID));
								break;
						}
					}

//						--------------------------------------------------ATM COMMANDS ABOVE---------------------------------------------------------------
					
					// this will be below all other request types and should only be reachable
					// if a message with an incorrect or invalid type is sent
					sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "Login First", Message.Type.INVALID));
					
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
	        //make sure accounts become free even in the event of a forced closure
	        finally {
				if(user != null) {
					//if the user was still being accessed
					if(user.checkAccessStatus()) {
						//free up access to the account
						user.switchAccess();
					}
				}
				if(BankAcc != null) {
					//TODO: free account based on class
				}
	        }
		}

		// helper methods for ClientHander:
		
		// schedules ATM refill to run once daily at midnight, starting from next midnight 
		// reference: stackoverflow
		// correctness not verified
		private void dailyUpkeep() {
			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
			long midnight = LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MINUTES);
			try{
				scheduler.scheduleAtFixedRate(
					//this inner try catch is required according to eclipse
					()->{try {
						sendMessage(new Message("Server", clientSocket.getInetAddress().toString(),"50000", Message.Type.REFILLATM));
					} catch (IOException e) {
						e.printStackTrace();
					}},
					midnight, 1440, TimeUnit.MINUTES);
			}catch(NullPointerException e) {
				
			}
		}
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
		private synchronized CustomerAccount login(Message msg) throws IOException {
			// account data will be sent as "username,password" so split it
			String args[] = msg.getData().split(",");
			
			// get the list of customer accounts
			File[] list = customerAccounts.listFiles();
			
			CustomerAccount tempAcc = null;
			Boolean access = false;
			String name = "";
			String phoneNumber = "";
			String address = "";
			String password = "";
			ArrayList<String> bankAccounts = new ArrayList<String>();
			

			
			// compare each file in the list
			for (File file : list) {
				// do not include folders
				if (file.isFile()) {
					// if the file is found in the list
					if(file.getName().equals(args[0] + ".txt")) {
						// create a scanner to move through the file
						Scanner scanner = new Scanner(file);
						
						//new version

						try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
							String line;
							//read in entire file to corresponding variables
							while ((line = reader.readLine()) != null) {
			                    String[] temp = line.split(" ");
			                    String frst = temp[0];
			                    String second = temp[1];
			                    
			                    if(frst.equalsIgnoreCase("Access_status:")) {
			                    	if(second.equalsIgnoreCase("0")) {
			                    		access = false;
			                    	}
			                    	else {
			                    		access = true;
			                    	}
			                    	
			                    }
			                    if(frst.equalsIgnoreCase("Name:")){
			                    	name = second;
			                    }
								if(frst.equalsIgnoreCase("Phone_number:")){
									phoneNumber = second;
								}
								if(frst.equalsIgnoreCase("Address:")){
									address = second;
								}
								if(frst.equalsIgnoreCase("Password:")){
									password = second;
								}
								if(frst.equalsIgnoreCase("Bank_accounts:")){
									String[] accs = second.split(",");
									for(String item : accs) {
										bankAccounts.add(item);
									}
								}
							}//while going through file
							reader.close();
						}//try reading
						
						//create a temporary account with all the info
						tempAcc = new CustomerAccount(access, name, phoneNumber, address, password, bankAccounts);
						
						//if the account is already being used
						if(tempAcc.checkAccessStatus() == true) {
							scanner.close();
							return null;
						}
						
						//validate the password. will run if password fails
						if(!tempAcc.validatePassword(args[1])) {
							scanner.close();
							return null;
						}
						
						//now that we have verified the password lock the account
						tempAcc.switchAccess();
						scanner.close();
					}
				}
			} // end 'for'
			//if the file isnt found will return null, if it is found then this will return the object
			return tempAcc;
		}
		
		private synchronized boolean loginBank(Message msg) throws IOException {
			// account data will be sent as "username,password" so split it
			String args[] = msg.getData().split(",");
			
			// get the list of customer accounts
			File[] list = bankAccounts.listFiles();
			
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
						else {
							// otherwise, the login is valid
							// update the file-access indicator in the file:
							// first, store the entire file in a string
							String info = new String(Files.readAllBytes(Paths.get(file.toString())));
							// then, replace the first char and append the rest of the string back
							info = "1" + info.substring(1);
							// finally, write the file back out
							Files.write(Paths.get(file.toString()), info.getBytes());
							found = true;
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
		
		private Boolean withdraw(String account, String amount) throws IOException {
			// get the list of customer accounts
			File[] list = bankAccounts.listFiles();
			Boolean Valid = true;
			// compare each file in the list
			for (File file : list) {
				// do not include folders
				if (file.isFile()) {
					// if the file is found in the list
					if(file.getName().equals(account + ".txt")) {
						
						// create a scanner to move through the file
						List<String> lines = new ArrayList<>();
						try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
							String line;
							while ((line = reader.readLine()) != null) {
			                    String[] temp = line.split(" ");
			                    String frst = temp[0];
			                    String second = temp[1];
			                    if (frst.equalsIgnoreCase("Current_balance:")) {
			                        double bal = Double.parseDouble(second);
			                        //if the current balance is greater than the withdraw amount
			                        if(bal > Double.parseDouble(amount)) {
			                        	lines.add(frst + " " + String.valueOf(bal - Double.parseDouble(amount)));
			                        }
			                        else {
			                        	lines.add(line);	// add line as is
			                        	Valid = false;		//if amount is greater than bal then money cannot be withdrawn
			                        }
			                        continue; // skip to next line in case of "SAVINGS" account
			                    }//if current balance line
			                    lines.add(line);  // add line as is
							}//while going through file
							reader.close();
						}//try reading
						try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			                for (String updatedLine : lines) {
			                    writer.write(updatedLine);
			                    writer.newLine();  // preserve line breaks
			                }
			                writer.close();
			            }//try writing
					}//if the name is right
				}//if it is a file
			}//for each file
			return Valid;
		}
		
		// TODO
		private void deposit(String account, String amount) throws IOException {
			// get the list of customer accounts
			File[] list = bankAccounts.listFiles();
			// compare each file in the list
			for (File file : list) {
				// do not include folders
				if (file.isFile()) {
					// if the file is found in the list
					if(file.getName().equals(account + ".txt")) {
						// create a scanner to move through the file
						List<String> lines = new ArrayList<>();
						try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
							String line;
							while ((line = reader.readLine()) != null) {
			                    String[] temp = line.split(" ");
			                    String frst = temp[0];
			                    String second = temp[1];
			                    if (frst.equalsIgnoreCase("Current_balance:")) {
			                        double bal = Double.parseDouble(second);
			                        lines.add(frst + " " + String.valueOf(bal + Double.parseDouble(amount)));
			                        continue; // skip to next line in case of "SAVINGS" account
			                    }//if current balance line
			                    lines.add(line);  // add line as is
							}//while going through file
							reader.close();
						}//try reading
						try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			                for (String updatedLine : lines) {
			                    writer.write(updatedLine);
			                    writer.newLine();  // preserve line breaks
			                }
			                writer.close();
			            }//try writing
					}//if the name is right
				}//if it is a file
			}//for each file
		}
	}

	// helper methods for Server:
	private static void addInterest() {
		    double interestRate = 3.50 / 100.00;
		
		    File[] files = bankAccounts.listFiles();
		    for (File file : files) {
		        if (file.isFile()) {
		            List<String> lines = new ArrayList<>();
		            boolean toChange = false;
		
		            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
		                String line;
		                while ((line = reader.readLine()) != null) {
		                    String[] temp = line.split(" ");
		                    String frst = temp[0];
		                    String second = temp[1];
		
		                    if (frst.equalsIgnoreCase("Account_type:") && second.equalsIgnoreCase("SAVINGS")) {
		                        toChange = true;
		                        lines.add(line);
		                        continue; // skip to next line in case of "SAVINGS" account
		                    }
		
		                    if (toChange && frst.equalsIgnoreCase("Current_balance:")) {
		                        // Add interest to the balance
		                        double bal = Double.parseDouble(second);
		                        lines.add(frst + " " + String.valueOf(bal + bal * interestRate));
		                        toChange = false;  // stop entering this block after the first update
		                        continue;  
		                    }
		                    lines.add(line);  // add line as is
		                }
		            } catch (IOException e) {
		                e.printStackTrace();
		                continue;  // skip to next file 
		            }
		
		            // write back
		            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		                for (String updatedLine : lines) {
		                    writer.write(updatedLine);
		                    writer.newLine();  // preserve line breaks
		                }
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }
	    		}
		}
}
