import java.util.ArrayList;

public class CustomerAccount {

	private String fullname;
	private String ph_num;
	private String addr;
	private ArrayList<String> associated_ba;
	private String pswd;
	//private Boolean in_access=false;
	private BankAccount ba_inuse_rn=null;
	
	// constructor for use when creating a new customer acc for first time
	// info sent from GUI
	public CustomerAccount(String fn, String pn, String a, String pswd){
		this.fullname=fn;
		this.ph_num=pn;
		this.addr=a;
		this.pswd=pswd;
		this.associated_ba = new ArrayList<String>();
	}

	// todo
	// constructor for use when opening/loading preexisting customer acc 
	// info sent by server from fn+pn.txt file
	//ORRRRRRRRRR do we want this constr to open the file and load all req data?????????????????????????????
	public CustomerAccount(String fn, String pn, String a, String pswd, ArrayList<String> assoc_ba){
		this.fullname=fn;
		this.ph_num=pn;
		this.addr=a;
		this.pswd=pswd;
		//this.associated_ba = ;
		//****IMPORTANT****
		//server must change in_access field to true in CustAcc file whenever this constr is called
		//and back to false when in exitCA function
		//******END OF IMPORTANT*****
		//dow e want to have server handle all that directlhy
		//or have checkaccess, switchaccess, save funcs here???????????????
		//if we have save, we can also use it to update custacc file when we update any cust info, add/del bankaccs etc
	}

	//no params constructor
	// so code doesn't crash if someone accidentally uses it
	public CustomerAccount(){
		//errrrrrrrrrrrr
	}
	
//	public Boolean validatePswd(String pswrd) {
//		if ((this.pswd).equals(pswrd)) {
//			return true;
//		} 
//		else {
//			return true;
//		}
//	}
//	
//	public Boolean checkAccessStatus() {
//		return this.in_access;
//	}
//	
//	public void switchAccess() {
//		this.in_access=!this.in_access
//	}
	
	public void removeBankAcc(String accID) {
		
	}
	
	public void addbankAcc() {
		
	}
	
	public void updateAddr(String addr) {
		
	}
	
	public void updatePswd(String pswd) {
		
	}
	
	public void updateName(String newname) {
		
	}
	
	public void updatePhoneNum(String newnum) {
		
	}
	
	public String getAddr() {
		return this.addr;
	}
	
	public String getName() {
		return this.fullname;
	}
	
	public String getPhoneNum() {
		return this.ph_num;
	}
	
}
