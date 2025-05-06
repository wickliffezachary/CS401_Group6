import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable{

	enum Type {
		INVALID, ERROR,										// error messages

		// login and logout messages
		LOGINREQATM,LOGINREQTELLER, LOGINOK, LOGINDENIED, 	// atm/teller account login messages
		LOGOUTREQATM, LOGOUTREQTELLER, LOGOUTOK,			// atm/teller account logout messages
		ACCESSCAREQ, ACCESSCAREQGRANTED, ACCESSCAREQDENIED, // customer account login messages
		EXITCAREQ, EXITCAREQGRANTED, EXITCAREQDENIED,		// customer account logout messages
		ACCESSBAREQ, ACCESSBAREQGRANTED, ACCESSBAREQDENIED, // bank account login messages
		EXITBAREQ, EXITBAREQGRANTED, EXITBAREQDENIED,		// bank account logout messages

		// withdraw and deposit messages
		WITHDRAWREQ, WITHDRAWREQACCEPTED, WITHDRAWDONE, 	// withdraw messages
		DEPOSITREQ, DEPOSITREQACCEPTED, DEPOSITDONE,		// deposit messages

		// linking bank accounts and customer accounts messages
		ADDUSERREQ, ADDUSERDONE, 							// link bank account to customer account messages
		ADDCUSTOMERREQ, ADDCUSTOMERDONE, 					// is this different from the one above?
		REMOVEUSERREQ, REMOVEUSERDONE, 						// unlink BA to CA messages

		// creating and deleting account messages
		CREATECACCREQ, CREATECACCDONE, 						// create customer account messages
		DELCAREQ, DELCADONE, 								// close customer account messages
		CREATEBACCREQ, CREATEBACCDONE, 						// create bank account messages
		DELBAREQ, DELBADONE, 								// close bank account messages

		// getting and modifying account messages
		GETREQ, GETREQGRANTED,								// get customer info message
		CHANGECUSTOMERINFOREQ, CHANGECUSTOMERINFODONE, 		// change customer info message

		// is this one necessary?
		REFILLATM
	}

	// Fields:
	private static int count = 0;
	private final int id;
	private final String data;
	private final Type type;
	private final String from;
	private final String to;

	// if a non-default constructor exists, then Java will not auto-create a default constructor
	// private Message() {
	//     this.data = "Invalid Message Object";
	//     this.type = Type.INVALID;
	//     this.id = ++count;
	//     this.from="Sender";
	//     this.to="Receiver";
	// }

	// Message - Constructor
	// initialize all fields to the parameters passed in to this constructor
	public Message(String from, String to, String data, Type type) {
		this.id = ++count;
		this.from = from;
		this.to = to;
		this.data = data;
		this.type = type;
	}

	// method that returns the data within the Message object
	public String getData() {
		return this.data;
	}
	
	// method that returns the ID of the Message object
	public int getID() {
		return this.id;
	}
	
	// method that returns the Type of the Message object
	public Type getType() {
		return this.type;
	}
	
	// method that returns the sender of the Message object
	public String getFrom() {
		return this.from;
	}
	
	// method that returns the receiver of the Message object
	public String getTo() {
		return this.to;
	}

	// method that overrides the default toString method in Java
	public String toString() {
		return "Message{" +
				"id=" + id +
				", from='" + from + '\'' +
				", to='" + to + '\'' +
				", type=" + type +
				", data='" + data + '\'' +
				'}';
	}

}