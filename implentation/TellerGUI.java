import javax.swing.*;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;



@SuppressWarnings("serial")
public class TellerGUI extends JFrame  implements Teller.TellerListener{

	private Teller teller;
	private CardLayout cardLayout;
	private JPanel cardPanel;
	
	public TellerGUI() {
		setTitle("Teller Login Screen");
		setSize(800,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		cardPanel.add(setupLoginPanel(), "login");
		cardPanel.add(setupMainPanel(), "main");


		add(cardPanel);
		cardLayout.show(cardPanel, "login");
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


}