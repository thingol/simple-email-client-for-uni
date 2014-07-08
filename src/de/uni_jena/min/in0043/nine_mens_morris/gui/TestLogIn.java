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
	private int playerCount;
	
	public List<String> names;
	
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
		names = new ArrayList<String>();
		client = new LogInClient("gw.kjerkreit.org");
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
			
			for(int i = 0; i < 20; i++) {
				names.add("Hans" + i);
			}
			
			playerCount = names.size();
			JButton x;
			for(int i = 0; i < playerCount; i++) {
				screen.setLayout(new GridLayout(playerCount, 0));
				JButton d = new JButton(names.get(i));
				Players.add(d);
				x = Players.get(i);
				screen.add(x);
				Players.get(i).addActionListener(this);
			}
			duel(5); //TODO delete
			screen.setVisible(false);
			screen.setVisible(true);
		}
		
	}
	
	public void duel(int id) {
		Object[] opt = {"Yes",
				"No"};
		int n2 = JOptionPane.showOptionDialog(this,
				names.get(id) + " challenges you to a fight?",
				"Duel Dialog",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				opt,
				opt[1]);
		if(n2 == 0) log.trace("Accepted the duel for life and death");//TODO Send yes to server
		else if (n2 == 1) log.trace("Declined the duel"); //TODO Send no to server 
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
				 boolean register = true;
//				 boolean register = client.register(user, pass);
				 if(register)
					 { pass = null;
					 nextLine = true;
					 this.LoggingIn(); }
				 else log.trace("Username taken");
			 }
			 else {
				 byte login = 0;
//				 byte login = client.loggingIn(user, pass);
				 if(login == 0) { pass = null;
				 			 nextLine = true;
				 			 this.LoggingIn(); }
				 else if(login == -1) log.trace("Wrong user, password combination");
				 else if(login == -2) log.trace("Login failed, general error");
				 else if(login == -3) log.trace("You are already logged in");
				 else if(login == -4) log.trace("Server is full, please try again later!");
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
			int number = -1;
			for(int i = 0; i < playerCount; i++) {
				if(((JButton)source).getText().equals(names.get(i)))
					number = i;
			}
			log.trace(number + " was pressed");
//			client.playWith(number);
			//TODO an den Server senden
		}
	}
}
