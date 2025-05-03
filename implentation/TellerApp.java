
public class TellerApp {
<<<<<<< Updated upstream

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
=======
  public static void main(String[] args) throws Exception {
    String[][] creds = {
      {"Nkc2004","cs411"},
      {"OtherUser","otherPass"},
      {"ThirdUser","thirdPass"}
    };
    for (String[] cred : creds) {
      new Thread(() -> {
        try {
          Teller t = new Teller("127.0.0.1",1234,msg->{});  
          t.testLogin(cred[0],cred[1]);  
          
        } catch (Exception e) { e.printStackTrace(); }
      }).start();
    }
  }
}
>>>>>>> Stashed changes
