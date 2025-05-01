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
	private static int count = 0;
	private String me;
	private boolean loggedInUser;
	
    //when an atm is created connect it to the server and listener
	public ATM(String host, int port, ATMListener listener) throws IOException {
		this.listener = listener;
		this.socket = new Socket(host, port);
	    this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		this.objectInputStream = new ObjectInputStream(socket.getInputStream());
	    count += 1;
		this.me = "ATM" + count;
		this.loggedInUser = false;
	}

	//helper function for sending messages to server
	private void sendMessage(Message message) throws IOException {
		//write object to server
		objectOutputStream.writeObject(message);
		//flush to keep stream clear
		objectOutputStream.flush();
	}
	
	//helper to read in a message 
	private Message parseRecMessage() throws IOException {
		Message temp = null;
		try {
			//read in message
			temp = (Message) objectInputStream.readObject();
			// //pass message to gui so gui can update accordingly
			// listener.receivedMessage(temp);
		} catch (ClassNotFoundException error) {
			error.printStackTrace();
		}
		return temp;
	}
	
	//login method
	//todo
	public void login(String firstName, String lastName,String phoneNumber, String password) throws IOException {
		//received customer name, phone number, password from gui 
		String loginCreds = "username=" + firstName + lastName + phoneNumber + ",password=" + password;
		//send message to server
		sendMessage(new Message(me, "Server", loginCreds, Message.Type.LOGINREQATM));
		//wait for server response message
		//if loginok type message, 
		loggedInUser=true;
		//and trigger gui by also sending contents of data field (list of bank accounts of customer) of message 
		//and spawn gui thread for autologout
		//elif logindenied type message, trigger gui to display error
	}

	//logout method
	//todo
	public void logout() throws IOException {
		sendMessage(new Message(me, "Server", "Requesting Logout", Message.Type.LOGOUTREQATM));
		//wait for server ok or not?
		//if logoutok type message, 
		loggedInUser=false;
		//kill autologout timer thread
		//send gui to login page
	}

	//must be on own thread
	public void autoLogout() {
	
	}
	
	public void selectAccount(String accNum) throws IOException //accnum supplied when user action triggers gui event which calls this
	{
		if(!loggedInUser) {return;} //our interface makes this an other functions unavailable until user logsin, but adding an extra check just in case
		//or should these only be in server?????
		sendMessage(new Message(me, "Server", accNum, Message.Type.ACCESSBAREQ));
		//wait for server response message
		//if ENTERBAREQGRANTED type message, trigger gui 
		//elif ENTERBAREQDENIED type message, trigger gui to display error
		Message serverResp = parseRecMessage();
		if (serverResp.getType() == Message.Type.ACCESSBAREQGRANTED){
			//trigger next GUI screen
		}
		else if (serverResp.getType() == Message.Type.ACCESSBAREQDENIED){
			//trigger error popup on GUI
		}
	}
	
	public void exitAccount(String accNum) throws IOException {
		if(!loggedInUser){return;} //our interface makes this an other functions unavailable until user logsin, but adding an extra check just in case
		//should there be a check for user in BA??
		sendMessage(new Message(me, "Server", accNum, Message.Type.EXITBAREQ));
		Message serverResp = parseRecMessage();
		if (serverResp.getType() == Message.Type.EXITBAREQGRANTED) {
			//trigger next GUI screen
		}
		else if (serverResp.getType() == Message.Type.EXITBAREQDENIED) {
			//smth
		}
	}

	public void withdraw(double amount) throws IOException {
		if(!loggedInUser){return;} //our interface makes this an other functions unavailable until user logsin, but adding an extra check just in case
		if(amount > getCurrReserve()) {
			//trigger gui error
		}
		else {
			sendMessage(new Message(me, "Server", String.valueOf(amount) , Message.Type.WITHDRAWREQ));
			//wait for server resp
			//...
			Message serverResp = parseRecMessage();
			if (serverResp.getType() == Message.Type.WITHDRAWDONE) {
				//imagine cash given out
				updateCurrReserve(getCurrReserve() - amount);
			}
			// if (serverresp.getType()==Message.Type.WITHDRAWREQACCEPTED){ //are we implementing server "temporarily" updates acc balance 
			// 	//and only permanently updates it after client confirms withdrawal???????????/
			// 	//imagine cash given out of machine
			// 	//decrease reserves
			// 	updateCurrReserve(getCurrReserve()-amt);
			// 	sendMessage(new Message(me, "Server", amt+"withdrawn" , Message.Type.WITHDRAWREQDONE); //and now server updates bal and daily lims
			// }
			else {
				//gui err popup
			}
		}
	}
	
	public void viewBalance(String accnum) throws IOException {
		if(!loggedInUser){return;} //our interface makes this an other functions unavailable until user logsin, but adding an extra check just in case
		sendMessage(new Message(me, "Server", accnum+",Balance", Message.Type.GETREQ));
		//wait for data from server
		Message serverResp = parseRecMessage();
		if (serverResp.getType() == Message.Type.GETREQGRANTED) {
		//send curr bal to GUI
		}
		else {
		//errr popup on GUI
		}
	}
	
	public void viewTransactionHistory(String accNum) throws IOException {
		if(!loggedInUser){return;} //our interface makes this an other functions unavailable until user logsin, but adding an extra check just in case
		sendMessage(new Message(me, "Server", accNum + ",History", Message.Type.GETREQ));
		//wait for data from server
		Message serverResp = parseRecMessage();
		if (serverResp.getType() == Message.Type.GETREQGRANTED) {
			//send transaction history to GUI
		}
		else {
			//errr popup on GUI
		}
	}
	
	public void deposit(double amount) throws IOException {
		if(!loggedInUser){return;} //our interface makes this an other functions unavailable until user logsin, but adding an extra check just in case
		sendMessage(new Message(me, "Server", String.valueOf(amount), Message.Type.DEPOSITREQ));
		//wait for server confirmation
		Message serverResp = parseRecMessage();
		if (serverResp.getType() == Message.Type.DEPOSITDONE) {
			//send transaction history to GUI
		}
		else if (serverResp.getType() == Message.Type.ERROR) {
			//errr popup on GUI
		}
	}
	
	public double getCurrReserve() {
		return this.cashInMachine;
	}

	//triggered by a separate thread which is always listening for server's refill message
	public void updateCurrReserve(double refillAmount)//refill_amt sent by server
	{
		this.cashInMachine = refillAmount;
	}

	
	public void testlogin(String firstName, String lastName,String phoneNumber, String password) throws IOException {
		//received customer name, phone number, password from gui 
		String loginCreds = "username=" + firstName + lastName + phoneNumber + ",pswd=" + password;
		//send message to server
		sendMessage(new Message(me, "Server", loginCreds, Message.Type.LOGINREQATM));
		//wait for server response message
		Message serverResp = parseRecMessage();
		if (serverResp.getType() == Message.Type.LOGINOK){
			//if loginok type message, 
			loggedInUser = true;
			//and trigger gui by also sending contents of data field (list of bank accounts of customer) of message 
			System.out.println("loggedin");
		}
		else if (serverResp.getType() == Message.Type.LOGINDENIED) {
			System.out.println("Incorrect credentials");
		}
		else{
			System.out.println("Some error, check more");
		}
		//and spawn gui thread for autologout
		//elif logindenied type message, trigger gui to display error
	}
	public void testlogout() throws IOException {
		sendMessage(new Message(me, "Server", "Requesting logout", Message.Type.LOGOUTREQATM));
		//wait for server ok or not?
		Message serverResp = parseRecMessage();
		if (serverResp.getType() == Message.Type.LOGOUTOK){
			//if logoutok type message, 
			loggedInUser = false;
			//and trigger gui by also sending contents of data field (list of bank accounts of customer) of message 
			System.out.println("loggedout");
		}
		else {
			System.out.println("some error, check more");
		}
		
		//kill autologout timer thread
		//send gui to login page
	}
}
