import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Teller {
	
	// Fields:
	private Socket socket = null;
   	private ObjectInputStream objectInputStream = null;
   	private ObjectOutputStream objectOutputStream = null;
	private TellerListener listener;
	private String id;
	private static int count = 0;
	private boolean loggedInTeller;

	private String currentUsername;
	private BankAccount currentBankAccount;
	private boolean connected;	//is Teller connected to Server?

	
	public interface TellerListener {
        void receivedMessage(Message msg);
	}
	
	//default constructor for unit testing
	public Teller() {
		count++;
		id = "Teller" + count;
	}
	
	// Teller - Constructor
	public Teller(String host, int port, TellerListener listener) throws IOException {
		this.socket = new Socket(host, port);
		this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		this.objectInputStream = new ObjectInputStream(socket.getInputStream());
		this.listener = listener;
		count += 1;
		this.id = "Teller" + count;
		this.loggedInTeller = false;
	}
	
	// helper method for sending messages to server
	private void sendMessage(Message message) throws IOException {
		
		// write object to server
		objectOutputStream.writeObject(message);
		
		// flush to keep stream clear
		objectOutputStream.flush();
	
	}
	
	// helper method to read a received message 
	private Message parseReceivedMessage() throws IOException {
		Message temp = null;
		
		try {
			// read the received message
			temp = (Message) objectInputStream.readObject();
		} 
		catch (ClassNotFoundException error) {
			error.printStackTrace();
		}
		
		return temp;
	}
	
	// method that logs a teller in
	public void login(String tellerID, String password) throws IOException {
		// received the teller's username and password from the GUI
		String loginCreds = "username=" + tellerID + ",password=" + password;
		
		// send a message to the server that the teller is requesting to log in
		sendMessage(new Message(id, "Server", loginCreds, Message.Type.LOGINREQTELLER));
		
		// get the response message back from the server
		Message serverResp = parseReceivedMessage();
		
		if (listener != null) listener.receivedMessage(serverResp);
		
		// if the response message is of type LOGIN_OK, then the teller is logged in and the GUI is triggered
		if (serverResp.getType() == Message.Type.LOGINOK) {
			loggedInTeller=true;
		}
		// else, if the response message is of type LOGIN_DENIED, then the teller entered incorrect credentials
		else if (serverResp.getType() == Message.Type.LOGINDENIED) {
			// TODO
		}
		// else, if the response message is of any other type, then trigger an error pop-up on the GUI
		else {
			// TODO
		}
	}

	// TODO
	// method that logs a teller out
	public void logout()  throws IOException {
		// send a message to the server that the teller is requesting to log out
		sendMessage(new Message(id, "Server", "Requesting logout", Message.Type.LOGOUTREQTELLER));
		
		//wait for server ok or not??????
		//if logoutok type message, 
		this.loggedInTeller = false;
		//send gui to login page
		//else err
	}
	
	// method that returns whether or not a teller is logged in
	public boolean isLoggedInTeller() {
		return loggedInTeller;
	}

	public int getCount() {
		return count;
	}
	
	public String getID() {
		return id;
	}
	
	// TODO
	// method that allows a teller to select a customer account
	public void selectCustomer(String customerUsername) throws IOException {
	    this.currentUsername = customerUsername;
	    sendMessage(new Message(id, "Server", customerUsername, Message.Type.ACCESSCAREQ));
	    Message resp = parseReceivedMessage();
	    if (listener != null) listener.receivedMessage(resp);
	}
	
	
	// TODO
	// method that allows a teller to exit a customer account
	public void exitCustomer() {
		
	}
	
	// TODO
	// method that allows a teller to select a customer's financial account
	public void selectAccount(String baId) throws IOException, ClassNotFoundException {
	    currentBankAccount = BankAccount.loadFromFile(baId);
	}
	
	// TODO
	// method that allows a teller to exit a customer's financial account
	public void exitAccount() {
		
	}
	
	// TODO
	// method that allows a teller to withdraw money from a customer's financial account
	public void withdraw() {
		
	}
	
	// TODO
	// method that allows a teller to deposit money into a customer's financial account
	public void deposit() {
		
	}
	
	// TODO
	// method that allows a teller to view a customer's financial account balance
	public void viewBalance() {
		
	}
	
	// TODO
	// method that allows a teller to view a customer's transaction history
	public void viewTransactionHistory() {
		
	}
	
	// method that allows a teller to create a new customer account for a customer
	public void createNewCustomer(String first, String last,String phone, String address, String password) throws IOException {
		String data = String.join(",", first, last, phone, address, password);
		sendMessage(new Message(id, "Server", data, Message.Type.CREATCBACCREQ));
		Message resp = parseReceivedMessage();
		if (listener != null) listener.receivedMessage(resp);
	}
	
	// TODO
	// method that allows a teller to create a new financial account for a customer
	public void createNewBankAccount(String accType) throws IOException {
	    String data = currentUsername + "," + accType;
	    sendMessage(new Message(id, "Server", data, Message.Type.CREATEBACCREQ));
	    Message resp = parseReceivedMessage();
	    if (listener != null) listener.receivedMessage(resp);
	}

	
	public BankAccount getCurrentBankAccount() {
	    return currentBankAccount;
	}
	
	// TODO
	// method that allows a teller to close a customer's financial account
	public void closeBankAccount() {
		
	}
	
	// TODO
	// method that allows a teller to add another (existing) user to a customer's financial account
	public void addUserToBankAccount() {
		
	}
	
	// TODO
	// method that allows a teller to remove a user from a customer's financial account
	public void removeUserFromBankAccount() {
		
	}
	
	// TODO
	// method that allows a teller to update a customer's account information
	public void updateCustomerInfo(String field, String value) throws IOException {
	    String data = currentUsername + "," + field + "," + value;
	    sendMessage(new Message(id, "Server", data, Message.Type.CHANGECUSTOMERINFOREQ));
	    Message resp = parseReceivedMessage();
	    if (listener != null) listener.receivedMessage(resp);
	}
	
	// TODO
	// method that allows a teller to call a manager
	public void callManager() {
		
	}
	


	// test method for logging in
	// (for use until the GUI is implemented)
	public void testLogin(String tellerID, String password) throws IOException {
		// received the teller's username and password from the GUI
		String loginCreds = "username=" + tellerID + ",password=" + password;
		
		// send a message to the server that the teller is requesting to log in
		sendMessage(new Message(id, "Server", loginCreds, Message.Type.LOGINREQTELLER));
		
		// get the response message back from the server
		Message serverResponse = parseReceivedMessage();
		
		// if the response message is of type LOGIN_OK, then the teller is logged in
		if (serverResponse.getType() == Message.Type.LOGINOK) {
			loggedInTeller = true;
			System.out.println("loggedin");
		}
		// else, if the response message is of type LOGIN_DENIED, then the teller entered incorrect credentials
		else if (serverResponse.getType() == Message.Type.LOGINDENIED) {
			System.out.println("Incorrect credentials");
		}
		// else, if the response message is of any other type, then print out an error message
		else {
			System.out.println("Some error, check more");
		}
		// and spawn GUI thread for auto-logout
		// else if LOGIN_DENIED type message, trigger GUI to display error
	}
	
	// test method for logging out
	// (for use until the GUI is implemented)
	public void testLogout() throws IOException {
		// send a message to the server that the teller is requesting to log out
		sendMessage(new Message(id, "Server", "Requesting logout", Message.Type.LOGOUTREQTELLER));
		
		// get the response message back from the server
		Message serverResponse = parseReceivedMessage();
		
		// if the response message is of type LOGOUT_OK, then the teller is logged out
		if (serverResponse.getType() == Message.Type.LOGOUTOK){
			loggedInTeller = false;
			System.out.println("loggedout");
		}
		// else, if the response message is of any other type, then print out an error message
		else {
			System.out.println("Some error, check more");
		}
		// send GUI to login page
	}
}