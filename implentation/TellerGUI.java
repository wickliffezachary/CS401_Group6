import javax.swing.*;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



@SuppressWarnings("serial")
public class TellerGUI extends JFrame  implements Teller.TellerListener{

	private Teller teller;
	private CardLayout cardLayout;
	private JPanel cardPanel;
	private JPanel customerOptionsPanel;
	private JPanel bankAccountsPanel;
	private String currentUsername;
	private List<String> currentAccountList = Collections.emptyList();
	
	public TellerGUI(Teller teller) {
		
		this.teller = teller;
		
		setTitle("Teller Login Screen");
		setSize(800,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		cardPanel.add(setupLoginPanel(), "login");
		cardPanel.add(setupMainPanel(), "main");
		customerOptionsPanel = setupCustomerOptionsPanel();
		cardPanel.add(customerOptionsPanel, "customerOptions");
		
		bankAccountsPanel = new JPanel();
		bankAccountsPanel.setLayout(new BoxLayout(bankAccountsPanel, BoxLayout.Y_AXIS));
		cardPanel.add(new JScrollPane(bankAccountsPanel), "bankAccounts");

        add(cardPanel);
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
	        // placeholder
	        System.out.println("View Activity clicked");
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
            List<String> list = msg.getData().isBlank()
                ? Collections.emptyList()
                : Arrays.asList(msg.getData().split(","));
            currentAccountList = list;
            SwingUtilities.invokeLater(() ->
                cardLayout.show(cardPanel, "customerOptions")
            );
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
    		    String type = JOptionPane.showInputDialog(this, "Type (SAVINGS/CHECKING):");
    		    if (type != null && !type.isBlank()) {
    		        try {
    		            teller.createNewBankAccount(type.trim());
    		        } catch (IOException ex) {
    		            ex.printStackTrace();
    		        }
    		    }
    		});


    	  updateUI.addActionListener(e -> {
    		    String field = JOptionPane.showInputDialog(this, "Field to update (address/password):");
    		    String val = JOptionPane.showInputDialog(this, "New value:");
    		    if (field != null && val != null) {
    		        try {
    		            teller.updateCustomerInfo(field.trim(), val.trim());
    		        } catch (IOException ex) {
    		            ex.printStackTrace();
    		        }
    		    }
    		});


    	  back.addActionListener(e -> cardLayout.show(cardPanel, "main"));

    	  p.add(showBA);
    	  p.add(createBA);
    	  p.add(updateUI);
    	  p.add(back);
    	  return p;
    	}
    
    private void updateBankAccountsPanel(List<String> accts) {
    	  bankAccountsPanel.removeAll();
    	  for (String a : accts) {
    	    JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	    row.add(new JLabel(a));
    	    JButton btn = new JButton("Select");
    	    btn.addActionListener(e -> {
    	      try {
				teller.selectAccount(a);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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