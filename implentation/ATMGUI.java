import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class ATMGUI extends JFrame implements ATM.ATMListener {
	
	// Fields:
	private ATM atm;				// the GUI must contain an ATM object
	
	private Panel stackPanel;		// stack of all panels
	
	private LoginPanel loginPanel;
	private PromptLoginPanel promptLoginPanel;
	private LoginFailPanel loginFailPanel;	
	private CustomerPanel customerPanel;
	private BankAccountPanel bankAccountPanel;
	private BankAccountFailPanel bankAccountFailPanel;
	private WithdrawPanel withdrawPanel;
	private DepositPanel depositPanel;
	private CurrentBalancePanel currentBalancePanel;
	private TransactionHistoryPanel transactionHistoryPanel;
	private WithdrawFailPanel withdrawFailPanel;
	private WithdrawGoodPanel withdrawGoodPanel;
	private DepositFailPanel depositFailPanel;
	private DepositGoodPanel depositGoodPanel;
	
	private JPanel currPanel;		// this keeps track of the currently-visible panel
	
	// ATMGUI - Constructor
	// when the GUI is initialized, it should instantiate an instance of ATM
	// and initialize all the GUI elements
	public ATMGUI() {
		try {
			atm = new ATM("localhost", 1234, this, 10000.00);		// connect to the server
	
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO - reset auto-logout timer
				}
			});
			
			// initialize the GUI elements
			this.setSize(600,600);
			
			stackPanel = new Panel();	
			stackPanel.setLayout(new OverlayLayout(stackPanel));
			
			this.add(stackPanel);
			promptLoginPanel = new PromptLoginPanel();
			loginPanel = new LoginPanel();
			loginPanel.setVisible(false);
			loginFailPanel = new LoginFailPanel();
			loginFailPanel.setVisible(false);
			customerPanel = new CustomerPanel();
			customerPanel.setVisible(false);
			bankAccountPanel = new BankAccountPanel();
			bankAccountPanel.setVisible(false);
			bankAccountFailPanel = new BankAccountFailPanel();
			bankAccountFailPanel.setVisible(false);
			withdrawPanel = new WithdrawPanel();
			withdrawPanel.setVisible(false);
			depositPanel = new DepositPanel();
			depositPanel.setVisible(false);
			currentBalancePanel = new CurrentBalancePanel();
			currentBalancePanel.setVisible(false);
			transactionHistoryPanel = new TransactionHistoryPanel();
			transactionHistoryPanel.setVisible(false);
			withdrawFailPanel = new WithdrawFailPanel();
			withdrawFailPanel.setVisible(false);
			withdrawGoodPanel = new WithdrawGoodPanel();
			withdrawGoodPanel.setVisible(false);
			depositFailPanel = new DepositFailPanel();
			depositFailPanel.setVisible(false);
			depositGoodPanel = new DepositGoodPanel();
			depositGoodPanel.setVisible(false);
			
			stackPanel.add(promptLoginPanel);
			stackPanel.add(loginPanel);
			stackPanel.add(loginFailPanel);
			stackPanel.add(customerPanel);
			stackPanel.add(bankAccountPanel);
			stackPanel.add(bankAccountFailPanel);
			stackPanel.add(withdrawPanel);
			stackPanel.add(depositPanel);
			stackPanel.add(currentBalancePanel);
			stackPanel.add(transactionHistoryPanel);
			stackPanel.add(withdrawFailPanel);
			stackPanel.add(withdrawGoodPanel);
			stackPanel.add(depositFailPanel);
			stackPanel.add(depositGoodPanel);
			
			currPanel = promptLoginPanel;
		} 
		catch (IOException error) {
			error.printStackTrace();
		}
	}

	// 'main' method for ATM-GUI class
	public static void main(String[] args) {
		// swing utilities
		SwingUtilities.invokeLater(() -> new ATMGUI().setVisible(true));
	}
	
	// helper method that hides a panel and shows the new panel,
	// which is passed in as a parameter
	private void switchPanel(JPanel panel) {
		currPanel.setVisible(false);
		currPanel = panel;
		currPanel.setVisible(true);
	}
	
	private class DepositGoodPanel extends JPanel {
		private JLabel label;
		private JButton continueButton;
		
		public DepositGoodPanel() {
			label = new JLabel("Deposit Successful");
			continueButton = new JButton("Continue");
			continueButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchPanel(bankAccountPanel);	
				}});
			
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.add(label);
			this.add(continueButton);
		}
	}
	
	private class WithdrawGoodPanel extends JPanel {
		private JLabel label;
		private JButton continueButton;
		
		public WithdrawGoodPanel() {
			label = new JLabel("Withdraw Successful");
			continueButton = new JButton("Continue");
			continueButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchPanel(bankAccountPanel);	
				}});
			
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.add(label);
			this.add(continueButton);
		}
	}
	
	private class BankAccountFailPanel extends JPanel {
		private JLabel failLabel;
		private JButton backButton;
		
		public BankAccountFailPanel(){
			failLabel = new JLabel("Bank Account AccessFailed");
			backButton = new JButton("Back");
			
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchPanel(customerPanel);
				}});

			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.add(failLabel);
			this.add(backButton);
		}
	}
	
	private class PromptLoginPanel extends JPanel {
		private JLabel promptLabel;
		
		public PromptLoginPanel() {
			promptLabel = new JLabel("Click Me");
			
			// when the panel is clicked on, go to the login view
			this.add(promptLabel);
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					loginPanel.clearFields();
					switchPanel(loginPanel);
				}
			});
		}
	}
	
	private class LoginFailPanel extends JPanel {
		private JLabel failLabel;
		private JPanel buttonBox;
		private JButton backButton;	
		private JButton retryButton;
		
		public LoginFailPanel(){
			failLabel = new JLabel("Login Failed");
			buttonBox = new JPanel();
			buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.X_AXIS));
			backButton = new JButton("Back");
			retryButton = new JButton("Retry");
			
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchPanel(loginPanel);
				}});
			retryButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					withdrawPanel.clearFields();
					switchPanel(loginPanel);
				}});
			
			buttonBox.add(backButton);
			buttonBox.add(retryButton);
			
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.add(failLabel);
			this.add(buttonBox);
		}
	}
	
	private class DepositFailPanel extends JPanel {
		private JLabel failLabel;
		private JPanel buttonBox;
		private JButton backButton;		//go back to BA panel
		private JButton retryButton;
		
		public DepositFailPanel() {
			failLabel = new JLabel("Deposit Failed");
			buttonBox = new JPanel();
			buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.X_AXIS));
			backButton = new JButton("Back");
			retryButton = new JButton("Retry");
			
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchPanel(bankAccountPanel);
				}});
			retryButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					withdrawPanel.clearFields();
					switchPanel(depositPanel);
				}});
			
			buttonBox.add(backButton);
			buttonBox.add(retryButton);
			
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.add(failLabel);
			this.add(buttonBox);
		}
	}
	
	private class WithdrawFailPanel extends JPanel {
		private JLabel failLabel;
		private JPanel buttonBox;
		private JButton backButton;		// go back to BA panel
		private JButton retryButton;
		
		public WithdrawFailPanel() {
			failLabel = new JLabel("Withdraw Failed");
			buttonBox = new JPanel();
			buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.X_AXIS));
			backButton = new JButton("Back");
			retryButton = new JButton("Retry");
			
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchPanel(bankAccountPanel);
				}});
			retryButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					withdrawPanel.clearFields();
					switchPanel(withdrawPanel);
				}});
			
			buttonBox.add(backButton);
			buttonBox.add(retryButton);
			
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.add(failLabel);
			this.add(buttonBox);
		}
	}

	private class DepositPanel extends JPanel {
		private JTextField amountField;
		private JPanel buttonPanel;
		private JButton sendButton;
		private JButton backButton;
		
		public DepositPanel() {
			amountField = new JTextField(16);
			amountField.setMaximumSize(amountField.getPreferredSize());
			
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
			sendButton = new JButton("Send");
			backButton = new JButton("Back");
						
			sendButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String amount = amountField.getText();
					// performing logic in GUI - seems bad
					// maybe withdraw should accept string input and parse there?
					int dotCount = 0;
					boolean isGood = true;
					for (char token : amount.toCharArray()) {
						if ('.' == token) {
							dotCount += 1;
							if(dotCount > 1) {
								isGood = false;
							}
						}
						if (!Character.isDigit(token)) {
							isGood = false;
						}
					}
					if (isGood) {
						try {
							atm.deposit(Double.valueOf(amount));
						}
						catch (NumberFormatException error) {
							error.printStackTrace();
						} 
						catch (IOException error) {
							error.printStackTrace();
						}
					}
				}});
			
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchPanel(bankAccountPanel);
				}});
			buttonPanel.add(backButton);
			buttonPanel.add(sendButton);
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(new JLabel("Deposit"));
			this.add(amountField);
			
			this.add(buttonPanel);
		}
		public String getInput() {
			return amountField.getText();
		}
		
		public void clearFields() {
			amountField.setText("");
		}
		
	}
	
	private class WithdrawPanel extends JPanel {
		private JTextField amountField;
		private JPanel buttonPanel;
		private JButton sendButton;
		private JButton backButton;
		
		public WithdrawPanel() {
			amountField = new JTextField(16);
			amountField.setMaximumSize(amountField.getPreferredSize());
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
			sendButton = new JButton("Send");
			backButton = new JButton("Back");
						
			sendButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String amount = amountField.getText();
					int dotCount = 0;
					boolean isGood = true;
					for(char token : amount.toCharArray()) {
						if('.' == token) {
							dotCount += 1;
							if(dotCount > 1) {
								isGood = false;
							}
						}
						if(!Character.isDigit(token)) {
							isGood = false;
						}
					}
					if(isGood) {
						try {
							atm.withdraw(Double.valueOf(amount));
						}
						catch (NumberFormatException error) {
							error.printStackTrace();
						}
						catch (IOException error) {
							error.printStackTrace();
						}
					}
				}});
			
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchPanel(bankAccountPanel);
				}});
			buttonPanel.add(backButton);
			buttonPanel.add(sendButton);
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(new JLabel("Withdraw"));
			this.add(amountField);
			
			this.add(buttonPanel);
		}
		public String getInput() {
			return "-"+amountField.getText();
		}
		public void clearFields() {
			amountField.setText("");
		}
	}
	
	private class TransactionHistoryPanel extends JPanel {
		private JScrollPane scrollPane;
		private JPanel mainPanel;			// this is used to hold the elements for the scroll pane
		private JList<String> transactionList;
		private DefaultListModel<String> transactionHistoryModel;
		private JButton backButton;
		
		public TransactionHistoryPanel() {
			mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			transactionHistoryModel = new DefaultListModel<String>();
			transactionList = new JList<String>(transactionHistoryModel);
			backButton = new JButton("Back");
			
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchPanel(bankAccountPanel);
				}});
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			mainPanel.add(transactionList);
			mainPanel.add(backButton);
			scrollPane = new JScrollPane(mainPanel);
			this.add(new JLabel("Transaction History"));
			this.add(scrollPane);
		}
		
		public void setContents(String[] history) {
			clearFields();
			for(int i = 0; i < history.length; ++i) {
				transactionHistoryModel.add(i, history[i]);
			}
		}
		
		public void clearFields() {
			transactionHistoryModel.clear();
		}
	}
	
	private class CurrentBalancePanel extends JPanel {
		private JLabel balance;
		private JButton backButton;
		
		public CurrentBalancePanel() {
			balance = new JLabel("");
			backButton = new JButton("Back");
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchPanel(bankAccountPanel);
				}});
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(new JLabel("Balance"));
			this.add(balance);
			this.add(backButton);
		}
		
		public void setContents(String amount) {
			balance.setText(amount);			
		}
		
		public void updateContents(String amount) {
			balance.setText(String.valueOf(Double.valueOf(balance.getText()) + Double.valueOf(amount)));
		}
		
		public void clearFields() {
			balance.setText("");
		}
	}
	
	private class BankAccountPanel extends JPanel {
		// organization panels
		private JPanel borderPanel;	
		private JPanel leftPanel;
		private JPanel rightPanel;
		
		private JLabel accountName;
		
		// buttons
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
					switchPanel(currentBalancePanel);
				}});
			viewTransactionHistoryButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchPanel(transactionHistoryPanel);
				}});
			depositButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchPanel(depositPanel);
				}});
			withdrawButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchPanel(withdrawPanel);
				}});
			
			exitButton = new JButton("Exit");
			exitButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						atm.exitAccount(accountName.getText());
					} 
					catch (IOException error) {
						error.printStackTrace();
					}
				}});
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(accountName);
			this.add(borderPanel);
			this.add(exitButton);
			
		}
		
		public void setContents(String name) {
			accountName.setText(name);
		}
		
		public void clearFields() {
			accountName.setText("");
		}
	}
	
	private class CustomerPanel extends JPanel {
		private JPanel mainPanel;
		private JScrollPane scrollPane;						// for if a user has many associated BA's
		private JList<String> bankAccountList;				// list view of customer's bank accounts; send ACCESS_BA_REQ when element is selected
		private DefaultListModel<String> bankAccountModel;	// list of customer's bank accounts
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
						if(!e.getValueIsAdjusting()) {
							atm.selectAccount(bankAccountList.getSelectedValue());
						}
					} catch (IOException e1) {e1.printStackTrace();}
				}
			});
			logoutButton = new JButton("Logout");
			logoutButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						atm.logoutCustomer();
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
			// data should be list of account numbers
			for (int i = 0; i < data.length; ++i) {
				bankAccountModel.add(i, data[i]);
			}
		}
		
		public String getSelectedBankAccount() {
			return bankAccountList.getSelectedValue();
		}
		
		public void clearFields() {
			bankAccountModel.clear();
		}
	}
	
	private class LoginPanel extends JPanel {
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
			// TODO - font size
			// TODO - component spacing
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
			
			phoneNumberField = new JTextField(10);
			phoneNumberField.setMaximumSize(firstNameField.getPreferredSize());
			phoneBox = new JPanel();
			phoneBox.setLayout(new BoxLayout(phoneBox, BoxLayout.X_AXIS));
			phoneBox.add(new JLabel("Phone Number:"));
			phoneBox.add(phoneNumberField);
			
			passwordField = new JTextField(16);
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
			// when the login button is pressed, send a login message with the credentials from the text fields
			loginButton.addActionListener(new ActionListener() {	
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						atm.login(firstNameField.getText(), lastNameField.getText(), phoneNumberField.getText(), passwordField.getText());
					} 
					catch (IOException error) {
						error.printStackTrace();
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

		public void clearFields() {
			firstNameField.setText("");
			lastNameField.setText("");
			phoneNumberField.setText("");
			passwordField.setText("");
		}
	}
	
	// find value corresponding to key
	private String searchStringElement(String text, String key) {
		int pos = text.indexOf(key);
		
		if (pos == -1) { 
			return "";
		}
		
		pos += key.length();
		int posEnd = text.indexOf(",", pos);
		
		if (posEnd == -1) {
			return "";
		}
		
		return text.substring(pos, posEnd);
	}
	
	// return array of strings starting from key and ending with '}'
	// expected data format: key:{stuff,stuff,stuff}
	private String[] searchStringArray(String text, String key) {
		int pos = text.indexOf(key);
		
		if(pos == -1) {
			return new String[0];
		}
		
		pos += key.length();
		int posEnd = text.indexOf("}", pos);
		
		if(posEnd == -1) {
			return new String[0];
		}
		
		String substr = text.substring(pos, posEnd);
		return substr.split(",");
	}
	
	@Override
	// this method is where we receive messages from ATM from server
	public void receivedMessage(Message msg) {
		System.out.println("type:"+msg.getType().name()+",msg text:"+msg.getData());
		switch(msg.getType()) {
			case EXITCAREQGRANTED: 
				// clear all user data from panels
				loginPanel.clearFields();
				customerPanel.clearFields();
				bankAccountPanel.clearFields();
				withdrawPanel.clearFields();
				depositPanel.clearFields();
				currentBalancePanel.clearFields();
				transactionHistoryPanel.clearFields();
				switchPanel(promptLoginPanel);
				break;
			case LOGINDENIED: switchPanel(loginFailPanel); break;
			case ACCESSCAREQGRANTED:
				String[] caData = msg.getData().split("\n");
				if(caData.length == 4) {
					String[] accounts = caData[3].split(",");
					customerPanel.setContents(accounts);
				}else {
					customerPanel.clearFields();
				}
				switchPanel(customerPanel); 
				break;
			case ACCESSBAREQGRANTED:
				String[] baData = msg.getData().split("\n");
				bankAccountPanel.setContents(customerPanel.getSelectedBankAccount());
				if(baData.length >= 4) {
					currentBalancePanel.setContents((baData[4].split(" ")[1]));
					if(baData.length >= 5) {
						String[] temp = baData[5].split(" ");
						if(temp.length >= 2) {
							String[] hist = temp[1].split(",");
							transactionHistoryPanel.setContents(hist);
						}
					}				
				}	
				switchPanel(bankAccountPanel);
				break;
			case DEPOSITREQACCEPTED:
				currentBalancePanel.updateContents(msg.getData());
				depositPanel.clearFields();
				switchPanel(depositGoodPanel);
				break;
			case WITHDRAWREQACCEPTED: 
				currentBalancePanel.updateContents("-"+msg.getData());
				withdrawPanel.clearFields();
				switchPanel(withdrawGoodPanel);
				break;
			case EXITBAREQGRANTED:
				bankAccountPanel.clearFields();
				switchPanel(customerPanel);
				break;
			case ACCESSCAREQDENIED:
				switchPanel(loginFailPanel);
				break;
			case ACCESSBAREQDENIED:
				switchPanel(bankAccountFailPanel);
				break;
			}
	}

}
