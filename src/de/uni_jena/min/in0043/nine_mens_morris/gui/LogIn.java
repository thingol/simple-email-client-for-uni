package de.uni_jena.min.in0043.nine_mens_morris.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.net.LobbyClient;

public class LogIn {

	private static Logger log = LogManager.getLogger();
	private JFrame login;
	private LobbyDisplay lobbyDisplay;
	private static LobbyClient lobbyClient;
	
	
	
	public LogIn()	{
		this(new LobbyClient("localhost"));
	}
	
	public LogIn(LobbyClient lobbyClient)	{
		lobbyDisplay = new LobbyDisplay();
		login = generateLogin();

		LogIn.lobbyClient = lobbyClient;
		login.setVisible(true);
	}
	
    private JFrame generateLogin() {
    	int login_width = 300;
    	int login_height = 300;
    	final JTextField username = new JTextField();
    	final JPasswordField password = new JPasswordField();
    	JButton ok;//, cancel;
    	final JCheckBox newUser;
    	final JFrame frame = new JFrame("LogIn Menu");
    	
        frame.setSize(login_width, login_height);
        frame.setLayout(null);
        frame.setResizable(false);
		
		ok = new JButton("Ok");
		//cancel = new JButton("Cancel");

		newUser = new JCheckBox("Register me!");
		
		username.setBounds(login_width/2 - 100, login_height/2 - 15, 200, 30);
		password.setBounds(login_width/2 - 100, login_height/2 + 15, 200, 30);
		newUser.setBounds(login_width/2 - 100, login_height/2 + 50, 200, 30);
		ok.setBounds(login_width/2 - 100, login_height/2 + 100, 100, 20);
		//cancel.setBounds(login_width/2, login_height/2 + 100, 100, 20);
		
		frame.add(username);
		frame.add(password);
		frame.add(newUser);
		frame.add(ok);
		//frame.add(cancel);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				int n = JOptionPane.showConfirmDialog(
						frame,
						"Would you like to close the application?",
						"A stupid question",
						JOptionPane.YES_NO_OPTION);
				if(n == 0) {
					System.exit(0);
				}
			}
		});
		
		ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String userInfo = username.getText() + "," + new String(password.getPassword());
				boolean regNew = newUser.isSelected();
				
				if(lobbyClient == null) {
					
				}

				log.debug("userInfo: " + userInfo + ", new user: " + regNew);

				int res = lobbyClient.logIn(userInfo, regNew);

				switch(res) {
				case 0:
					log.trace("Login failed, general error");
					break;
				case 1:
					log.debug("Login succeeded");
					moveToLobby(username.getText());
					break;
				case 2:
					log.trace("Wrong user, password combination");
					break;
				case 3:
					log.trace("You are already logged in");
					break;
				case 4:
					log.trace("Server is full, please try again later!");
					break;
				case 5:
					log.error("Username is in use!");
				default:
					log.error("Unknown return value received");
					break;
				}
			}
		});
/*		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				log.debug("user clicked 'cancel'");
				lobbyClient.disconnect();
				System.exit(0);
			}
		});*/

		return frame;
	}

	private void moveToLobby(String userName) {
		log.debug("moving to lobby, disposing of login gui");
		lobbyClient.setUserName(userName);
		login.setVisible(false);
		login.dispose();
		lobbyDisplay.setVisible(true);
		lobbyClient.addDisplay(lobbyDisplay);
		log.debug("staring lobbyClient");
		lobbyClient.start();
	}

	public static void main(String[] args) {
		new LogIn();
	}

}
