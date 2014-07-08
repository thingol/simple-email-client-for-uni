package de.uni_jena.min.in0043.nine_mens_morris.test;

import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.core.net.Protocol;

import de.uni_jena.min.in0043.nine_mens_morris.core.Player;
import de.uni_jena.min.in0043.nine_mens_morris.net.ProtocolOperators;

public class NotSoToyClient {
	private static String srv = "localhost";
	private static int port = 6112; 
	
	private static Socket server;
	private static DataInputStream in;
	private static DataOutputStream out;

	static Frame testFrame;
	static JPanel miscPanel,tmp;
    static JLabel retVal, fromSrv, colour_label;
    static JButton[] buttons;
    private static JTextField operand0,operand1;
    
    private static byte[] rcvBuf = new byte[3];
    
    private static Player colour;
    
    private static ToyBoard board;
    
    private static void sendMessage(DataOutputStream out, byte[] msg) {
    	System.out.println("sending message: " + Arrays.toString(msg));
    	System.out.println("out: " + out);
    	try {
			out.write(msg);
		} catch (IOException e) {
			System.err.println("well shit, that didn't work..." + e);
		}
    }
    
    private static void receiveMessage() {
    	System.out.println("receiving message");
    	try {
			in.readFully(rcvBuf);
		} catch (IOException e) {
			System.err.println("well shit, that didn't work..." + e);
		}
    	
    	int op = (int)rcvBuf[0];
    	
    	if(op == ProtocolOperators.MOVE_STONE[0]) {
    		    		
    	
    	}
    	retVal.setText(String.format(Arrays.toString(rcvBuf)));
    	fromSrv.setText("");
    	
    }
    
    private static void readFromServer() {
    	System.out.println("receiving message");
    	try {
			in.readFully(rcvBuf);
		} catch (IOException e) {
			System.err.println("well shit, that didn't work..." + e);
		}
    	fromSrv.setText(String.format(Arrays.toString(rcvBuf)));
    	retVal.setText("");
    	
    }
    
    private static void receiveColour() {
    	System.out.println("receiving message");
    	try {
			in.readFully(rcvBuf);
		} catch (IOException e) {
			System.err.println("well shit, that didn't work..." + e);
		}
    	if(rcvBuf[0] == 18) {
    		colour_label.setText(Player.WHITE.name());
    		colour = Player.WHITE;
    	} else {
    		colour_label.setText(Player.BLACK.name());
    		colour = Player.BLACK;
    	}
    }
    
    private static void setUp() {
        retVal = new JLabel();
        fromSrv = new JLabel();
        colour_label = new JLabel();
        miscPanel = new JPanel();
        tmp = new JPanel();
        
        miscPanel.setLayout(new GridLayout(4,4));
    	
        
    	board = new ToyBoard();
    	
    	testFrame = new Frame("ToyClient for nine men's morris");

        operand0 = new JTextField();
        operand1 = new JTextField();
        buttons = new JButton[]
        	{new JButton("MOVE_STONE"),
        	new JButton("REMOVE_STONE"),
        	new JButton("CONCEDE"),
        	
        	new JButton("BYE"),
        	
        	new JButton("NEW_GAME"),
        	new JButton("NO_MORE"),
        	
        	new JButton("ACK"),
        	
        	new JButton("READ_FROM_SRV")};
        
        buttons[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	byte stone = -1, point = -1;
            	int lenOpnd0 = operand0.getText().length();
            	int lenOpnd1 = operand1.getText().length();
            	
            	if(lenOpnd0 == 1 || lenOpnd0 == 2 || lenOpnd1 == 1 || lenOpnd1 == 2) {
            		stone = Byte.parseByte(operand0.getText());
            		point = Byte.parseByte(operand1.getText());
            		sendMessage(out, new byte[]{ProtocolOperators.MOVE_STONE[0],stone,point}); receiveMessage();
            	}}});
        buttons[1].addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) { 
        		byte stone = -1;
            	int lenOpnd0 = operand0.getText().length();
            	
            	if(lenOpnd0 == 1 || lenOpnd0 == 2) {
            		stone = Byte.parseByte(operand0.getText());

            		sendMessage(out, new byte[]{ProtocolOperators.REMOVE_STONE[0],stone,0}); receiveMessage();}}});
        buttons[2].addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) { sendMessage(out, ProtocolOperators.CONCEDE); receiveMessage();}});
        buttons[3].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { sendMessage(out, ProtocolOperators.BYE); receiveMessage();}});
        buttons[4].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.NEW_GAME);
                receiveMessage();
            }
        });
        buttons[5].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.NO_MORE);
                receiveMessage();
            }
        });

        buttons[6].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.ACK);
            }
        });
        
        buttons[7].addActionListener(new ActionListener() {
          	 
            public void actionPerformed(ActionEvent e)
            {
                readFromServer();
            }
        });
        
        for(JButton b : buttons) {
        	miscPanel.add(b);
        }
        
        miscPanel.add(colour_label);
        miscPanel.add(new JLabel("retVal: "));
        miscPanel.add(retVal);
        miscPanel.add(new JLabel("fromSrv: "));
        miscPanel.add(fromSrv);
        miscPanel.add(new JLabel("operands: "));
        miscPanel.add(operand0);
        miscPanel.add(operand1);
        
        
        tmp.add(miscPanel);
        tmp.add(board);
        testFrame.add(tmp);
        testFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
        testFrame.pack();
        testFrame.setVisible(true);
        
        try {
    		System.out.println("connecting to " + srv + ":" + port);
			server = new Socket(srv, 6112);
			in = new DataInputStream(server.getInputStream());
	    	out = new DataOutputStream(server.getOutputStream());
	    	/* s hello
	    	 * m ack
	    	 * m farge
	    	 * s ack
	    	 * if farge b
	    	 *     m trekk
	    	 * else
	    	 *     s trekk 
	    	 */
	    	
	    	sendMessage(out, ProtocolOperators.HELLO);
	    	receiveMessage();
	    	receiveColour();
	    	sendMessage(out, ProtocolOperators.ACK);
	    	
	    	if(colour == Player.BLACK) {
	    		System.out.println("waiting for white to move");
	    		readFromServer();
	    		System.out.println("sending move to server");
	    		sendMessage(out, new byte[]{ProtocolOperators.MOVE_STONE[0],9,3});
	    		System.out.println("waiting for ACK from server");
	    		receiveMessage();
	    		
	    		System.out.println("waiting for white to move");
	    		readFromServer();
	    		System.out.println("sending move to server");
	    		sendMessage(out, new byte[]{ProtocolOperators.MOVE_STONE[0],10,4});
	    		receiveMessage();
	    		System.out.println("waiting for ACK from server");
	    		
	    		System.out.println("waiting for white to move");
	    		readFromServer();
	    		System.out.println("server says white got a mill, waiting for move");
	    		readFromServer();
	    		System.out.println("waiting for which stone to remove");
	    		readFromServer();
	    		System.out.println("sending move to server");
	    		sendMessage(out, new byte[]{ProtocolOperators.MOVE_STONE[0],11,5});
	    		System.out.println("waiting for ACK from server");
	    		receiveMessage();
	    		
	    	} else {
	    		System.out.println("sending move to server");
	    		sendMessage(out, new byte[]{ProtocolOperators.MOVE_STONE[0],0,0});
	    		System.out.println("waiting for ACK from server");
	    		receiveMessage();
	    		
	    		System.out.println("waiting for black to move");
	    		readFromServer();
	    		System.out.println("sending move to server");
	    		sendMessage(out, new byte[]{ProtocolOperators.MOVE_STONE[0],1,1});
	    		receiveMessage();
	    		System.out.println("waiting for ACK from server");
	    		
	    		
	    		System.out.println("waiting for black to move");
	    		readFromServer();
	    		System.out.println("sending move to server");
	    		sendMessage(out, new byte[]{ProtocolOperators.MOVE_STONE[0],2,2});
	    		System.out.println("waiting for ACK from server");
	    		receiveMessage();
	    		System.out.println("sending *RE*move to server");
	    		sendMessage(out, new byte[]{ProtocolOperators.REMOVE_STONE[0],9,0});
	    		System.out.println("waiting for ACK from server");
	    		receiveMessage();
	    	}
	    	
	    	
		} catch (IOException e) {
			System.err.println("we oops'ed during setup");
			System.exit(-1);
		}
        
    }

    public static void main(String args[]) {   
    	setUp();
    }   
}
