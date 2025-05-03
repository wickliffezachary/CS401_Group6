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
	private boolean inAccess = false;
	private String tranctionHist;
	private String currentAccessor = ""; // used to ensure currAccessor can modify data but others can not if file is in access
	
	// BankAccount - Constructor
	// this constructor is used when creating a new bank account
	public BankAccount(String user, AccType type) {
			id+=1;
			this.accountID = id + "0";
			this.accType = type;
			this.users = new ArrayList<String>();
			users.add(user);
			this.dateCreated = new Date();
			this.tranctionHist = "";
	}

	// BankAccount - Constructor
	// this constructor is used when loading from file
	// accessor is customerAccount which we're in when we create this object
	public BankAccount(String access, String accid, AccType t, Date d, double cb, String th, ArrayList<String> usrs, String accessor) {
		this.inAccess = access;
		this.accountID = accid;
		this.accType = t;
		this.dateCreated = d;
		this.currBalance = cb;
		this.tranctionHist = th;
		this.users = new ArrayList<String>();
		for (int i = 0; i < usrs.size(); i++) {
		      this.users.add(usrs.get(i));
		}
		if (!this.inAccess) // no existing access when this object is created
		{
			switchAccess(); // change access status here and on file
			this.currentAccessor = accessor; 
		}
	}
	
	// BankAccount - Default Constructor
	// this constructor is here so that the code does not crash
	public BankAccount() {
		// TODO
	}

	public Boolean checkInActiveAccess() {
		return this.inAccess;
	}
	
	//*****IMPORTANT*******
	// before calling any of the following methods
	// server must check that either acc is !inAccess and make the requesting client the currentAccessor
	// or that if account is inAccess, the requesting client IS the currentAccessor
	// and only then allow these actions
	//*********************
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

	public Date getCreationDate(){
		return this.dateCreated;
	}
	
	public void updateBalance(double newBalance) {
		this.currBalance = newBalance;
		save();
	}
	
	public void updateHistory(String entry) {
		this.tranctionHist = this.tranctionHist + entry;
		save();
	}
	
	public void switchAccess() {
		this.inAccess = !this.inAccess;
		save();
	}
	
	// save to file
	// just like dvdcollection
	public void save() {
		String sourceName=System.getProperty("user.dir")+"/data/bankAccounts/"+this.accountID;
		try{
			FileWriter writer = new FileWriter(sourceName);
			w.write("Account_type: " + this.accType + "\nDate_created: " + this.dateCreated
				+ "\nUsers: " + users.toString() + "\nCurrent_balance: " + currBalance + "\nTransaction_history: " + transactionHist); 
			w.close()
		}
		catch (IOException e){}
	}
}

