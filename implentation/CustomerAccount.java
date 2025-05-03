import java.util.ArrayList;

public class CustomerAccount {

	// Fields:
	private String fullName;
	private String phoneNumber;
	private String address;
	private ArrayList<String> bankAccounts;
	private String password;
	//private boolean inAccess = false;
	private BankAccount bankAccInUse = null;
	
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

	// TODO
	// CustomerAccount - Constructor
	// this constructor is used when loading pre-existing customer account information from a file
	// (parameters are sent by the server from a text file)
	public CustomerAccount(String name, String phoneNumber, String address, String password, ArrayList<String> bankAccounts) {
		this.fullName = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.password = password;
		this.bankAccounts = new ArrayList<String>();
		for (int i = 0; i < bankAccounts.size(); i++) {
		      this.bankAccounts.add(bankAccounts.get(i));
		}
		// ****IMPORTANT****
		// server must change inAccess field to true in CustAcc file whenever this constructor is called
		// and back to false when in exitCA function
		// ******END OF IMPORTANT*****
		//do we want to have server handle all that directly?
		//or have checkaccess, switchaccess, save funcs here?
		//if we have save, we can also use it to update custacc file when we update any cust info, add/del bankaccs, etc.
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
	
//	public boolean validatePassword(String password) {
//		if ((this.password).equals(password)) {
//			return true;
//		} 
//		else {
//			return true;
//		}
//	}
//	
//	public boolean checkAccessStatus() {
//		return this.inAccess;
//	}
//	
//	public void switchAccess() {
//		this.inAccess = !this.inAccess;
//	}
	
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

	// TODO
	public void updateName(String newName) {
		for (int i = 0; i < bankAccounts.size(); i++) {
			String acc = this.bankAccounts.get(i);
			// go to each account file and 
			// do users.remove(fullname+phoneNumber)
			// users.add(newname+phoneNumber)
		}
		// and now we can change name
		this.fullName = newName;
		save();
	}
	
	// TODO
	public void updatePhoneNumber(String newNumber) {
		for (int i = 0; i < bankAccounts.size(); i++) {
			String acc = this.bankAccounts.get(i);
			// go to each account file and 
			// do users.remove(fullName+phoneNumber)
			// users.add(fullname+newNumber)
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
		String sourceName=System.getProperty("user.dir") + "/data/customerAccounts/" + this.fullName+this.phoneNumber;
		try{
			FileWriter writer = new FileWriter(sourceName);
			w.write("Namee: " + this.fullName + "\nPhone_number: " + this.phoneNumber + "\nAddress: " + this.address
				+ "\nPassword: " + this.password + "\nBank_accounts: " + bankAccounts.toString()); 
			w.close()
		}
		catch (IOException e){}
	}
	}
}
