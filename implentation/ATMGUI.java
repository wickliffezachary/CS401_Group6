import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ATMGUI extends JFrame implements ATM.ATMListener {
	
	//TODO: add gui functions from jframe

	//gui must contain an atm object
	private ATM atm;
	
	//when the gui is created it should instantiate an atm
	public ATMGUI() {
		try {
			atm = new ATM("xxxx", 1234, this);
		}
		catch (IOException error) {
			// TODO Auto-generated catch block
			error.printStackTrace();
		}
	}
	
	
	//starting point for class
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//swing utilities
		SwingUtilities.invokeLater(() -> new ATMGUI().setVisible(true));
	}

	@Override
	//this function is where we receive messages from atm from server
	public void receivedMessage(Message msg) {
		// TODO update gui based on received info
		
		//example
		if(msg.getType() == Message.Type.ACCESSBAREQ) {
			
		}
		
	}

}
