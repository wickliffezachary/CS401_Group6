import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;





class ATM_Test {

	@Test
	public void testLogin() {
		String firstName = "first";
		String lastName = "last";
		String phoneNumber = "1234567890";
		String password = "password";
		
		ATM atm = new ATM();
		try {
			
			atm.login(firstName, lastName, phoneNumber, password);
			assertTrue(atm.isLoggedInUser());
		} catch (IOException e) {
			assumeNoException(e);
		}
	}
	@Test
	public void testLogout() {
		String firstName = "first";
		String lastName = "last";
		String phoneNumber = "1234567890";
		String password = "password";
		
		ATM atm = new ATM();
		try {
			atm.login(firstName, lastName, phoneNumber, password);
			assertTrue(atm.isLoggedInUser());
			atm.logout();
			assertFalse(atm.isLoggedInUser());
		} catch (IOException e) {
			assumeNoException(e);
		}
	}
	@Test
	public void testCurrReserve() {
		ATM atm = new ATM();
		double amount = 10000.50;
		atm.updateCurrReserve(amount);
		assertEquals(atm.getCurrReserve(), amount);
	}
	
	@Test
	public void testCount() {
		int max = 10;
		ATM atm = new ATM();
		int count = atm.getCount();
		for(int i = 0; i < max; ++i) {
			new ATM();
		}
		assertEquals(atm.getCount(), max + count);
	}
	

	
	@Test
	public void testID() {
		ATM atm = new ATM();
		String expectedID = "ATM" + atm.getCount();
		assertEquals(expectedID, atm.getID());
	}
	//dependent on server
	/*
	 * 	private final String address = "localhost";
	private final int port = 1234;
	
	private String firstName = "first";
	private String lastName = "last";
	private String phoneNumber = "555123456";
	private String password = "password";
	private String account = "account1";
	
	private class TestListener implements ATM.ATMListener{
		@Override
		public void receivedMessage(Message msg) {
			// TODO Auto-generated method stub
			
		}
	}
	TestListener listener = new TestListener();
	private ATM atm;
	
	
	@BeforeEach
	public void init() {
		try {
			atm = new ATM(address, port, listener);
		} catch (IOException e) {
			e.printStackTrace();
			assumeNoException(e);	//crash and burn test if atm cannot be initialized
		}
	}
	@AfterEach
	public void close() {
		try {
			atm.logout();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		atm = null;
	}
	
	@Test
	public void testLogin() {
		//TODO: Consider how to implement test users

		assumeFalse(atm.isLoggedInUser());
		try {
			atm.login(firstName, lastName, phoneNumber, password);
			assertTrue(atm.isLoggedInUser());
		} catch (IOException e) {
			e.printStackTrace();
			assumeNoException(e);	//failed to login
		}
	}
	
	@Test
	void testLogout() {
		testLogin();
		try {
			atm.logout();
			assertFalse(atm.isLoggedInUser());
		} catch (IOException e) {
			e.printStackTrace();
			assumeNoException(e);
		}
	}

	@Test
	public void testCurrReserve() {
		int amount = 1000;
		atm.updateCurrReserve(1000);
		assertEquals(amount, atm.getCurrReserve());
	}
	
	@Test
	public void testNotStartedLoggedIn() {
		assertFalse(atm.isLoggedInUser());
	}
	
	@Test
	public void testSelectAccount() {
		
		testLogin();	//user must be logged in before an account can be selected
		try {
			atm.selectAccount(account);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

*/
}
