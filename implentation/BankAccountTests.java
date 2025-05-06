import java.io.*;
import java.util.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;



public class BankAccountTests {
	
	// Fields:
	private String username = "TestAccount";
	private BankAccount.AccType checking = BankAccount.AccType.CHECKING;
	private BankAccount.AccType savings = BankAccount.AccType.SAVINGS;
	
	@Test
	public void ConstructorTest() {
		BankAccount testAccount = null;
		testAccount = new BankAccount(username, checking);
		
		Assertions.assertNotNull(testAccount);
		
		BankAccount testAccount2 = null;
		testAccount2 = new BankAccount(username, savings);
		
		Assertions.assertNotNull(testAccount2);
	}
	
	@Test
	public void checkInActiveStatusTest() {
		BankAccount testAccount = new BankAccount(username, checking);
		
		Assertions.assertFalse(testAccount.checkInActiveAccess());
	}
	
	@Test
	public void addUserTest() {
		// TODO - implement this test
		Assertions.fail();
	}
	
	@Test
	public void removeUserTest() {
		// TODO - implement this test
		Assertions.fail();
	}
	
	@Test
	public void renameUserTest() {
		// TODO - implement this test
		Assertions.fail();
	}
	
	@Test
	public void getAccountIDTest() {
		BankAccount testAccount = new BankAccount(username, checking);
		
		Assertions.assertEquals("40", testAccount.getAccountID());
	}
}