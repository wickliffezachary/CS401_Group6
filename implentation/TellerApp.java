
public class TellerApp {

	public static void main(String[] args) {
		try {
			System.out.println("Hii");
			
			Teller teller = new Teller("127.0.0.1", 1234, msg -> {});
			
			 teller.testlogin("Nkc2004", "cs411");
			 teller.testlogout();
			 Thread.sleep(500);
		}
		
		catch (Exception e) {
	        e.printStackTrace();
	    }

	}

}
