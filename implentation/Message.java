import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable{
	enum Type {INVALID,LOGINREQATM, LOGOUTREQATM, LOGINOK, LOGOUTOK, WITHDRAWDONE, WITHDRAWREQ,
		WITHDRAWREQACCEPTED,DEPOSITREQACCEPTED, EXITBADONE, EXITCADONE, REFILLATM,
		DEPOSITDONE, DEPOSITREQ, ADDUSEREQ, ADDUSERDONE, REMOVEUSER, REMOVEUSERDONE, ADDCUSTOMERREQ,
		ADDCUSTOMERDONE, ACCESSBAREQ, ACCESSBAREQGRANTED, ACCESSCAREQ, ACCESSCAREQGRANTED, CREATEBACCREQ,
		 CREATEBACCDONE, DELBAREQ, DELBADONE, CHANGECUSTOMERINFOREQ, CHANGECUSTOMERINFODONE, EXITBA,
		 EXITCA, LOGINREQTELLER, LOGOUTREQTELLER, ERROR, GETREQ}
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
