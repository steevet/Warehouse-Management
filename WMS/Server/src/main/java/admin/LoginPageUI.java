package admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LoginPageUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	JTextField username;
	JPasswordField password;
	private static LoginPageUI instance = null;
	
	public static LoginPageUI getInstance() {
		if (instance == null) {
			instance = new LoginPageUI();
		}
		return instance;
	}
	
	private LoginPageUI() {
		
		super("Login Page UI");
		
		JPanel panel = new JPanel();
		
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setBounds(100, 8, 70, 20);
		panel.add(usernameLabel);		
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(100, 55, 70, 20);
		panel.add(passwordLabel);
		
		username = new JTextField();
		username.setBounds(100, 27, 193, 28);
		panel.add(username);
		
		password = new JPasswordField();
		password.setBounds(100, 75, 193, 28);
		panel.add(password);
		
		JButton loginButton = new JButton("Login");
		loginButton.setBounds(100, 110, 90, 25);
		loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Starter.start(username.getText(), new String (password.getPassword()));	
				setVisible(false);
			}
		});
		
		panel.add(loginButton);
		panel.setLayout(null);
		add(panel);
	}
	

	
	public static void main(String[] args) {
		
		JFrame frame = LoginPageUI.getInstance();
		frame.setSize(900, 600);
		frame.setVisible(true);
	}

}