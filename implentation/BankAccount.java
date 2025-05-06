import java.util.ArrayList;
import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
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
	private String transactionHist;
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
			this.transactionHist = "";
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
		this.transactionHist = th;
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

	// helper method for when we update customer name or phone number because custID=name+phnum..uses add and rem user
	public void renameUser(String olduser, String newuser) {
		this.users.remove(olduser);
		this.users.add(newuser);
		save();
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
		return this.transactionHist;
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
		this.transactionHist = this.transactionHist + entry;
		save();
	}
	
	public void switchAccess() {
		this.inAccess = !this.inAccess;
		save();
	}
	
	public static BankAccount loadFromFile(String id) throws IOException {
	    String path = System.getProperty("user.dir")
	                + "/data/bankAccounts/" + id + ".txt";
	    List<String> lines = Files.readAllLines(Paths.get(path));

	    // type
	    String typeStr = lines.get(0).split(":",2)[1].trim().toUpperCase();
	    AccType accType = AccType.valueOf(typeStr);

	    // skip file’s date—use now
	    Date dateCreated = new Date();

	    // users
	    String usersLine = lines.get(2).split(":",2)[1].trim()
	                          .replace("[","").replace("]","");
	    ArrayList<String> users = new ArrayList<>();
	    if (!usersLine.isEmpty()) {
	        String[] parts = usersLine.split(",");
	        for (String u : parts) {
	            u = u.trim();
	            if (!u.isEmpty()) users.add(u);
	        }
	    }

	    // balance
	    double currBalance = Double.parseDouble(
	        lines.get(3).split(":",2)[1].trim()
	    );

	    // history
	    String tranHist = "";
	    if (lines.size() > 4) {
	        tranHist = lines.get(4).split(":",2)[1].trim();
	    }

	    BankAccount ba = new BankAccount(); 
	    ba.accountID = id;
	    ba.accType = accType;
	    ba.dateCreated = dateCreated;
	    ba.users = users;
	    ba.currBalance = currBalance;
	    ba.transactionHist = tranHist;
	    return ba;
	}





	public void deposit(double amount) {
	    this.currBalance += amount;
	    this.transactionHist += "Deposited: " + amount + "\n";
	    save();
	}

	public void withdraw(double amount) {
	    this.currBalance -= amount;
	    this.transactionHist += "Withdrew: " + amount + "\n";
	    save();
	}

	
	// save to file
	// just like dvdcollection
	// save to file
	public void save() {
	    String sourceName = System.getProperty("user.dir")
	                      + "/data/bankAccounts/"
	                      + this.accountID
	                      + ".txt";
	    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    List<String> lines = Arrays.asList(
	        "Account_type: " + this.accType,
	        "Date_created: " + fmt.format(this.dateCreated),
	        "Users: " + this.users.toString(),
	        "Current_balance: " + this.currBalance,
	        "Transaction_history: " + this.transactionHist
	    );
	    try {
	        Files.write(Paths.get(sourceName), lines);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
