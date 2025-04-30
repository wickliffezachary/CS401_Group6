import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.net.InetAddress;


public class Teller {

	public static void main(String[] args) {
		Teller teller = new Teller(InetAddress.getLocalHost(), 1234);
		TellerGUI = new TellerGUI(teller);

	}


	public Teller(String serverAddress, int port) {
		private Socket socket;
		private ObjectOutputStream out;
		Private ObjectInputStream in;
		private String tellerId = "";
		private boolean loggedIn = false;

		// setup a client-server connection.
		try {
			socket = new Socket(serverAddress, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

			// create a message for teller login and send it.
			Message loginRequest = new Message("Teller", "Server", "Teller requesting to login", "LOGINREQTELLER")
			sendMessage(loginReqest);

			Message response = (Message) in.readObject();

			if (response.getType() == Message.Type.LOGINOK) {
                loggedIn = true;
                System.out.println("Teller login successful.");
            } else {
                System.out.println("Teller login failed.");
                socket.close();
            }
		}

		catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
		
	}
	
	public void parseRecMessage() {
		
	}
	public void login() {
		
	}
	public void logout() {
		
	}
	public void selectCustomer() {
		
	}
	public void exitCustomer() {
		
	}
	public void selectAccount() {
		
	}
	public void exitAccount() {
		
	}
	public void sendMessage(Message message) {
		out.writeObject(message);
		out.flush();
	}
	public void withdraw() {
		
	}
	public void deposit() {
		
	}
	public void viewBalance() {
		
	}
	public void viewTransactionHist() {
		
	}
	public void createNewCustomer() {
		
	}
	public void createNewBankAccount() {
		
	}
	public void closeBankAccount() {
		
	}
	public void addUserToBankAccount() {
		
	}
	public void removeUserFromBankAccount() {
		
	}
	public void updateCustomerInfo() {
		
	}
	public void callManager() {

	}
}
