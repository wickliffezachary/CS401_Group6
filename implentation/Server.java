import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

	public static void main(String[] args) {
		ServerSocket server = null;
		
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
		
		//helper function for sending messages
		private void sendMessage(Message message) throws IOException {
			objectOutputStream.writeObject(message);
			objectOutputStream.flush();
		}
		
	}

}
