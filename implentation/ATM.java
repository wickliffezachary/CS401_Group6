import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



public class ATM {
	
	//the gui needs to be able to update in the event we receive a message
	//the gui has no way of knowing unless the atm object tells it
	//the listener essentially allows us to send messages from the atm to the gui
    public interface ATMListener {
        void receivedMessage(Message msg);
    }
	
	
	//variables to store key data
	private double cashInMachine = 0.0;
	private Socket socket = null;
    private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;
    private ATMListener listener;
	
    //when an atm is created connect it to the server and listener
	public ATM(String host, int port, ATMListener listener) throws IOException {
		this.listener = listener;
		this.socket = new Socket(host, port);
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        
	}

	//helper function for sending messages to server
	private void sendMessage(Message message) throws IOException {
		//write object to server
		objectOutputStream.writeObject(message);
		//flush to keep stream clear
		objectOutputStream.flush();
	}
	
	// //helper to read in a message then send it to the gui to update
	// private void parseRecMessage() throws IOException {
	// 	try {
	// 		//read in message
	// 		Message temp = (Message) objectInputStream.readObject();
	// 		//pass message to gui so gui can update accordingly
	// 		listener.receivedMessage(temp);
	// 	} catch (ClassNotFoundException e) {
	// 		e.printStackTrace();
	// 	}
	// }
	
	//login method
	public void login() throws IOException {
		//receive custname, phno, pswd from gui 
		String fname,lname,phno,pswd;
		//assunimg we have those
		String login_creds="uname="+fname+lname+phno+",pswd="+pswd;
		//send message to server
		sendMessage(new Message("ATM", "Server", login_creds, Message.Type.LOGINREQATM));
		//wait for server response message
		//if loginok type message, trigger gui by also sending data (list of bank accounts of customer) of message 
		//elif logindenied type message, trigger gui to display error
	}
	public void logout() {

	}
	public void autoLogout() {

	}
	public void selectAccount() {

	}
	public void exitAccount() {

	}

	public void withdraw() {

	}
	public void viewBalance() {

	}
	public void viewTransactionHistory() {

	}
	public void deposit() {

	}
	public void getCurrReserve() {

	}
	public void updateCurrReserve() {

	}
}
