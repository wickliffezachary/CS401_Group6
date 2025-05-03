import javax.swing.*;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;



public class TellerGUI extends JFrame  implements Teller.TellerListener{

<<<<<<< Updated upstream
    private Teller teller;
    private CardLayout cardLayout;
    private JPanel cardPanel;
=======
	private Teller teller;
	private CardLayout cardLayout;
	private JPanel cardPanel;
	
	public TellerGUI(Teller teller) {
		
		this.teller = teller;
		
		setTitle("Teller Login Screen");
		setSize(800,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		cardPanel.add(setupLoginPanel(), "login");
		cardPanel.add(setupMainPanel(), "main");
>>>>>>> Stashed changes


    public TellerGUI(){
        setTitle("Teller Login Screen");
        setSize(800,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(setupLoginPanel(), "login");
        cardPanel.add(setupMainPanel(), "main");


<<<<<<< Updated upstream
        add(cardPanel);
        cardLayout.show(cardPanel, "login");
    }
=======
	private JPanel setupMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		JLabel welcome = new JLabel("Welcome, Teller");
	    JButton selectCustomerBtn = new JButton("Select Customer");
	    JButton createCustomerBtn = new JButton("Create New Customer");
	    JButton viewActivityBtn = new JButton("View Account Activity");
	    JButton logoutBtn = new JButton("Logout");
	    
	    selectCustomerBtn.addActionListener(e -> {
	        // placeholder
	        System.out.println("Select Customer clicked");
	    });

	    createCustomerBtn.addActionListener(e -> {
	        // placeholder
	        System.out.println("Create New Customer clicked");
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
>>>>>>> Stashed changes

    private JPanel setupLoginPanel() {
        JPanel loginPanel = new JPanel();
        JTextField userField = new JTextField(20);
        JPasswordField passField = new JPasswordField(20);
        JButton loginBtn = new JButton("Login");

<<<<<<< Updated upstream
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

    private JPanel setupMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.add(new JLabel("Welcome, Teller"));
        // buttons to be implemented here!
        return mainPanel;
    }

    public void receivedMessage(Message msg) {
        if (msg.getType() == Message.Type.LOGINOK) {
            SwingUtilities.invokeLater(() -> cardLayout.show(cardPanel, "main"));
        } else if (msg.getType() == Message.Type.LOGINDENIED) {
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, "Login failed")
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TellerGUI gui = new TellerGUI();
            try {
                gui.teller = new Teller("localhost", 5000, gui);
            } catch (IOException e) {
                e.printStackTrace();
            }
            gui.setVisible(true);
        });
    }
=======
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
		    try {
		        TellerGUI[] guiRef = new TellerGUI[1];  // to hold reference from lambda, without this, the gui doesn't exist yet and we run into bugs.
		        Teller teller = new Teller("localhost", 1234, msg -> {
		            guiRef[0].receivedMessage(msg);  // forward message to GUI
		        });
		        TellerGUI gui = new TellerGUI(teller);
		        guiRef[0] = gui; // now we have an actual gui.
		        gui.setVisible(true);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		});

	}
>>>>>>> Stashed changes


}