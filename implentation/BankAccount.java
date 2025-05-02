import java.util.ArrayList;
import java.util.*;
import java.time.*;

public class BankAccount {
	
	enum AccType {SAVINGS, CHECKING}
	private String accountID;
	private static int id=0;
	private AccType acc_type;
	private ArrayList<String> users;
	private Date date_created;
	private double curr_balance;
	private Boolean in_access;
	private String tranction_hist;
	
	// constr for when loading from file
	public BankAccount(String accid, String th, AccType t, ....) {
		
	}
	
	//constr for when creating
	public BankAccount(String user, AccType type) {
		
	}
	
	// so code doesn't crash
	public BankAccount() {
		
	}
	
	public void addUser (String userID) {
		
	}
	
	public void removeUser(String userID) {
		
	}
	
	public void renameUser(String olduser, String newuser)
	{
		//: bool #helper meth for when we update customer name or ph num because custID=name+phnum..uses add and rem user
	}
	
	public String get_accID() {
		
	}
	
	public ArrayList<String> get_users(){
		
	}
	
	public double get_balance() {
		
	}
	
	public String get_hist() {
		
	}
	
	public AccType get_type() {
		
	}
	
	public void update_bal(double new_bal) {
		
	}
	
	public void update_hist(String entry) {
		
	}
	
	public Boolean check_in_active_access() {
		
	}
	
	public void switch_access() {
		
	}
}
