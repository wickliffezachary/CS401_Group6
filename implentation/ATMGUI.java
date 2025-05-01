import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class ATMGUI extends JFrame implements ATM.ATMListener {
	
	private Panel stackPanel;	//stack of all panels
	private LoginPanel loginPanel;
	//TODO: PromptLoginPanel
	//TODO: LoginFailedPanel
	
	private CustomerPanel customerPanel;
	private BankAccountPanel bankAccountPanel;
	private ATMPanel currPanel;	//keeps track of currently visible panel
	

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
			customerPanel = new CustomerPanel();
			customerPanel.setVisible(false);
			bankAccountPanel = new BankAccountPanel();
			bankAccountPanel.setVisible(false);
			stackPanel.add(loginPanel);
			stackPanel.add(customerPanel);
			stackPanel.add(bankAccountPanel);
			
			currPanel = loginPanel;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//helper method
	//takes the panel you want to show, hides the old one, and shows the new one
	private void switchPanel(ATMPanel panel) {
		currPanel.setVisible(false);
		currPanel = panel;
		currPanel.setVisible(true);
	}
	
	//starting point for class
	public static void main(String[] args) {
		//swing utilities
		SwingUtilities.invokeLater(() -> new ATMGUI().setVisible(true));
	}
	
	private abstract class ATMPanel extends JPanel{
		//call so ATM GUI doesn't display previous users data
		public abstract void clearFields();
	}
	
	private class BankAccountPanel extends ATMPanel{
		//organization panels
		private JPanel borderPanel;	
		private JPanel leftPanel;
		private JPanel rightPanel;
		
		private JLabel accountName;
		
		//buttons
		private JButton viewCurrentBalanceButton;
		private JButton viewTransactionHistoryButton;
		private JButton depositButton;
		private JButton withdrawButton;
		private JButton exitButton;
		
		public BankAccountPanel() {
			accountName = new JLabel("");
			
			borderPanel = new JPanel();
			borderPanel.setLayout(new BorderLayout());
			
			leftPanel = new JPanel();
			leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
			rightPanel = new JPanel();
			rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
			borderPanel.add(leftPanel, BorderLayout.WEST);
			borderPanel.add(rightPanel, BorderLayout.EAST);
			
			viewCurrentBalanceButton = new JButton("View Current Balance");
			viewTransactionHistoryButton = new JButton("Transaction History");
			depositButton = new JButton("Deposit");
			withdrawButton = new JButton("Withdraw");
			
			leftPanel.add(viewCurrentBalanceButton);
			leftPanel.add(viewTransactionHistoryButton);
			rightPanel.add(depositButton);
			rightPanel.add(withdrawButton);
			
			viewCurrentBalanceButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
				}});
			viewTransactionHistoryButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
				}});
			depositButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
						
				}});
			withdrawButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {	
				}});
			
			exitButton = new JButton("Exit");
			exitButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {	
				}});
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(accountName);
			this.add(borderPanel);
			this.add(exitButton);
			
		}
		
		public void setContents() {
			
		}
		
		@Override
		public void clearFields() {
			
		}
	}
	
	private class CustomerPanel extends ATMPanel{
		private JPanel mainPanel;
		private JScrollPane scrollPane;	//for if user has many associated BA's
		private JList<String> bankAccountList;	//list view of customers bank accounts; send ACCESSBAREQ when element selected
		private DefaultListModel<String> bankAccountModel;	//list of customers bank accounts
		private JButton logoutButton;
		
		public CustomerPanel() {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			
			
			mainPanel.add(new JLabel("Accounts"));
			bankAccountModel = new DefaultListModel<>();
			bankAccountList = new JList<>(bankAccountModel);
			bankAccountList.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					try {
						atm.selectAccount(bankAccountList.getSelectedValue());
					} catch (IOException e1) {e1.printStackTrace();}
				}
			});
			logoutButton = new JButton("Logout");
			logoutButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						atm.logout();
					} catch (IOException e1) {e1.printStackTrace();}
				}
			});
			
			mainPanel.add(bankAccountList);
			mainPanel.add(logoutButton);
			
			scrollPane = new JScrollPane(mainPanel);
			this.add(scrollPane);
		}
		
		public void setContents(String[] data) {
			clearFields();
			//data should be list of account numbers
			for(int i = 0; i < data.length; ++i) {
				bankAccountModel.add(i, data[i]);
			}
		}
		
		@Override
		public void clearFields() {
			bankAccountModel.clear();
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
			firstNameField.setMaximumSize(firstNameField.getPreferredSize());	//adjusts height of textfield
			lastNameField = new JTextField(16);
			lastNameField.setMaximumSize(firstNameField.getPreferredSize());
			nameBox = new JPanel();
			nameBox.setLayout(new BoxLayout(nameBox, BoxLayout.X_AXIS));
			nameBox.add(new JLabel("First Name:"));
			nameBox.add(firstNameField);
			nameBox.add(new JLabel("Last Name:"));
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
			passwordBox.add(new JLabel("Password:"));
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
			//nameBox.setAlignmentX(Component.LEFT_ALIGNMENT);               
			//phoneBox.setAlignmentX(Component.LEFT_ALIGNMENT);              
			//passwordBox.setAlignmentX(Component.LEFT_ALIGNMENT);
			//fieldBox.setAlignmentX(Component.LEFT_ALIGNMENT);
			
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
		
		switch(msg.getType()) {
		case LOGOUTOK: //call clearFields() on all panels
			loginPanel.clearFields();
			customerPanel.clearFields();
			bankAccountPanel.clearFields();
			switchPanel(loginPanel);
		case ACCESSCAREQGRANTED: 
			String search = "BankAccounts:";
			int pos = msg.getData().indexOf(search);
			if(pos > -1) {
				pos += search.length();
				String[] accounts = msg.getData().substring(pos).split(",");
				customerPanel.setContents(accounts);
			}else {
				customerPanel.clearFields();
			}
			switchPanel(customerPanel); break;
		case ACCESSBAREQGRANTED:
			//TODO: Handle how data should be distributed to GUI components
			switchPanel(bankAccountPanel);
			break;
		
		}
	}

}
