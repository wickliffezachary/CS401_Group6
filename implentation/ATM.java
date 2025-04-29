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
		Message temp = null;
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
	public void logout() throws IOException {
		sendMessage(new Message(me, "Server", "Requesting logout", Message.Type.LOGOUTREQATM));
		//wait for server ok or not?
	}

	//must be on own thread
	public void autoLogout() {
	
	}
	
	public void selectAccount(String accnum) throws IOException //accnum supplied when user action triggers gui event which calls this
	{
		sendMessage(new Message(me, "Server", accnum, Message.Type.ACCESSBAREQ));
		//wait for server response message
		//if ENTERBAREQGRANTED type message, trigger gui 
		//elif ENTERBAREQDENIED type message, trigger gui to display error
		Message serverresp = parseRecMessage();
		if (serverresp.getType()==Message.Type.ACCESSBAREQGRANTED){
			//trigger next GUI screen
		}
		else if (serverresp.getType()==Message.Type.ACCESSBAREQDENIED){
			//trigger error popup on GUI
		}
	}
	
	public void exitAccount(String accnum) throws IOException {
		sendMessage(new Message(me, "Server", accnum, Message.Type.EXITBAREQ));
		Message serverresp = parseRecMessage();
		if (serverresp.getType()==Message.Type.EXITBAREQGRANTED){
			//trigger next GUI screen
		}
		else if (serverresp.getType()==Message.Type.EXITBAREQDENIED){
			//???????????
		}
	}

	//todo
	public void withdraw(double amt) throws IOException {
		if(amt>getCurrReserve()){
		//trigger gui error
		}
		else{
		sendMessage(new Message(me, "Server", String.valueOf(amt) , Message.Type.WITHDRAWREQ));
			//wait for server resp
			//...
		
		}
	}
	
	public void viewBalance(String accnum) throws IOException {
		sendMessage(new Message(me, "Server", accnum+",Balance", Message.Type.GETREQ));
		//wait for data from server
		Message serverresp = parseRecMessage();
		if (serverresp.getType()==Message.Type.GETREQGRANTED){
		//send curr bal to GUI
		}
		else{
		//errr popup on GUI
		}
	}
	
	public void viewTransactionHistory(String accnum) throws IOException {
		sendMessage(new Message(me, "Server", accnum + ",History", Message.Type.GETREQ));
		//wait for data from server
		Message serverresp = parseRecMessage();
		if (serverresp.getType()==Message.Type.GETREQGRANTED){
		//send transaction history to GUI
		}
		else{
		//errr popup on GUI
		}
	}
	
	public void deposit(double amt) throws IOException {
		sendMessage(new Message(me, "Server", String.valueOf(amt), Message.Type.DEPOSITREQ));
		//wait for server confirmation
		Message serverresp = parseRecMessage();
		if (serverresp.getType()==Message.Type.DEPOSITDONE){
		//send transaction history to GUI
		}
		else if (serverresp.getType()==Message.Type.ERROR){
		//errr popup on GUI
		}
	}
	
	public double getCurrReserve() {
		return this.cashInMachine;
	}

	//triggered by a separate thread which is always listening for server's refill message
	public void updateCurrReserve(double refill_amt)//refill_amt sent by server
	{
		this.cashInMachine = refill_amt;
	}
}
