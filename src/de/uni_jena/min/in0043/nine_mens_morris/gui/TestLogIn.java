package de.uni_jena.min.in0043.nine_mens_morris.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestLogIn extends Panel implements ActionListener {

	private static final long serialVersionUID = 8838423681956882828L;
	private static Logger log = LogManager.getLogger();
	private Frame screen;
	private int width = 500;
	private int height = 600;
	private String user;
	private char[] pass;
	JTextField username = new JTextField();
	JPasswordField password = new JPasswordField();
	JButton ok, cancel;
	
	public TestLogIn()
	{
		screen = new Frame("LogIn Menu");
		screen.setSize(width, height);
		user = null;
		pass = null;
		ok = new JButton("Ok");
		cancel = new JButton("Cancel");
	}
	
	public void LoggingIn() {
		screen.setLayout(null);
		username.setBounds(width/2 - 100, height/2 - 15, 200, 30);
		password.setBounds(width/2 - 100, height/2 + 15, 200, 30);
		ok.setBounds(width/2 - 100, height/2 + 50, 100, 20);
		cancel.setBounds(width/2, height/2 + 50, 100, 20);
		this.setBounds(0, 0, width, height);
		screen.add(username);
		screen.add(password);
		screen.add(ok);
		screen.add(cancel);
		screen.setForeground(new Color(255, 255, 255));
		screen.setBackground(new Color(0, 0, 0));
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
		
		ok.addActionListener(this);
		cancel.addActionListener(this);
		screen.add(this);
		
	}
	
	public static void main(String[] args)
	{
		TestLogIn G = new TestLogIn();
		G.LoggingIn();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		 user = username.getText();
		 pass = password.getPassword();
		 String g = "Username: " + user + "\nPassword: ";
		 for(int i = 0; i < pass.length; i++) {
			 g += pass[i];
		 }
		 System.out.println(g + "\n");
		 
		 if( ok == e.getSource())
		 {
			 //TODO an server senden
		 }
		 else if( cancel == e.getSource())
		 {
			 System.exit(0);
		 }
		
	}
}
