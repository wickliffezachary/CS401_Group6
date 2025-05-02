import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ATMGUI extends JFrame implements ATM.ATMListener {
	
	// TODO - add GUI functions from JFrame

	// Fields:
	private ATM atm;		// the GUI must contain an ATM object
	
	// ATMGUI - Constructor
	// when the GUI is initialized, it should instantiate an instance of ATM
	public ATMGUI() {
		try {
			atm = new ATM("xxxx", 1234, this);
		}
		catch (IOException error) {
			error.printStackTrace();
		}
	}
	
	// 'main' method
	public static void main(String[] args) {
		// TODO - auto-generated method stub
		
		// swing utilities
		SwingUtilities.invokeLater(() -> new ATMGUI().setVisible(true));
	}

	@Override
	// this method is where we receive messages from ATM from server
	public void receivedMessage(Message msg) {
		// TODO - update the GUI based on received info
		
		// example
		if(msg.getType() == Message.Type.ACCESSBAREQ) {
			
		}
		
	}

}
