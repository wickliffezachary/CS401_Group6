public class TellerApp {

	public static void main(String[] args) {

		try {
			System.out.println("Hii");

			Teller teller = new Teller("127.0.0.1", 1234, msg -> {});

			teller.testLogin("Nkc2004", "cs411");
			teller.testLogout();
			Thread.sleep(500);
		}

		catch (Exception error) {
			error.printStackTrace();
		}

	}

}
