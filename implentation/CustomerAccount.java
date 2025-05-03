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
		this.associated_ba=new ArrayList<String>();
		for (int i = 0; i < assoc_ba.size(); i++) {
		      this.associated_ba.add(assoc_ba.get(i));
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
		// TODO
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
		this.associated_ba.remove(accID);
		save();
	}
	
	public void addBankAccount(String accID) {
		this.associated_ba.add(accID);
		save();
	}
	
	public void updateAddress(String address) {
		this.address=address;
		save();
	}
	
	public void updatePassword(String password) {
		this.password=password;
		save();
	}

	//todo
	public void updateName(String newName) {
		for (int i = 0; i < assoc_ba.size(); i++) {
		      String accc= this.associated_ba.get(i);
			//go to each acc file and 
			//do users.remove(fullname+phoneNumber)
			//users.add(newname+phoneNumber)
		}
		// and now we can change name
		this.fullname=newName;
		save();
	}
	//todo
	public void updatePhoneNumber(String newNumber) {
		for (int i = 0; i < assoc_ba.size(); i++) {
		      String accc= this.associated_ba.get(i);
			//go to each acc file and 
			//do users.remove(fullname+phoneNumber)
			//users.add(fullname+newNumber)
		}
		// and now we can change number
		this.phoneNumber=newNumber;
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
		return this.associated_ba;
	}
	//todo
	public void save() {
		//save to file
		//just like dvdcollection
	}
}
