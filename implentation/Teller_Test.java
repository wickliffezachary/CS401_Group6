import static org.junit.Assume.assumeNoException;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class Teller_Test {
	@Test
	public void testLoginConstructor() {
		Teller teller = new Teller();
		assertFalse(teller.isLoggedInTeller());
	}
	

	
	@Test
	public void testCount() {
		Teller teller = new Teller();
		int start = teller.getCount();
		int max = 10;
		for(int i = 0; i < max; ++i) {
			new Teller();
		}
		assertEquals(start + max, teller.getCount());
	}
	@Test
	public void testID() {
		Teller teller = new Teller();
		String expectedID = "Teller" + teller.getCount();
		assertEquals(expectedID, teller.getID());
	}
}
