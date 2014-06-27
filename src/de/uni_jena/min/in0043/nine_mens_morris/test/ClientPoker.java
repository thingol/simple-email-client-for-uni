package de.uni_jena.min.in0043.nine_mens_morris.test;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;

import de.uni_jena.min.in0043.nine_mens_morris.net.Client;

public class ClientPoker {
	
	static Frame testFrame;
    static JButton poker;
    static Client client;

    private static void setUp() {
    	client = new Client("gw.kjerkreit.org");
    	testFrame = new Frame("ToyClient for nine men's morris");
        poker = new JButton("Poke client");
        	        
        poker.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		client.moveStone(-1, -2);
        	}});
        
        
        testFrame.add(poker);
        testFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
        testFrame.pack();
        testFrame.setVisible(true);
        
        client.start();
        
    }

    public static void main(String args[]) {   
    	setUp();
    }   
}
