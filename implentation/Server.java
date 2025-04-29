import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class Server {
	
	//determine where accounts will be stored
	//these will be created at server start if they don't exist.
	//they are accessible by the clienthandler class.
	private final static File directory = new File(System.getProperty("user.dir"));	//should probably be stored somewhere like user.home that isn't pushable to git for "security"
    private final static File customerAccounts = new File(directory, "data/customerAccounts/");
    private final static File bankAccounts = new File(directory, "data/bankAccounts/");
    private final static File otherFiles = new File(directory, "data/otherFiles/");

	public static void main(String[] args) {
		
		
		
		ServerSocket server = null;
		
		//attempts to create directory where files go and outputs status
		System.out.println("customer Directory: " + ((customerAccounts.mkdirs()) ? "Created" : "Exists"));
		System.out.println("Bank Accounts Directory: " + ((bankAccounts.mkdirs()) ? "Created" : "Exists"));
		System.out.println("other Directory: " + ((otherFiles.mkdirs()) ? "Created" : "Exists"));

		
		System.out.println("Server Started");
		
		try {

			// server is listening on port 1234
			server = new ServerSocket(1234);
			server.setReuseAddress(true);

			// running infinite loop for getting
			// client request
			while (true) {

				// socket object to receive incoming client
				// requests
				Socket client = server.accept();

				// Displaying that new client is connected
				// to server
				System.out.println("New client connected "
								+ client.getInetAddress()
										.getHostAddress());

				// create a new thread object
				ClientHandler clientSock
					= new ClientHandler(client);

				// This thread will handle the client
				// separately
				new Thread(clientSock).start();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (server != null) {
				try {
					server.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	//ClientHandler class
	private static class ClientHandler implements Runnable{
		//socket for current connection
		private final Socket clientSocket;
		private final ObjectInputStream objectInputStream;
        private final ObjectOutputStream objectOutputStream;

        

		//Thread constructor to handle socket
		public ClientHandler(Socket socket) throws IOException
		{
			this.clientSocket = socket;
			this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
			this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

		}
		
		public void run() {
			
			
			
			//variables to hold data to change
			Message message;
			Boolean LOGGEDIN = false;	//determines if you are currently accessing a Customer Account
			Boolean VERIFIED = false;	//determines if the client has verified its identity
			Boolean isTeller = false;	//determines if the client is an atm or teller
			String User = "";			//TODO: change this to be based on customer data
	        try {
				//while the connection is still receiving messages
		        while((message = (Message) objectInputStream.readObject()) != null) {
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
		        	if(!VERIFIED && (message.getType() == Message.Type.LOGINREQATM || message.getType() == Message.Type.LOGINREQTELLER)) {
		        		//if the login was for a teller then flow will adjust accordingly
	        			if(message.getType() == Message.Type.LOGINREQTELLER) {
	        				isTeller = true;
	        			}
	        			sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "Login successful", Message.Type.LOGINOK));
	        			VERIFIED = true;
		        	}
		        	//the client MUST send the first log in message otherwise they cant attempt to access login functions
		        	if(!VERIFIED) {
		        		//tell them its not valid if they dont properly connect the client
		        		sendMessage(new Message("Server", clientSocket.getInetAddress().toString(), "Login First", Message.Type.INVALID));
		        		//go back to waiting for new message
		        		continue;
			        }
		        	
		        	
		        	//a verified client should be able to quit as a first options
		        	//this should be called when the client is exiting
		        	if(message.getType() == Message.Type.LOGOUTREQATM || message.getType() == Message.Type.LOGOUTREQTELLER) {
		        		if(message.getType() == Message.Type.LOGOUTREQTELLER) {
	        				isTeller = false;
	        			}
		        		//if a user is currently being accessed then it needs to be logged out
		        		if(LOGGEDIN) {
		        			//TODO: logout CA
		        		}
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
			        
		        	//if a message is sent but they are not logged in just throw back an error
		        	if(!LOGGEDIN) {
		        		//tell them its not valid
		        		sendMessage(
		        				new Message(
		        						"Server", clientSocket.getInetAddress().toString(), "Login First", Message.Type.INVALID));
		        		//go back to waiting for new message
		        		continue;
			        }
	

		        	//if they are logged in then see if they want to log out first
		        	if(message.getType() == Message.Type.EXITCAREQ ) {
		        		//TODO: Logout CA
		        	}
		        	
		        	
		        	//below this is where commands for logged in users go
		        	
		        	//to prevent invalid messages from being sent
		        	//only allow commands based on whether they are a teller or atm
		        	
		        	//commands allowed for Tellers
		        	if(isTeller) {
		        		switch(message.getType().name()) {
			        	case "WITHDRAWREQ":break;
			        	
			        	case "DEPOSITREQ":break;
			        	
			        	default: /*invalid command*/
			        		sendMessage(
		        				new Message("Server", clientSocket.getInetAddress().toString(), "Login First", Message.Type.INVALID));
			        					break;
		        		}
		        	}
		        	//commands allowed for ATMS
		        	else {
		        		switch(message.getType().name()) {
			        	case "WITHDRAWREQ":break;
			        	
			        	case "DEPOSITREQ":break;
			        	
			        	default: /*invalid command*/
			        		sendMessage(
		        				new Message("Server", clientSocket.getInetAddress().toString(), "Login First", Message.Type.INVALID));
			        					break;
		        		}
		        	}
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	//this will be below all other request types and should only be reachable
		        	//if a message with an incorrect or invalid type is sent
		        	sendMessage(
	        				new Message(
	        						"Server", clientSocket.getInetAddress().toString(), "Login First", Message.Type.INVALID));
		        	
		        	objectOutputStream.flush();
		        }//while
	        }//try
	        catch(IOException e) {
	        	e.printStackTrace();
	        } catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	        
	        
			
		}
		
		//helpers for clienthandler
		
		//send messages cleanly
		private void sendMessage(Message message) throws IOException {
			objectOutputStream.writeObject(message);
			objectOutputStream.flush();
		}
		
		//called whenever an action occurs that modifies an account
		private void addTransactionHistory(String account, String action, String details) {
			//TODO: implement
			//write(Date.getTime() + " Account " + account + " " + action + " " + details);
		}
		
		
		//login function synchronized to prevent duplicate logins
		private synchronized Boolean login(Message msg) throws IOException {
			//account data will be sent as "user,pass" so split it
			String args[] = msg.getData().split(",");
			//get the list of customer accounts
			File[] list = customerAccounts.listFiles();
			//determine if account is valid
			boolean found = false;
			//compare each file in the list
			for (File file : list) {
				//dont include folders
				if (file.isFile()) {
					//if the file is found in the list
					if(file.getName().equals(args[0] + ".txt")) {
						//create a scanner to move through the file
						Scanner scanner = new Scanner(file);
						//if the access indicator on line 1 is 1 then the file is currently in use and login is not allowed
						if(scanner.nextLine().equals("1")) {
							scanner.close();
							return false;
						}
						//if the file is not being accessed
						//since the password is located on the 5th line we need to move past the next 3 lines
						for(int i=0; i<3; i++) {
							scanner.nextLine();
						}
						//check to see if the password is correct
						if(scanner.nextLine().equals(args[1])) {
							//if it is then the login is valid
							
							//update file access indicator in file
							//first store entire file in string
							String info = new String(Files.readAllBytes(Paths.get(file.toString())));
							//replace the first char and append the rest of the string back
							info = "1" + info.substring(1);
							//write the file back out
							Files.write(Paths.get(file.toString()), info.getBytes());
							
							found = true;
						}
						else {
							//otherwise its invalid
							found = false;
						}
						
						scanner.close();
						return found;

					}
				}
			}//for
			//if the file isn't found then the account doesn't exist so no login
			return found;
		}//login
		
		private void CreateBankAccount() {
			
		}
		private void DeleteBankAccount() {
			
		}
		private void Withdraw() {
			
		}
		private void deposit() {
			
		}
		
	}


	//helpers for server
	private void dailyUpkeep() {
		//a seperate thread should run this once a day 
	}
	private void monthlyUpdate() {
		//a seperate thread should run this once a month
		//used for things like interest, etc.
	}
	
}
