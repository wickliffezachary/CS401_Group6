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
		this.accountID=accid;
		this.acc_type=t;
		this.date_created=d;
		this.curr_balance=cb;
		this.tranction_hist=th;
		//this.in_access=true;
		//****IMPORTANT****
		//server must change in_access field to true in CustAcc file whenever this constr is called
		//and back to false when in exitCA function
		//******END OF IMPORTANT*****
		//do we want to have server handle all that directlhy
		//or have checkaccess, switchaccess, save funcs here???????????????
		//if we have save, we can also use it to update custacc file when we update any cust info, add/del bankaccs etc
		this.users=new ArrayList<String>();
		for (int i = 0; i < usrs.size(); i++) {
		      this.users.add(usrs.get(i));
		}
	}

	// BankAccount - Constructor
	// this constructor is used when loading from file
	public BankAccount(String user, AccType type) {
			id+=1;
			this.accountID=id+"0";
			this.acc_type=type;
			this.users=new ArrayList<String>();
			users.add(user);
			this.date_created=new Date();
			this.tranction_hist="";
	}
	
	// BankAccount - Default Constructor
	// this constructor is here so that the code does not crash
	public BankAccount() {
		//errrrrrrrrr
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
		// bool #helper meth for when we update customer name or ph num because custID=name+phnum..uses add and rem user
	}
	
	public String get_accID() {
		return this.accountID;
	}
	
	public ArrayList<String> get_users(){
		return this.users;
	}
	
	public double get_balance() {
		return this.curr_balance;
	}
	
	public String get_hist() {
		return this.tranction_hist;
	}
	
	public AccType get_type() {
		return this.acc_type;
	}
	
	public void update_bal(double new_bal) {
		this.curr_balance=new_bal;
		save();
	}
	
	public void update_hist(String entry) {
		this.tranction_hist=this.tranction_hist+entry;
		save();
	}
	
	public Boolean check_in_active_access() {
		return this.in_access;
	}
	
	public void switch_access() {
		this.in_access=!this.in_access;
	}
	
	//todo
	public void save() {
		//save to file
		//just like dvdcollection
	}
}

