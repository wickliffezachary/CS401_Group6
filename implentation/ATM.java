import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



public class ATM {
	
	// the GUI needs to be able to update in the event we receive a message
	// the GUI has no way of knowing unless the ATM object tells it
	// the listener essentially allows us to send messages from the ATM to the GUI
    public interface ATMListener {
        void receivedMessage(Message msg);
    }
	
	
	// Fields:
    private Socket socket = null;
    private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;
    private ATMListener listener;
    private String id;
    private double cashInMachine = 0.0;
    private static int count = 0;
    private boolean loggedInUser;
	
    // ATM - Constructor
	// when an ATM is created, connect it to the server and listener
	// and give it some money
	public ATM(String host, int port, ATMListener listener, double initialReserve) throws IOException {
		this.listener = listener;
		this.socket = new Socket(host, port);
	    this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		this.objectInputStream = new ObjectInputStream(socket.getInputStream());
	    count += 1;
		this.id = "ATM" + count;
		this.loggedInUser = false;
		this.cashInMachine=initialReserve;

		Thread listenerThread = new Thread(new ServerListener());
        listenerThread.start();
	}

	// helper method for sending messages to server
	private void sendMessage(Message message) throws IOException {
		// write an output object to the server
		objectOutputStream.writeObject(message);
		// flush the output stream to keep it clear
		objectOutputStream.flush();
	
	}
	
	// // helper method to read a received message 
	// private Message parseReceivedMessage() throws IOException {
	// 	Message temp = null;
		
	// 	try {
	// 		// read the received message
	// 		temp = (Message) objectInputStream.readObject();
	// 		// //pass message to GUI so GUI can update accordingly
	// 		// listener.receivedMessage(temp);
	// 	}
	// 	catch (ClassNotFoundException error) {
	// 		error.printStackTrace();
	// 	}
		
	// 	return temp;
	// }
	
	// method that logs a user in
	public void login(String firstName, String lastName,String phoneNumber, String password) throws IOException {
		// received the customer's name, phone number, and password from the GUI 
		String loginCreds = "username=" + firstName + lastName + phoneNumber + ",password=" + password;
		// send a message to the server that the user is requesting to log in
		sendMessage(new Message(id, "Server", loginCreds, Message.Type.LOGINREQATM));
	}

	// method that logs a user out
	public void logout() throws IOException {
		// send a message to the server that the user is requesting to log out
		sendMessage(new Message(id, "Server", "Requesting Logout", Message.Type.LOGOUTREQATM));
	}

	// TODO0000000000OOOOOOO
	// triggered by GUI
	// where it must be on its own thread
	public void autoLogout() {
	
	}
	
	// method that returns whether or not a user is logged in
	public boolean isLoggedInUser() {
		return loggedInUser;
	}

		// method that allows a user to select a financial account
	// (the account number is supplied when user action triggers GUI event that calls this method)
	public void selectAccount(String accNum) throws IOException {
		// if the user is not logged in, then they should not be allowed to select any account
		// so exit this method call
		if (!loggedInUser) {
			return;
		}
		// send a message to the server that the user is requesting to access a financial account
		sendMessage(new Message(id, "Server", accNum, Message.Type.ACCESSBAREQ));
	}
	
	// method that allows a user to exit a financial account
	public void exitAccount(String accNum) throws IOException {
		// if the user is not logged in, then they should not be allowed to exit any account
		// so exit this method call
		if (!loggedInUser) {
			return;
		}
		// send a message to the server that the user is requesting to exit the financial account previously selected
		sendMessage(new Message(id, "Server", accNum, Message.Type.EXITBAREQ));
	}

	// method that allows a user to withdraw money from their financial account
	public void withdraw(double amount) throws IOException {
		// if the user is not logged in, then they should not be allowed to withdraw from any account
		// so exit this method call
		if (!loggedInUser) {
			return;
		}
		// if the amount to withdraw is greater than the current cash reserve in the ATM, then trigger an error pop-up on the GUI
		if (amount > getCurrReserve()) {
			// TODO - trigger GUI error
		}
		// else, initiate the withdraw request
		else {
			// send a message to the server that the user wants to withdraw from their financial account
			sendMessage(new Message(id, "Server", String.valueOf(amount) , Message.Type.WITHDRAWREQ));
		}
	}
	
	// method that allows a user to view their financial account balance
	public void viewBalance(String accNum) throws IOException {
		// if the user is not logged in, then they should not be allowed to view any account's balance
		// so exit this method call
		if (!loggedInUser) {
			return;
		}
		sendMessage(new Message(id, "Server", accNum + ",Balance", Message.Type.GETREQ));
	}
	
	// method that allows a user to view their account transaction history
	public void viewTransactionHistory(String accNum) throws IOException {
		// if the user is not logged in, then they should not be allowed to view any account's transaction history
		// so exit this method call
		if (!loggedInUser) {
			return;
		}
		// send a message to the server that the user is requesting to exit the financial account previously selected
		sendMessage(new Message(id, "Server", accNum + ",History", Message.Type.GETREQ));
	}
	
	// method that allows a user to deposit money into one of their financial accounts
	public void deposit(double amount) throws IOException {
		// if the user is not logged in, then they should not be allowed to deposit to any account
		// so exit this method call
		if (!loggedInUser) {
			return;
		}
		// send a message to the server that the user is requesting to exit the financial account previously selected
		sendMessage(new Message(id, "Server", String.valueOf(amount), Message.Type.DEPOSITREQ));
	}
	
	// method that returns the current cash reserves in the ATM machine
	public double getCurrReserve() {
		return this.cashInMachine;
	}

	// method that updates the cash reserves in the ATM machine
	// (this method is triggered by a separate thread which is always listening for the server's refill message)
	// (refillAmount is sent by the server in the data field of message, extracted by the thread when message is received, and passed to this funvtion)
	public void updateCurrReserve(double refillAmount) {
		this.cashInMachine = refillAmount;
	}
	
	// Background thread that listens for all server messages
    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Message msg = (Message) objectInputStream.readObject();

                    switch (msg.getType()) {
                        case REFILLATM:
                            try {
                                double refillAmount = Double.parseDouble(msg.getData());
                                updateCurrReserve(refillAmount);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid refill amount: " + msg.getData());
                            }
                            break;

                        case LOGINOK:
                            loggedInUser = true;
							String acc = msg.getData();
							// trigger GUI and send it accounts names
							// spawn autologout thread in gui
                            break;

                        case LOGINDENIED:
							//trigger GUI login denied popup
                            //loggedInUser = false;
                            break;

                        case LOGOUTOK:
                            loggedInUser = false;
							// stop autologout 
							// send GUI back to login screen
                            break;

						case ACCESSBAREQGRANTED:
							// trigger next GUI screen
							break;
							
						case ACCESSBAREQDENIED:
							// trigger access denied, try gain later popup 
							break;

						case EXITBAREQGRANTED:
							// GUI back to acc selection screen
							break;

						case WITHDRAWREQACCEPTED:
							updateCurrReserve(getCurrReserve() - amount);
							// GUI shows successful wthdrawal message
							break;

						case GETBALREQGRANTED:
							String bal = serverResponse.getData();
							// send balance to GUI and trigger display
							break;

						case GETHISTREQGRANTED:
							String th = serverResponse.getData();
							// send history  to GUI and trigger display
							break;

						case DEPOSITDONE:
							//trigger success message in GUI
							break;
							
						case EXITBAREQDENIED:
						case IVALID:
						case ERROR:
                            // trigger GUI generic error, maybe with a call manager option
                            break;
						default:
							// something went wrong
                    }

                    // Notify the GUI of every message
                    if (listener != null) {
                        listener.receivedMessage(msg);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("ServerListener stopped: " + e.getMessage());
            }
        }
    }

	
	// // test method for logging in
	// // (for use until the GUI is implemented)
	// public void testLogin(String firstName, String lastName, String phoneNumber, String password) throws IOException {
	// 	// received the customer's name, phone number, and password from the GUI 
	// 	String loginCreds = "username=" + firstName + lastName + phoneNumber + ",password=" + password;
		
	// 	// send a message to the server that the user is requesting to log in
	// 	sendMessage(new Message(id, "Server", loginCreds, Message.Type.LOGINREQATM));
		
	// 	// get the response message back from the server
	// 	Message serverResponse = parseReceivedMessage();
		
	// 	// if the response message is of type LOGIN_OK, then the user is logged in and the GUI is triggered
	// 	if (serverResponse.getType() == Message.Type.LOGINOK){
	// 		loggedInUser = true;
	// 		System.out.println("loggedin");
	// 	}
	// 	// else, if the response message is of type LOGIN_DENIED, then the user entered incorrect credentials
	// 	else if (serverResponse.getType() == Message.Type.LOGINDENIED) {
	// 		System.out.println("Incorrect credentials");
	// 	}
	// 	// else, if the response message is of any other type, then print out an error message
	// 	else{
	// 		System.out.println("Some error, check more");
	// 	}
	// 	// and spawn a GUI thread for auto-logout
	// 	// else if LOGIN_DENIED type message, trigger GUI to display error
	// }
	
	// // test method for logging out
	// // (for use until the GUI is implemented)
	// public void testLogout() throws IOException {
	// 	// send a message to the server that the user is requesting to log out
	// 	sendMessage(new Message(id, "Server", "Requesting logout", Message.Type.LOGOUTREQATM));
		
	// 	// get the response message back from the server
	// 	Message serverResponse = parseReceivedMessage();
		
	// 	// if the response message is of type LOGIN_OK, then the user is logged out
	// 	if (serverResponse.getType() == Message.Type.LOGOUTOK){
	// 		loggedInUser = false;
	// 		System.out.println("loggedout");
	// 	}
	// 	// else, if the response message is of any other type, then print out an error message
	// 	else {
	// 		System.out.println("some error, check more");
	// 	}
	// 	// kill the auto-logout timer thread
	// 	// send the GUI to the login page
	// }
}
