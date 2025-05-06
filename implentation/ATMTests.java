import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ATMTests {
	@Test
	public void testConstructorReserve() {
		double amount = 10000.50;
		ATM atm = new ATM(amount);
		
		assertEquals(atm.getCurrReserve(), amount);
	}
	
	@Test
	public void testUpdateCurrReserve() {
		ATM atm = new ATM(0);
		double amount = 10000.50;
		assertNotEquals(atm.getCurrReserve(), amount);
		atm.updateCurrReserve(amount);
		assertEquals(atm.getCurrReserve(), amount);
	}
	
	@Test
	public void testCount() {
		int max = 10;
		ATM atm = new ATM(0);
		int start = atm.getCount();
		for(int i = 0; i < max; ++i) {
			new ATM(0);
		}
		assertEquals(atm.getCount(), max + start);
	}
	
	@Test
	public void testID() {
		ATM atm = new ATM(0);
		String expectedID = "ATM" + atm.getCount();
		assertEquals(expectedID, atm.getID());
	}
}
