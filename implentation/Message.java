import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable{
	enum Type {
		INVALID, ERROR,										//error messages
		
		//login and out messages
		LOGINREQATM,LOGINREQTELLER, LOGINOK, LOGINDENIED, 	//atm/teller account login messages
		LOGOUTREQATM, LOGOUTREQTELLER, LOGOUTOK,			//atm/teller account logout messages
		ACCESSCAREQ, ACCESSCAREQGRANTED, ACCESSCAREQDENIED, //Customer account login messages
		EXITCA, EXITCADONE, 								//Customer account logout messages
		ACCESSBAREQ, ACCESSBAREQGRANTED, ACCESSBAREQDENIED, //Bank account login messages
		EXITBA, EXITBADONE, 								//Bank account logout messages
		
		//withdraw and desposit messages
		WITHDRAWREQ, WITHDRAWREQACCEPTED, WITHDRAWDONE, 	//withdraw messages
		DEPOSITREQ, DEPOSITREQACCEPTED, DEPOSITDONE,		//deposit messages
		
		//linking bank accounts and customer accounts messages
		ADDUSERREQ, ADDUSERDONE, 							//link Bank account to Customer account messages
		ADDCUSTOMERREQ, ADDCUSTOMERDONE, 					//is this different from the one above?
		REMOVEUSERREQ, REMOVEUSERDONE, 						//unlink BA to CA messages
		
		//creating and deleting account messages
		CREATCBACCREQ, CREATECACCDONE, 						//create customer account messages
		DELCAREQ, DELCADONE, 								//close customer account messages
		CREATEBACCREQ, CREATEBACCDONE, 						//create bank account messages
		DELBAREQ, DELBADONE, 								//close bank account messages
		
		//getting and modifying account messages
		GETCUSTOMERINFOREQ, GETCUSTOMERINFOREQGRANTED,		//get customer info message
		CHANGECUSTOMERINFOREQ, CHANGECUSTOMERINFODONE, 		//change customer info message
		
		//is this one necessary?
		REFILLATM
		}
 private static int count = 0;
 private final int id;
 private final String data;
 private final Type type;
 private final String from;
 private final String to;

//if a non-default constructor exists then java will not auto-create a default constructor
// private Message() {
//     this.data = "Invalid Message Object";
//     this.type = Type.INVALID;
//     this.id = ++count;
//     this.from="Sender";
//     this.to="Receiver";
// }
 public Message(String from, String to, String data, Type type) {
	 this.id = ++count;
	 this.from=from;
     this.to=to;
	 this.data = data;
     this.type = type;
     
 }
 public String getData() {
     return this.data;
 }
 public int getID(){
     return this.id;
 }
 public Type getType(){
     return this.type;
 }
 public String getFrom() {
	 return this.from;
 }
 public String getTo() {
	 return this.to;
 }
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
