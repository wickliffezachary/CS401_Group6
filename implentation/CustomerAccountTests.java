import java.io.*;
import java.util.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;



public class CustomerAccountTests {
	
	// Fields:
	private String fullName = "TestAccount";
	private String phoneNumber = "5108853000";
	private String address = "25800 Carlos Bee Blvd.";
	private String password = "test";
	private ArrayList<String> bankAccounts = new ArrayList<String>();
	
	@Test
	public void ConstructorTest() {
		CustomerAccount testCustomer = null;
		testCustomer = new CustomerAccount(fullName, phoneNumber, address, password);
			
		Assertions.assertNotNull(testCustomer);
	}
	
	@Test
	public void validatePasswordTest() {
		CustomerAccount testCustomer = new CustomerAccount(fullName, phoneNumber, address, password);
		
		Assertions.assertTrue(testCustomer.validatePassword("test"));
		Assertions.assertFalse(testCustomer.validatePassword("incorrect"));
	}
	
	@Test
	public void checkAccessStatusTest() {
		CustomerAccount testCustomer = new CustomerAccount(fullName, phoneNumber, address, password);
		
		Assertions.assertFalse(testCustomer.checkAccessStatus());
	}
	
	@Test
	public void switchAccessTest() {
		CustomerAccount testCustomer = new CustomerAccount(fullName, phoneNumber, address, password);
		testCustomer.switchAccess();
		
		Assertions.assertTrue(testCustomer.checkAccessStatus());
	}
	
	@Test
	public void removeBankAccountTest() {
		// TODO - implement this test
		Assertions.fail();
	}
	
	@Test
	public void addBankAccountTest() {
		// TODO - implement this test
		Assertions.fail();
	}
	
	@Test
	public void updateAddressTest() {
		CustomerAccount testCustomer = new CustomerAccount(fullName, phoneNumber, address, password);
		testCustomer.updateAddress("69420 Testing Street");
		
		Assertions.assertEquals("69420 Testing Street", testCustomer.getAddress());
	}
	
	@Test
	public void updatePasswordTest() {
		CustomerAccount testCustomer = new CustomerAccount(fullName, phoneNumber, address, password);
		testCustomer.updatePassword("newPassword");
		
		Assertions.assertTrue(testCustomer.validatePassword("newPassword"));
	}
	
	@Test
	public void updateNameTest() {
		// TODO - implementation of updateName in CustomerAccounts.java incomplete
		Assertions.fail();
	}
	
	@Test
	public void updatePhoneNumberTest() {
		// TODO - implementation of updatePhoneNumber in CustomerAccounts.java incomplete
		Assertions.fail();
	}
	
	@Test
	public void getAddressTest() {
		CustomerAccount testCustomer = new CustomerAccount(fullName, phoneNumber, address, password);
		
		Assertions.assertEquals("25800 Carlos Bee Blvd.", testCustomer.getAddress());
	}
	
	@Test
	public void getNameTest() {
		CustomerAccount testCustomer = new CustomerAccount(fullName, phoneNumber, address, password);
		
		Assertions.assertEquals("TestAccount", testCustomer.getName());
	}
	
	@Test
	public void getPhoneNumberTest() {
		CustomerAccount testCustomer = new CustomerAccount(fullName, phoneNumber, address, password);
		
		Assertions.assertEquals("5108853000", testCustomer.getPhoneNumber());
	}
	
	@Test
	public void saveTest() {
		CustomerAccount testCustomer = new CustomerAccount(fullName, phoneNumber, address, password);
		testCustomer.addBankAccount("T01");
		testCustomer.addBankAccount("T02");
		testCustomer.addBankAccount("T03");
		
		testCustomer.save();
		
		File testFile = new File(System.getProperty("user.dir") + "/data/customerAccounts/TestAccount5108853000.txt");
		
		Assertions.assertTrue(testFile.exists());
	}
}