import java.io.Serializable;

public class Message implements Serializable{
	enum Type {INVALID,LOGINREQATM, LOGOUTREQATM, LOGINOK, LOGOUTOK, WITHDRAWDONE, WITHDRAWREQ,
		WITHDRAWREQACCEPTED,DEPOSITREQACCEPTED, EXITBADONE, EXITCADONE, REFILLATM,
		DEPOSITDONE, DEPOSITREQ, ADDUSEREQ, ADDUSERDONE, REMOVEUSER, REMOVEUSERDONE, ADDCUSTOMERREQ,
		ADDCUSTOMERDONE, ACCESSBAREQ, ACCESSBAREQGRANTED, ACCESSCAREQ, ACCESSCAREQGRANTED, CREATEBACCREQ,
		 CREATEBACCDONE, DELBAREQ, DELBADONE, CHANGECUSTOMERINFOREQ, CHAMGECUSTOMERINFODONE, EXITBA,
		 EXITCA, LOGINREQTELLER, LOGOUTREQTELLER, ERROR, GETREQ}
 private static int count = 0;
 private final int id;
 private final String data;
 private final Type type;
 private final String from;
 private final String to;

 public Message() {
     this.data = "Invalid Message Object";
     this.type = Type.INVALID;
     this.id = ++count;
     this.from="Sender";
     this.to="Receiver";
 }
 public Message(String f, String t, String d, Type ty) {
     this.data = d;
     this.type = ty;
     this.id = ++count;
     this.from=f;
     this.to=t;
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
}