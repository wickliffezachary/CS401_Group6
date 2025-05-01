import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Teller {
	
	private TellerListener listener;
	private Socket socket = null;
   	private ObjectInputStream objectInputStream = null;
   	private ObjectOutputStream objectOutputStream = null;
	private static int ct=0;
	private String me;
	private boolean loggedinteller;

	public interface TellerListener {
        	void receivedMessage(Message msg);
	}
	
	public Teller(String host, int port, TellerListener listener) throws IOException {
		this.listener=listener;
		this.socket = new Socket(host, port);

		this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		this.objectInputStream = new ObjectInputStream(socket.getInputStream());
	        ct+=1;
		this.me="Teller"+ct;
		this.loggedinteller=false;
	}
	
	//helper to read in a message 
	private Message parseRecMessage() throws IOException {
		Message temp = null;
		try {
			//read in message
			temp = (Message) objectInputStream.readObject();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return temp;
	}

	private void sendMessage(Message message) throws IOException {
		//write object to server
		objectOutputStream.writeObject(message);
		//flush to keep stream clear
		objectOutputStream.flush();
	}
	
	public void login(String tellerid, String pswd) throws IOException {
		//received custname, phno, pswd from gui 
		String login_creds="uname="+tellerid+",pswd="+pswd;
		//send message to server
		sendMessage(new Message(me, "Server", login_creds, Message.Type.LOGINREQTELLER));
		//wait for server response message
		//if loginok type message, 
		Message serverresp = parseRecMessage();
		if (serverresp.getType()==Message.Type.LOGINOK){
			loggedinteller=true;
			//and trigger gui by also sending contents of data field (list of bank accounts of customer) of message 
		}
		else if (serverresp.getType()==Message.Type.LOGINDENIED){
			//elif logindenied type message, trigger gui to displaycreds  error
		}
		else{
			//trigger gui error
		}
	}

	public void logout()  throws IOException {
		sendMessage(new Message(me, "Server", "Requesting logout", Message.Type.LOGOUTREQTELLER));
		//wait for server ok or not??????
		//if logoutok type message, 
		this.loggedinteller=false;
		//send gui to login page
		//else err
	}
	
	public void selectCustomer() {
		
	}
	public void exitCustomer() {
		
	}
	public void selectAccount() {
		
	}
	public void exitAccount() {
		
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
		//lolz does nothing
		//ok, maybe a gui lie saying "manager alerted, please wait for assistance"
	}


	//for testing server-client prior to having our GUI
	public void testlogin(String tellerid, String pswd) throws IOException {
		//received custname, phno, pswd from gui 
		String login_creds="uname="+tellerid+",pswd="+pswd;
		//send message to server
		sendMessage(new Message(me, "Server", login_creds, Message.Type.LOGINREQTELLER));
		//wait for server response message
		Message serverresp = parseRecMessage();
		if (serverresp.getType()==Message.Type.LOGINOK){
		//if loginok type message, 
		loggedinteller=true;
		
		//and trigger gui by also sending contents of data field (list of bank accounts of customer) of message 
		System.out.println("loggedin");
		}
		else if (serverresp.getType()==Message.Type.LOGINDENIED){
		System.out.println("Incorrect creds");
		}
		else{
		System.out.println("some error, check more");
		}
		//and spawn gui thread for autologout
		//elif logindenied type message, trigger gui to display error
	}
	public void testlogout() throws IOException {
		sendMessage(new Message(me, "Server", "Requesting logout", Message.Type.LOGOUTREQTELLER));
		//wait for server ok or not?
		Message serverresp = parseRecMessage();
		if (serverresp.getType()==Message.Type.LOGOUTOK){
		//if logoutok type message, 
		loggedinteller=false;
		//and trigger gui by also sending contents of data field (list of bank accounts of customer) of message 
		System.out.println("loggedout");
		}
		else {
		System.out.println("some error, check more");
		}
		//send gui to login page
	}
}
