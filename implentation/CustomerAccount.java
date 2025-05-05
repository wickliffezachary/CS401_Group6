import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.*; 

public class CustomerAccount {

	// Fields:
	private String fullName;
	private String phoneNumber;
	private String address;
	private ArrayList<String> bankAccounts;
	private String password;
	private boolean inAccess = false;
	private BankAccount bankAccInUse = null;
	private String currentAccessor = ""; // used to ensure currAccessor can modify data but others can nt if file is in access
	
	// CustomerAccount - Constructor
	// this constructor is used when creating a new customer account for the first time
	// (parameters are sent from the GUI)
	public CustomerAccount(String name, String phoneNumber, String address, String password) {
		this.fullName = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.password = password;
		this.bankAccounts = new ArrayList<String>();
	}

	// CustomerAccount - Constructor
	// this constructor is used when loading pre-existing customer account information from a file
	// (parameters are sent by the server from a text file)
	// accesssor is the ATM or Teller who sent the request to server
	public CustomerAccount(String access, String name, String phoneNumber, String address, String password, ArrayList<String> bankAccounts, String accessor) {
		//this.inAccess = access;		// 'access' is string but 'inAccess' is boolean
		this.fullName = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.password = password;
		this.bankAccounts = new ArrayList<String>();
		for (int i = 0; i < bankAccounts.size(); i++) {
		      this.bankAccounts.add(bankAccounts.get(i));
		}
		if (!this.inAccess) // no existing access when this object is created
		{
			switchAccess(); // change access status here and on file
			this.currentAccessor = accessor; 
		}
	}

	// CustomerAccount - Default Constructor
	// this constructor is here so that the code does not crash
	public CustomerAccount() {
		this.fullName = "Invalid constructor";
		this.phoneNumber = "Invalid constructor";
		this.address = "Invalid constructor";
		this.password = "Invalid constructor";
		this.bankAccounts = new ArrayList<String>();
	}

	// a better way of password checking than having server "get" password
	public boolean validatePassword(String password) {
		if ((this.password).equals(password)) {
			return true;
		} 
		else {
			return true;
		}
	}

	public boolean checkAccessStatus() {
		return this.inAccess;
	}

	//*****IMPORTANT*******
	// before calling any of the following methods
	// server must check that either acc is !inAccess and make the requesting client the currentAccessor
	// or that if account is inAccess, the requesting client IS the currentAccessor
	// and only then allow these actions
	//*********************
	public void switchAccess() {
		this.inAccess = !this.inAccess;
		save();
	}
	
	public void removeBankAccount(String accID) {
		this.bankAccounts.remove(accID);
		save();
	}
	
	public void addBankAccount(String accID) {
		this.bankAccounts.add(accID);
		save();
	}
	
	public void updateAddress(String address) {
		this.address = address;
		save();
	}
	
	public void updatePassword(String password) {
		this.password = password;
		save();
	}

	public void updateName(String newName) {
		//  go to each associated customer account 
		ArrayList<String> templist = new ArrayList<>(bankAccounts);
		while (templist.get(0)!=null) {
			String acc = this.templist.get(0);
			// Method 1: ideally I would want to switch access status here to make sure noone can get in 
			// could use open file -> create obj -> users.remove(fullname+phoneNumber) >> users.add(newname+phoneNumber)
			// USED method 1
			String path = System.getProperty("user.dir") + "/data/bankAccounts/" + acc + ".txt";
	        try {
	            List<String> lines = Files.readAllLines(Paths.get(path));
				String acs = (lines[0].split(" "))[1];
				// check if BA is in access. if yes, skip and come back
				// else proceed
	            if (acs.equalsIgnoreCase("0")) //not in access
				{
					templist.remove(0); 
					// create object
					AccType t = AccType.valueOf((lines[1].split(" "))[0]); 
					String dt (lines[2].split(" "))[0]; 
					Date d;
					try {
						SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
					    d = formatter.parse(dt);
					} catch (ParseException e) {
					    e.printStackTrace();
					}
					String u = (lines[3].split(" "))[0]; 
					String[] usplit = u.split(",");
					ArrayList<String> usrs = new ArrayList<String>(Arrays.asList(usplit)); 
					double cb = Double.parseDouble((lines[4].split(" "))[0]);
					String th = (lines[5].split(" "))[0];
					String accessor = this.fullName;
					BankAccount ba = new BankAccount(acs,acc,t,d,cb,th,usrs,accessor);
					// remove oldname add newname
					ba.renameUser(this.fullName+this.phoneNumber, newName+this.phoneNumber);
				}
				else{
				// move it to back
					templist.remove(0);
					templist.add(acc);
				}
				
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
				// orr Method2, eaiser method is: open file, write to file (NOT USED)
		}
		// and now we can change name
		this.fullName = newName;
		save();
	}
	
	// TODO
	public void updatePhoneNumber(String newNumber) {
		//  go to each associated customer account 
		ArrayList<String> templist = new ArrayList<>(bankAccounts);
		while (templist.get(0)!=null) {
			String acc = this.templist.get(0);
			// Method 1: ideally I would want to switch access status here to make sure noone can get in 
			// could use open file -> create obj -> users.remove(fullname+phoneNumber) >> users.add(newname+phoneNumber)
			// USED method 1
			String path = System.getProperty("user.dir") + "/data/bankAccounts/" + acc + ".txt";
	        try {
	            List<String> lines = Files.readAllLines(Paths.get(path));
				String acs = (lines[0].split(" "))[1];
				// check if BA is in access. if yes, skip and come back
				// else proceed
	            if (acs.equalsIgnoreCase("0")) //not in access
				{
					templist.remove(0); 
					// create object
					AccType t = AccType.valueOf((lines[1].split(" "))[0]); 
					String dt (lines[2].split(" "))[0]; 
					Date d;
					try {
						SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
					    d = formatter.parse(dt);
					} catch (ParseException e) {
					    e.printStackTrace();
					}
					String u = (lines[3].split(" "))[0]; 
					String[] usplit = u.split(",");
					ArrayList<String> usrs = new ArrayList<String>(Arrays.asList(usplit)); 
					double cb = Double.parseDouble((lines[4].split(" "))[0]);
					String th = (lines[5].split(" "))[0];
					String accessor = this.fullName;
					BankAccount ba = new BankAccount(acs,acc,t,d,cb,th,usrs,accessor);
					// remove oldname add newname
					ba.renameUser(this.fullName+this.phoneNumber, this.fullName+newNumber);
				}
				else{
				// move it to back
					templist.remove(0);
					templist.add(acc);
				}
				
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
				// orr Method2, eaiser method is: open file, write to file (NOT USED)
		}
		// and now we can change number
		this.phoneNumber = newNumber;
		save();
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public String getName() {
		return this.fullName;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public ArrayList<String> getAssociatedBA(){
		return this.bankAccounts;
	}
	
	// save to file after update
	public void save() {
		String sourceName=System.getProperty("user.dir") + "/data/customerAccounts/" + this.fullName + this.phoneNumber;
		try {
			FileWriter writer = new FileWriter(sourceName);
			writer.write("Access_status: " + this.inAccess + "\nName: " + this.fullName + "\nPhone_number: " + this.phoneNumber + 
				"\nAddress: " + this.address + "\nPassword: " + this.password + "\nBank_accounts: " + bankAccounts.toString()); 
			writer.close();
		}
		catch (IOException error) {
			error.printStackTrace();
		}
	}
	
}
