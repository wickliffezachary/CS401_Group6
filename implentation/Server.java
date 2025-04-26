import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	
	//determine where accounts will be stored
	static File accountListFolder;

	public static void main(String[] args) {
		ServerSocket server = null;
		//set the location when the server starts, this will be passed to all threads
		accountListFolder = new File(System.getProperty("user.dir"));
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
					= new ClientHandler(client, accountListFolder);

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
        //folder where accounts are stored for file i/o
        private final File accountListFolder;

		//Thread constructor to handle socket
		public ClientHandler(Socket socket, File folder) throws IOException
		{
			this.clientSocket = socket;
			this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
			this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			this.accountListFolder = folder;
		}
		
		public void run() {
			
			//variables to hold data to change
			Message message;
			Boolean LOGGEDIN = false;
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
		        	
		        	//if the user isn't logged in yet but is making a request to
		        	if(!LOGGEDIN && (message.getType() == Message.Type.LOGINREQATM || message.getType() == Message.Type.LOGINREQTELLER)) {
		        		//log them in
		        		LOGGEDIN = true;
		        		User = message.getData();
		        		//respond that the login was successful
		        		sendMessage(
		        				new Message(
		        						"Server", clientSocket.getInetAddress().toString(), "Login successful", Message.Type.LOGINOK));
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
	
		        	
		        	//below this is where commands for logged in users go
		        	
		        	
		        	if(message.getType() == Message.Type.) {
		        		
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
