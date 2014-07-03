package de.uni_jena.min.in0043.nine_mens_morris.gui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogIn extends Panel {

	private static final long serialVersionUID = 8838423681956882828L;
	private static Logger log = LogManager.getLogger();
	private Frame screen;
	private int width = 500;
	private int height = 600;
	private List<String> list;
	JTextField username = new JTextField();
	JPasswordField password = new JPasswordField();
	
	public LogIn()
	{
		screen = new Frame("LogIn Menu");
		screen.setSize(width, height);
	}
	
	public void LoggingIn() {
		String user = "", pw = "";
		//TODO use a function to check whether or not the username is available
		while(user.equals("") || pw.equals("")) {
			log.debug("user: " + user + " pw: " + pw);
		if(user.equals("close")) System.exit(0);
			input();
			user = username.getText();
			pw = new String(password.getPassword());
		}
		
		//TODO ask for users and then put them in a list
		
		screen.add(this);
		screen.add(username);
		screen.setForeground(new Color(100, 100, 100));
		screen.setBackground(new Color(255, 255, 255));
		screen.setVisible(true);
		
		screen.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				int n = JOptionPane.showConfirmDialog(
						screen,
						"Would you like to close the application?",
						"A stupid question",
						JOptionPane.YES_NO_OPTION);
				if(n == 0)
					System.exit(0);
			}
		});
	}
	
	public void input() {
		final JComponent[] inputs = new JComponent[]
		{
				new JLabel("Username (type \"close\" to close application)"),
				username,
				new JLabel("Password"),
				password
		};
		JOptionPane.showMessageDialog(null, inputs, "Log In", JOptionPane.PLAIN_MESSAGE);
		
	}
	
	public static void main(String[] args)
	{
		LogIn G = new LogIn();
		G.LoggingIn();
	}
}
