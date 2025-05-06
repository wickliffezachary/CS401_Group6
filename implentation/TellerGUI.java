import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



@SuppressWarnings("serial")
public class TellerGUI extends JFrame  implements Teller.TellerListener{

	private final Teller teller;
	private CardLayout cardLayout;
	private JPanel cardPanel;
	private JPanel customerOptionsPanel;
	private JPanel bankAccountsPanel;
	private String currentUsername;
	private List<String> currentAccountList = Collections.emptyList();
	
	private JButton createCallManagerButton() {
	    JButton btn = new JButton("Call Manager");
	    btn.addActionListener(e ->
	        JOptionPane.showMessageDialog(this, "MANAGER IS ON THE WAY")
	    );
	    return btn;
	}
	
	public TellerGUI(Teller teller) {
		
		this.teller = teller;
		
		setTitle("Teller Login Screen");
		setSize(800,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout());

		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		cardPanel.add(setupLoginPanel(), "login");
		cardPanel.add(setupMainPanel(), "main");
		customerOptionsPanel = setupCustomerOptionsPanel();
		cardPanel.add(customerOptionsPanel, "customerOptions");
		
		bankAccountsPanel = new JPanel();
		bankAccountsPanel.setLayout(new BoxLayout(bankAccountsPanel, BoxLayout.Y_AXIS));
		cardPanel.add(new JScrollPane(bankAccountsPanel), "bankAccounts");
		
		getContentPane().add(cardPanel, BorderLayout.CENTER);

		JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    footer.add(createCallManagerButton());
	    getContentPane().add(footer, BorderLayout.SOUTH);

	    cardLayout.show(cardPanel, "login");
    }
	
	private JPanel setupMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		JLabel welcome = new JLabel("Welcome, Teller");
	    JButton selectCustomerBtn = new JButton("Select Customer");
	    JButton createCustomerBtn = new JButton("Create New Customer");
	    JButton viewActivityBtn = new JButton("View Account Activity");
	    JButton logoutBtn = new JButton("Logout");
	    
	    
	    
	    selectCustomerBtn.addActionListener(e -> {
	    	  String uname = JOptionPane.showInputDialog(this, "Enter customer username:");
	    	  if (uname != null && !uname.isBlank()) {
	    	    currentUsername = uname;
	    	    try {
	    	      //trigger ACCESSCAREQGRANTED
	    	      teller.selectCustomer(uname);
	    	    } catch(IOException ex) {
	    	      ex.printStackTrace();
	    	    }
	    	  }
	    	});


	    createCustomerBtn.addActionListener(e -> {
	        String first   = JOptionPane.showInputDialog(this, "First Name:");
	        String last    = JOptionPane.showInputDialog(this, "Last Name:");
	        String phone   = JOptionPane.showInputDialog(this, "Phone No.:");
	        String address = JOptionPane.showInputDialog(this, "Address:");
	        String pswd    = JOptionPane.showInputDialog(this, "Password:");
	        if (first!=null && last!=null && phone!=null 
	         && address!=null && pswd!=null) {
	            try {
	                teller.createNewCustomer(first, last, phone, address, pswd);
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        }
	    });

	    viewActivityBtn.addActionListener(e -> {
	        if (currentUsername == null || currentUsername.isBlank()) {
	            JOptionPane.showMessageDialog(this, "Select a customer first.");
	            return;
	        }
	        // ensure we have up-to-date account list
	        try {
	            teller.selectCustomer(currentUsername);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            return;
	        }
	        if (currentAccountList.isEmpty()) {
	            JOptionPane.showMessageDialog(this, "No accounts for " + currentUsername);
	            return;
	        }
	        String[] acctOptions = currentAccountList.toArray(new String[0]);
	        String baId = (String) JOptionPane.showInputDialog(
	            this,
	            "Select account to view activity:",
	            "View Account Activity",
	            JOptionPane.PLAIN_MESSAGE,
	            null,
	            acctOptions,
	            acctOptions[0]
	        );
	        if (baId == null) return;
	        try {
	            teller.selectAccount(baId);
	            BankAccount ba = teller.getCurrentBankAccount();
	            String msg = "Balance: " + ba.getBalance() + "\n\nHistory:\n" + ba.getHistory();
	            JOptionPane.showMessageDialog(this, msg, "Activity for " + baId, JOptionPane.INFORMATION_MESSAGE);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    });


	    logoutBtn.addActionListener(e -> {
	        try {
	            teller.logout();
	            cardLayout.show(cardPanel, "login");
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    });
	    
	    mainPanel.add(welcome);
	    mainPanel.add(selectCustomerBtn);
	    mainPanel.add(createCustomerBtn);
	    mainPanel.add(viewActivityBtn);
	    mainPanel.add(logoutBtn);
		
		return mainPanel;
	}
	


    private JPanel setupLoginPanel() {
        JPanel loginPanel = new JPanel();
        JTextField userField = new JTextField(20);
        JPasswordField passField = new JPasswordField(20);
        JButton loginBtn = new JButton("Login");

        loginBtn.addActionListener(e -> {
            try {
                String uname = userField.getText();
                String pswd = new String(passField.getPassword());
                teller.login(uname, pswd);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(userField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passField);
        loginPanel.add(loginBtn);
        return loginPanel;
    }

    public void receivedMessage(Message msg) {
    	switch (msg.getType()) {
        case LOGINOK:
            SwingUtilities.invokeLater(() ->
                cardLayout.show(cardPanel, "main")
            );
            break;
       
        case ACCESSCAREQGRANTED:
            
            String raw = msg.getData();
            if (raw.toLowerCase().startsWith("bank_accounts:")) {
                raw = raw.substring("bank_accounts:".length()).trim();
            }
            List<String> list = raw.isBlank()
                ? Collections.emptyList()
                : Arrays.stream(raw.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList();
            currentAccountList = list;
            SwingUtilities.invokeLater(() ->
                cardLayout.show(cardPanel, "customerOptions")
            );
            break;
            
        case CREATEBACCDONE:
            // refresh list
            SwingUtilities.invokeLater(() -> {
                try { teller.selectCustomer(currentUsername); }
                catch(IOException ex){ ex.printStackTrace(); }
            });
            break;

       
    }


    }

    
    private JPanel setupCustomerOptionsPanel() {
    	  JPanel p = new JPanel();
    	  p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

    	  JButton showBA   = new JButton("Show Bank Accounts");
    	  JButton createBA = new JButton("Create New Bank Account");
    	  JButton updateUI = new JButton("Update User Information");
    	  JButton back     = new JButton("Back");
    	  
    	  
    	  showBA.addActionListener(e -> {
    		    updateBankAccountsPanel(currentAccountList);
    		    cardLayout.show(cardPanel, "bankAccounts");
    		});

    	  
    	  createBA.addActionListener(e -> {
    		    String[] options = { "SAVINGS", "CHECKING" };
    		    int choice = JOptionPane.showOptionDialog(
    		        this,
    		        "Select account type:",
    		        "New Bank Account",
    		        JOptionPane.DEFAULT_OPTION,
    		        JOptionPane.PLAIN_MESSAGE,
    		        null,
    		        options,
    		        options[0]
    		    );
    		    if (choice >= 0) {
    		        try {
    		            teller.createNewBankAccount(options[choice]);
    		        } catch (IOException ex) {
    		            ex.printStackTrace();
    		        }
    		    }
    		});



    	  updateUI.addActionListener(e -> {
    		    String[] options = { "Address", "Password" };
    		    int choice = JOptionPane.showOptionDialog(
    		        this,
    		        "Select field to update:",
    		        "Update User Information",
    		        JOptionPane.DEFAULT_OPTION,
    		        JOptionPane.PLAIN_MESSAGE,
    		        null,
    		        options,
    		        options[0]
    		    );
    		    if (choice < 0) return;
    		    String val = JOptionPane.showInputDialog(this, "New " + options[choice] + ":");
    		    if (val == null || val.isBlank()) return;
    		    try {
    		        teller.updateCustomerInfo(options[choice].toLowerCase(), val.trim());
    		    } catch (IOException ex) {
    		        ex.printStackTrace();
    		    }
    		});



    	  back.addActionListener(e -> {
    		    try {
    		        teller.exitCustomer();               // tell server we’re leaving that customer
    		    } catch (IOException ex) {
    		        ex.printStackTrace();
    		    }
    		    currentUsername = null;                  // clear local state
    		    currentAccountList = Collections.emptyList();
    		    cardLayout.show(cardPanel, "main");
    		});


    	  p.add(showBA);
    	  p.add(createBA);
    	  p.add(updateUI);
    	  p.add(back);
    	  
    	  return p;
    	}
    
    private void updateBankAccountsPanel(List<String> accts) {
    	  bankAccountsPanel.removeAll();
    	  
    	  JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    	  JButton backBtn = new JButton("Back");
    	  backBtn.addActionListener(e -> {
    		    try { teller.exitCustomer(); } catch(IOException ex){ ex.printStackTrace(); }
    		    currentUsername     = null;
    		    currentAccountList  = Collections.emptyList();
    		    cardLayout.show(cardPanel, "main");
    		});
    		nav.add(backBtn);


    	  bankAccountsPanel.add(nav);
    	    
    	    
    	  for (String a : accts) {
    	    JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	    row.add(new JLabel(a));
    	    JButton btn = new JButton("Select");
    	    
    	    btn.addActionListener(e -> {
    	        try {
    	            teller.selectAccount(a);
    	            BankAccount ba = teller.getCurrentBankAccount();

    	            String[] options = {
    	                "View Balance",
    	                "Withdraw",
    	                "Deposit",
    	                "Transaction History"
    	            };
    	            int choice = JOptionPane.showOptionDialog(
    	                this,
    	                "Choose action for account “" + a + "”:",
    	                "Account Actions",
    	                JOptionPane.DEFAULT_OPTION,
    	                JOptionPane.PLAIN_MESSAGE,
    	                null,
    	                options,
    	                options[0]
    	            );
    	            if (choice < 0) return;

    	            switch (choice) {
    	                case 0: // View Balance
    	                    JOptionPane.showMessageDialog(
    	                        this,
    	                        "Balance: " + ba.getBalance()
    	                    );
    	                    break;
    	                case 1: // Withdraw
    	                    String w = JOptionPane.showInputDialog(this, "Amount to withdraw:");
    	                    if (w != null) {
    	                        double amt = Double.parseDouble(w);
    	                        ba.withdraw(amt);
    	                        ba.save();
    	                        JOptionPane.showMessageDialog(
    	                            this,
    	                            "New Balance: " + ba.getBalance()
    	                        );
    	                    }
    	                    break;
    	                case 2: // Deposit
    	                    String d = JOptionPane.showInputDialog(this, "Amount to deposit:");
    	                    if (d != null) {
    	                        double amt2 = Double.parseDouble(d);
    	                        ba.deposit(amt2);
    	                        ba.save();
    	                        JOptionPane.showMessageDialog(
    	                            this,
    	                            "New Balance: " + ba.getBalance()
    	                        );
    	                    }
    	                    break;
    	                case 3: // Transaction History
    	                    JOptionPane.showMessageDialog(
    	                        this,
    	                        "History:\n" + ba.getHistory()
    	                    );
    	                    break;
    	            }
    	        } catch (Exception ex) {
    	            ex.printStackTrace();
    	        }
    	    });
    	   

    	    row.add(btn);
    	    bankAccountsPanel.add(row);
    	  }
    	  bankAccountsPanel.revalidate();
    	  bankAccountsPanel.repaint();
    	}
    
    

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
		    try {
		        TellerGUI[] guiRef = new TellerGUI[1];  // to hold reference from lambda, without this, the gui doesn't exist yet and we run into bugs.
		        Teller teller = new Teller("localhost", 1234, msg -> {
		            guiRef[0].receivedMessage(msg);  // forward message to GUI
		        });
		        TellerGUI gui = new TellerGUI(teller);
		        guiRef[0] = gui; // now we have an actual gui
		        gui.setVisible(true);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		});

	}
}