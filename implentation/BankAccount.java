import java.util.ArrayList;
import java.util.*;
import java.time.*;

public class BankAccount {
	
	// Fields:
	enum AccType {SAVINGS, CHECKING}
	private String accountID;
	private static int id = 0;
	private AccType accType;
	private ArrayList<String> users;
	private Date dateCreated;
	private double currBalance;
	private boolean inAccess=false;
	private String tranctionHist;

	// BankAccount - Constructor
	// this constructor is used when creating a new bank account
	public BankAccount(String accid, AccType t, Date d, double cb, String th, ArrayList<String> usrs) {
		this.accountID = accid;
		this.accType = t;
		this.dateCreated = d;
		this.currBalance = cb;
		this.tranctionHist = th;
		//this.in_access = true;
		// ****IMPORTANT****
		// server must change inAccess field to true in CustAcc file whenever this constructor is called
		// and back to false when in exitCA function
		// ******END OF IMPORTANT*****
		//do we want to have server handle all that directly?
		//or have checkAccess, switchAccess, save methods here?
		//if we have save, we can also use it to update custAcc file when we update any customer info, add/del bankaccs, etc.
		this.users = new ArrayList<String>();
		for (int i = 0; i < usrs.size(); i++) {
		      this.users.add(usrs.get(i));
		}
	}

	// BankAccount - Constructor
	// this constructor is used when loading from file
	public BankAccount(String user, AccType type) {
			id+=1;
			this.accountID = id + "0";
			this.accType = type;
			this.users = new ArrayList<String>();
			users.add(user);
			this.dateCreated = new Date();
			this.tranctionHist = "";
	}
	
	// BankAccount - Default Constructor
	// this constructor is here so that the code does not crash
	public BankAccount() {
		
	}
	
	public void addUser (String user) {
		this.users.add(user);
		save();
	}
	
	public void removeUser(String user) {
		this.users.remove(user);
		save();
	}
	
	public void renameUser(String olduser, String newuser) {
		// bool #helper method for when we update customer name or phone number because custID=name+phnum..uses add and rem user
	}
	
	public String getAccountID() {
		return this.accountID;
	}
	
	public ArrayList<String> getUsers(){
		return this.users;
	}
	
	public double getBalance() {
		return this.currBalance;
	}
	
	public String getHistory() {
		return this.tranctionHist;
	}
	
	public AccType getType() {
		return this.accType;
	}
	
	public void updateBalance(double newBalance) {
		this.currBalance = newBalance;
		save();
	}
	
	public void updateHistory(String entry) {
		this.tranctionHist = this.tranctionHist + entry;
		save();
	}
	
	public Boolean checkInActiveAccess() {
		return this.inAccess;
	}
	
	public void switchAccess() {
		this.inAccess = !this.inAccess;
	}
	
	// TODO
	public void save() {
		//save to file
		//just like dvdcollection
	}
}

