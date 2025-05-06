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
	public void checkInActiveAccessTest() {
		BankAccount testAccount = new BankAccount(username, checking);
		
		Assertions.assertFalse(testAccount.checkInActiveAccess());
	}
	
	@Test
	public void getBalanceTest() {
		BankAccount testAccount = new BankAccount(username, savings);
		testAccount.updateBalance(420.69);
		
		Assertions.assertEquals(420.69, testAccount.getBalance());
	}
	
	@Test
	public void getTypeTest() {
		BankAccount testAccount = new BankAccount(username, savings);
		
		Assertions.assertEquals(savings, testAccount.getType());
	}
	
	@Test
	public void switchAccessTest() {
		BankAccount testAccount = new BankAccount(username, checking);
		testAccount.switchAccess();
		
		Assertions.assertTrue(testAccount.checkInActiveAccess());
	}
	
	@Test
	public void depositTest() {
		BankAccount testAccount = new BankAccount(username, checking);
		testAccount.deposit(420.69);
		testAccount.deposit(69.42);
		
		Assertions.assertEquals(420.69 + 69.42, testAccount.getBalance());
	}
	
	@Test
	public void withdrawTest() {
		BankAccount testAccount = new BankAccount(username, savings);
		testAccount.updateBalance(1000.00);
		testAccount.withdraw(420.69);
		
		Assertions.assertEquals(1000.00 - 420.69, testAccount.getBalance());
	}
	
	@Test
	public void saveTest() {
		BankAccount testAccount = new BankAccount(username, checking);
		testAccount.save();
		
		File testFile = new File(System.getProperty("user.dir") + "/data/bankAccounts/" + testAccount.getAccountID() + ".txt");
		
		Assertions.assertTrue(testFile.exists());
	}
}