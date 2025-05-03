public class TellerApp {

	public static void main(String[] args) throws Exception {
		String[][] creds = {
				{"Nkc2004", "cs411"},
				{"OtherUser", "otherPass"},
				{"ThirdUser", "thirdPass"}
		};
		for (String[] cred : creds) {
			new Thread(() -> {
				try {
					Teller t = new Teller("127.0.0.1", 1234, msg -> {});  
					t.testLogin(cred[0], cred[1]);  

				} 
				catch (Exception error) {
					error.printStackTrace(); 
				}
			}).start();
		}
	}

}