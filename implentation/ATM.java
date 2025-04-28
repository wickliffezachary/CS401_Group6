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
	private static int ct=0;
	private String me;
	
    //when an atm is created connect it to the server and listener
	public ATM(String host, int port, ATMListener listener) throws IOException {
		this.listener = listener;
		this.socket = new Socket(host, port);
	        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
	        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
	        ct+=1;
		this.me="ATM"+ct;
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
		Message temp;
		try {
			//read in message
			temp = (Message) objectInputStream.readObject();
			// //pass message to gui so gui can update accordingly
			// listener.receivedMessage(temp);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	//login method
	public void login(String fname, String lname,String phno, String pswd) throws IOException {
		//received custname, phno, pswd from gui 
		String login_creds="uname="+fname+lname+phno+",pswd="+pswd;
		//send message to server
		sendMessage(new Message(me, "Server", login_creds, Message.Type.LOGINREQATM));
		//wait for server response message
		//if loginok type message, trigger gui by also sending contents of data field (list of bank accounts of customer) of message 
		//elif logindenied type message, trigger gui to display error
	}

	//logout method
	public void logout() {
		sendMessage(new Message(me, "Server", "Requesting logout", Message.Type.LOGOUTREQATM));
		//wait for server ok or not?
	}

	//must be on own thread
	public void autoLogout() {
	
	}
	
	public void selectAccount(String accnum) //accnum supplied when user action triggers gui event which calls this
	{
		sendMessage(new Message(me, "Server", accnum, Message.Type.ENTERBAREQ));
		//wait for server response message
		//if ENTERBAREQGRANTED type message, trigger gui 
		//elif ENTERBAREQDENIED type message, trigger gui to display error
		/*Message serverresp = parseRecMessage;
		if (serveresp.getType()==Message.Type.ENTERBAREQGRANTED){
			//trigger next GUI screen
		}
		else if (serveresp.getType()==Message.Type.ENTERBAREQDENIED){
			//trigger error popup on GUI
		}
		*/
	}
	public void exitAccount(String accnum) {
		sendMessage(new Message(me, "Server", accnum, Message.Type.EXITBAREQ));
		//Message serverresp = parseRecMessage;
		// if (serveresp.getType()==Message.Type.EXITBAREQGRANTED){
		// 	//trigger next GUI screen
		// }
		// else if (serveresp.getType()==Message.Type.EXITBAREQDENIED){
		// 	//???????????
		// }
	}

	public void withdraw(double amt) {
		if(amt>getCurrReserve()){
		//trigger gui error
		}
		else{
		sendMessage(new Message(me, "Server", amt, Message.Type.ATMWITHDRAWREQ));
			//wait for server resp
			//...
		}
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
