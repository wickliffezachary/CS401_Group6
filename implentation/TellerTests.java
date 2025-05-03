import java.io.IOException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TellerTests {
    
	// Fields:
	private final String host = "localhost";
	private final int port = 1234;
	TestListener listener = new TestListener();
	
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
	
}