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
	private boolean inAccess;
	private String tranctionHist;
	
	// BankAccount - Constructor
	// this constructor is used when loading from file
	public BankAccount(String accid, String th, AccType t) {
		
	}
	
	// BankAccount - Constructor
	// this constructor is used when creating a new bank account
	public BankAccount(String user, AccType type) {
		
	}
	
	// BankAccount - Default Constructor
	// this constructor is here so that the code does not crash
	public BankAccount() {
		
	}
	
	public void addUser (String userID) {
		
	}
	
	public void removeUser(String userID) {
		
	}
	
	public void renameUser(String olduser, String newuser) {
		// bool #helper meth for when we update customer name or ph num because custID=name+phnum..uses add and rem user
	}
	
	public String getAccountID() {
		// TODO
		return null;
	}
	
	public ArrayList<String> getUsers(){
		// TODO
		return null;
	}
	
	public double getBalance() {
		// TODO
		return 0.0;
		
	}
	
	public String getHistory() {
		// TODO
		return null;
	}
	
	public AccType getType() {
		// TODO
		return null;
	}
	
	public void updateBalance(double newBalance) {
		
	}
	
	public void updateHist(String entry) {
		
	}
	
	public boolean checkInActiveStatus() {
		// TODO
		return false;
	}
	
	public void switchAccess() {
		
	}
}
