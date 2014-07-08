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

public class ToyClient {
	private static String srv = "localhost";
	private static int port = 6112; 
	
	private static Socket server;
	private static DataInputStream in;
	private static DataOutputStream out;

	static Frame testFrame;
    static JLabel retVal, fromSrv, colour_label;
    static JButton[] buttons;
    private static JTextField operand0,operand1;
    
    private static byte[] rcvBuf = new byte[3];
    
    private static Player colour;
    
    private static JPanel board;
    
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
    	
    	board = new JPanel();
    	board.setLayout(new GridBagLayout());
    	
    	testFrame = new Frame("ToyClient for nine men's morris");

        operand0 = new JTextField();
        operand1 = new JTextField();
        buttons = new JButton[]
        	{new JButton("HELLO"),
        	new JButton("MOVE_STONE"),
        	new JButton("REMOVE_STONE"),
        	new JButton("CONCEDE"),
        	
        	new JButton("BYE"),
        	new JButton("GET_PHASE"),
        	new JButton("GET_ACTIVE_PLAYER"),
        	new JButton("GET_WHITE_ACTIVE"),
        	
        	new JButton("WHITE_IN_PLAY"),
        	new JButton("WHITE_LOST"),
        	new JButton("GET_BLACK_ACTIVATED"),
        	new JButton("BLACK_OF_PLAY"),
        	
        	new JButton("BLACK_LOST"),
        	new JButton("GET_ROUND"),
        	new JButton("YOU_WIN"),
        	new JButton("YOU_LOSE"),
        	
        	new JButton("NEW_GAME"),
        	new JButton("NO_MORE"),
        	new JButton("IS_WHITE"),
        	new JButton("IS_BLACK"),
        	
        	new JButton("ACK"),
        	new JButton("NACK"),
        	new JButton("UNKNOWN_OP"),
        	new JButton("ILLEGAL_OP"),
        	
        	new JButton("GENERAL_ERROR"),
        	new JButton("READ_FROM_SRV")};
        
        buttons[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
            	sendMessage(out, ProtocolOperators.HELLO);
            	receiveMessage();}});
        buttons[0].setEnabled(false);
        buttons[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	byte stone = -1, point = -1;
            	int lenOpnd0 = operand0.getText().length();
            	int lenOpnd1 = operand1.getText().length();
            	
            	if(lenOpnd0 == 1 || lenOpnd0 == 2 || lenOpnd1 == 1 || lenOpnd1 == 2) {
            		stone = Byte.parseByte(operand0.getText());
            		point = Byte.parseByte(operand1.getText());
            		sendMessage(out, new byte[]{ProtocolOperators.MOVE_STONE[0],stone,point}); receiveMessage();
            	}}});
            	
        buttons[2].addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) { 
        		byte stone = -1;
            	int lenOpnd0 = operand0.getText().length();
            	
            	if(lenOpnd0 == 1 || lenOpnd0 == 2) {
            		stone = Byte.parseByte(operand0.getText());

            		sendMessage(out, new byte[]{ProtocolOperators.REMOVE_STONE[0],stone,0}); receiveMessage();}}});
        buttons[3].addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) { sendMessage(out, ProtocolOperators.CONCEDE); receiveMessage();}});
        buttons[4].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { sendMessage(out, ProtocolOperators.BYE); receiveMessage();}});
        buttons[5].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.GET_PHASE);
                receiveMessage();
            }
        });
        buttons[5].setEnabled(false);
        
        buttons[6].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.GET_ACTIVE_PLAYER);
                receiveMessage();
            }
        });
        buttons[6].setEnabled(false);
        
        buttons[7].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.GET_WHITE_ACTIVATED);
                receiveMessage();
            }
        });
        buttons[7].setEnabled(false);
        
        buttons[8].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.WHITE_IN_PLAY);
                receiveMessage();
            }
        });
        buttons[8].setEnabled(false);
        
        buttons[9].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.WHITE_LOST);
                receiveMessage();
            }
        });
        buttons[9].setEnabled(false);
        
        buttons[10].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.GET_BLACK_ACTIVATED);
                receiveMessage();
            }
        });
        buttons[10].setEnabled(false);
        
        buttons[11].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.BLACK_IN_PLAY);
                receiveMessage();
            }
        });
        buttons[11].setEnabled(false);
        
        buttons[12].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.BLACK_LOST);
                receiveMessage();
            }
        });
        buttons[12].setEnabled(false);
        
        buttons[13].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.GET_ROUND);
                receiveMessage();
            }
        });
        buttons[13].setEnabled(false);
        
        buttons[14].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.YOU_WIN);
                receiveMessage();
            }
        });
        buttons[14].setEnabled(false);
        
        buttons[15].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.YOU_LOSE);
                receiveMessage();
            }
        });
        buttons[15].setEnabled(false);
        
        buttons[16].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.NEW_GAME);
                receiveMessage();
            }
        });
        buttons[17].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.NO_MORE);
                receiveMessage();
            }
        });
        buttons[18].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.IS_WHITE);
                receiveMessage();
            }
        });
        buttons[18].setEnabled(false);
        
        buttons[19].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.IS_BLACK);
                receiveMessage();
            }
        });
        buttons[19].setEnabled(false);
        
        buttons[20].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.ACK);
            }
        });
        buttons[21].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.NACK);
                receiveMessage();
            }
        });
        buttons[21].setEnabled(false);
        
        buttons[22].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.UNKNOW_OP);
                receiveMessage();
            }
        });
        buttons[22].setEnabled(false);
        
        buttons[23].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.ILLEGAL_OP);
                receiveMessage();
            }
        });
        buttons[23].setEnabled(false);
        
        buttons[24].addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(out, ProtocolOperators.GENERAL_ERROR);
                receiveMessage();
            }
        });
        buttons[24].setEnabled(false);
        
        buttons[25].addActionListener(new ActionListener() {
          	 
            public void actionPerformed(ActionEvent e)
            {
                readFromServer();
            }
        });
        
        testFrame.setLayout(new GridLayout(6,4));
                
        for(JButton b : buttons) {
        	testFrame.add(b);
        }
        
        testFrame.add(colour_label);
        testFrame.add(new JLabel("retVal: "));
        testFrame.add(retVal);
        testFrame.add(new JLabel("fromSrv: "));
        testFrame.add(fromSrv);
        testFrame.add(new JLabel("operands: "));
        testFrame.add(operand0);
        testFrame.add(operand1);
        
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
