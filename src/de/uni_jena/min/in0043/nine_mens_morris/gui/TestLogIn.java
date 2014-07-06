package de.uni_jena.min.in0043.nine_mens_morris.gui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.net.LogInClient;

public class TestLogIn extends Panel implements ActionListener {

	private static final long serialVersionUID = 8838423681956882828L;
	private static Logger log = LogManager.getLogger();
	private Frame screen;
	private int width = 300;
	private int height = 300;
	private int width2 = 600;
	private int height2 = 600;
	private String user;
	private char[] pass;
	private boolean newU;
	private JTextField username = new JTextField();
	private JPasswordField password = new JPasswordField();
	private JButton ok, cancel;
	private JCheckBox newUser;
	private boolean nextLine;
	private LogInClient client;
	
	private List<JButton> Players;
	private int[] numbers;
	private String[] names;
	
	public TestLogIn()
	{
		screen = new Frame("LogIn Menu");
		screen.setSize(width, height);
		user = null;
		pass = null;
		ok = new JButton("Ok");
		cancel = new JButton("Cancel");
		newUser = new JCheckBox("Register me!");
		newU = false;
		nextLine = false;
		Players = new ArrayList<JButton>();
		client = new LogInClient();
	}
	
	public void LoggingIn() {
		screen.setLayout(null);
		username.setBounds(width/2 - 100, height/2 - 15, 200, 30);
		password.setBounds(width/2 - 100, height/2 + 15, 200, 30);
		newUser.setBounds(width/2 - 100, height/2 + 50, 200, 30);
		ok.setBounds(width/2 - 100, height/2 + 100, 100, 20);
		cancel.setBounds(width/2, height/2 + 100, 100, 20);
		this.setBounds(0, 0, width, height);
		screen.add(username);
		screen.add(password);
		screen.add(newUser);
		screen.add(ok);
		screen.add(cancel);
		screen.setForeground(new Color(0, 0, 0));
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
		
		ok.addActionListener(this);
		cancel.addActionListener(this);
		newUser.addActionListener(this);
		
		if(nextLine) {
			screen.remove(username);
			screen.remove(password);
			screen.remove(newUser);
			screen.remove(ok);
			screen.remove(cancel);
			screen.setSize(width2, height2);
			
			
			numbers = new int[20];
			names = new String[numbers.length];
			JButton x;
			for(int i = 0; i < names.length; i++) {
				screen.setLayout(new GridLayout(names.length, 0));
				names[i] = "" + (i+1);
				JButton d = new JButton(names[i]);
				Players.add(d);
				x = Players.get(i);
				screen.add(x);
				Players.get(i).addActionListener(this);
			}
			screen.setVisible(false);
			screen.setVisible(true);
		}
		
	}
	
	public static void main(String[] args)
	{
		TestLogIn G = new TestLogIn();
		G.LoggingIn();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!nextLine) {
		 user = username.getText();
		 pass = password.getPassword();
		 String g = "\nUsername: " + user + "\nPassword: ";
		 for(int i = 0; i < pass.length; i++) {
			 g += pass[i];
		 }
		 log.debug(g + "\nNewUser: " + newU);
		 
		 if(newUser == e.getSource()){
			 if(newU == false)
			 newU = true;
			 else newU = false;
		 }
		 if( ok == e.getSource())
		 {
			 if(newU == true)
			 {
				 boolean register = client.register(user, pass);
				 if(!register)
					 log.trace("Username taken");
				 else { pass = null;
				 		nextLine = true;
				 		this.LoggingIn(); }
			 }
			 else {
				 boolean login = client.loggingIn(user, pass);
				 if(login) { pass = null;
				 			 nextLine = true;
				 			 this.LoggingIn(); }
				 else log.trace("Wrong user, password combination");
			 }
		 }
		 else if( cancel == e.getSource())
		 {
			 client.disconnect();
			 System.exit(0);
		 }
		}//!newline
		else {
			Object source = e.getSource();
			int number = Integer.parseInt(((JButton)source).getText());
			log.trace(number + " was pressed");
			//TODO an den Server senden
		}
	}
}
