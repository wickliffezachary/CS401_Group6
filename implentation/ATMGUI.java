import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ATMGUI extends JFrame implements ATM.ATMListener {
	
	private Panel stackPanel;	//stack of all panels
	private LoginPanel loginPanel;
	private JPanel customerPanel;
	private JPanel BankAccountPanel;
	private ATMPanel currPanel;

	

	//gui must contain an atm object
	private ATM atm;
	
	//when the gui is created it should instantiate an atm
	public ATMGUI() {
		try {
			atm = new ATM("localhost", 1234, this);	//connect to server
	
			//initialize gui elements
			this.setSize(600,600);
			
			stackPanel = new Panel();	
			stackPanel.setLayout(new OverlayLayout(stackPanel));
			
			this.add(stackPanel);
			
			loginPanel = new LoginPanel();
			stackPanel.add(loginPanel);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//helper method
	//takes the panel you want to show, hides the old one, and shows the new one
	private void switchPanel(ATMPanel panel) {
		currPanel.setVisible(false);
		currPanel.clearFields();
		currPanel = panel;
		currPanel.setVisible(true);
	}
	
	
	//starting point for class
	public static void main(String[] args) {
		//swing utilities
		SwingUtilities.invokeLater(() -> new ATMGUI().setVisible(true));
	}
	
	private class ATMPanel extends JPanel{
		//call so ATM GUI doesn't display previous data
		public void clearFields() {
			//Implement in child classes
		}
	}
	
	private class LoginPanel extends ATMPanel{
		private JTextField firstNameField;
		private JTextField lastNameField;
		private JPanel nameBox;
		private JPanel phoneBox;
		private JTextField phoneNumberField;
		private JPanel passwordBox;
		private JTextField passwordField;
		private JPanel fieldBox;
		private JButton loginButton;
		
		public LoginPanel() {
			//TODO: Font size
			//TODO: component spacing
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			firstNameField = new JTextField(16);
			firstNameField.setMaximumSize(firstNameField.getPreferredSize());
			lastNameField = new JTextField(16);
			lastNameField.setMaximumSize(firstNameField.getPreferredSize());
			nameBox = new JPanel();
			nameBox.setLayout(new BoxLayout(nameBox, BoxLayout.X_AXIS));
			nameBox.add(new JLabel("First Name:\t"));
			nameBox.add(firstNameField);
			nameBox.add(new JLabel("Last Name:\t"));
			nameBox.add(lastNameField);
			
			phoneNumberField = new JTextField();
			phoneNumberField.setMaximumSize(firstNameField.getPreferredSize());
			phoneBox = new JPanel();
			phoneBox.setLayout(new BoxLayout(phoneBox, BoxLayout.X_AXIS));
			phoneBox.add(new JLabel("Phone Number:"));
			phoneBox.add(phoneNumberField);
			
			passwordField = new JTextField();
			passwordField.setMaximumSize(firstNameField.getPreferredSize());
			passwordBox = new JPanel();
			passwordBox.setLayout(new BoxLayout(passwordBox, BoxLayout.X_AXIS));
			passwordBox.add(new JLabel("Password:\t"));
			passwordBox.add(passwordField);
			
			fieldBox = new JPanel();
			fieldBox.setLayout(new BoxLayout(fieldBox, BoxLayout.Y_AXIS));
			fieldBox.add(nameBox);
			fieldBox.add(phoneBox);
			fieldBox.add(passwordBox);
			loginButton = new JButton("Login");
			//when login button pressed, send login message w/ credentials from text fields
			loginButton.addActionListener(new ActionListener() {	
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						atm.login(firstNameField.getText(), lastNameField.getText(), 
								phoneNumberField.getText(), passwordField.getText());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			
			//set all data fields to left alignment
			nameBox.setAlignmentX(Component.LEFT_ALIGNMENT);               
			phoneBox.setAlignmentX(Component.LEFT_ALIGNMENT);              
			passwordBox.setAlignmentX(Component.LEFT_ALIGNMENT);
			fieldBox.setAlignmentX(Component.LEFT_ALIGNMENT);
			
			this.add(fieldBox);
			this.add(loginButton);
		}

		@Override
		public void clearFields() {
			firstNameField.setText("");
			lastNameField.setText("");
			phoneNumberField.setText("");
			passwordField.setText("");
		}
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
