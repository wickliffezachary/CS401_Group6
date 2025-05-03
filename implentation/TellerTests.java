import java.io.IOException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.Assume;

public class TellerTests {
    
	// Fields:
	private String host = "localhost";
	private int port = 1234;
	TestListener listener = new TestListener();
	private String username = "Nkc2004";
	private String password = "cs411";
	
	private class TestListener implements Teller.TellerListener {
		@Override
		public void receivedMessage(Message msg) {
			// TODO - auto-generated method stub
		}
	}
	
	@Test
	public void ConstructorTest() {
		Teller teller = null;
		
		try {
			teller = new Teller(host, port, listener);
		} 
		catch (IOException error) {
			error.printStackTrace();
		}
		
		Assertions.assertNotNull(teller);
	}
	
	@Test
	public void loginTest() {
		Teller teller = null;
		
		try {
			teller = new Teller(host, port, listener);
			teller.testLogin(username, password);
			
			Assertions.assertTrue(teller.isLoggedInTeller());
		}
		catch (IOException error) {
			Assume.assumeNoException(error);
		}
	}
	
	@Test
	public void logoutTest() {
		Assertions.fail();
	}
	
	@Test
	public void selectCustomerTest() {
		// TODO - method not yet implemented in Teller.java
		Assertions.fail();
	}
	
	@Test
	public void exitCustomerTest() {
		// TODO - method not yet implemented in Teller.java
		Assertions.fail();
	}
	
	@Test
	public void selectAccountTest() {
		// TODO - method not yet implemented in Teller.java
		Assertions.fail();
	}
	
	@Test
	public void exitAccountTest() {
		// TODO - method not yet implemented in Teller.java
		Assertions.fail();
	}
	
	@Test
	public void withdrawTest() {
		// TODO - method not yet implemented in Teller.java
		Assertions.fail();
	}
	
	@Test
	public void depositTest() {
		// TODO - method not yet implemented in Teller.java
		Assertions.fail();
	}
	
	@Test
	public void viewBalanceTest() {
		// TODO - method not yet implemented in Teller.java
		Assertions.fail();
	}
	
	@Test
	public void viewTransactionHistoryTest() {
		// TODO - method not yet implemented in Teller.java
		Assertions.fail();
	}
	
	@Test
	public void createNewCustomerTest() {
		// TODO - method not yet implemented in Teller.java
		Assertions.fail();
	}
	
	@Test
	public void createNewBankAccountTest() {
		// TODO - method not yet implemented in Teller.java
		Assertions.fail();
	}
	
	@Test
	public void closeBankAccountTest() {
		// TODO - method not yet implemented in Teller.java
		Assertions.fail();
	}
	
	@Test
	public void addUserToBankAccountTest() {
		// TODO - method not yet implemented in Teller.java
		Assertions.fail();
	}
	
	@Test
	public void removeUserFromBankAccountTest() {
		// TODO - method not yet implemented in Teller.java
		Assertions.fail();
	}
	
	@Test
	public void updateCustomerInfoTest() {
		// TODO - method not yet implemented in Teller.java
		Assertions.fail();
	}
	
	@Test
	public void callManagerTest() {
		// TODO - method not yet implemented in Teller.java
		Assertions.fail();
	}
	
}