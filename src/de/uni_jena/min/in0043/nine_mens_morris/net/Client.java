package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.core.Game;
import de.uni_jena.min.in0043.nine_mens_morris.core.Phase;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;

public class Client extends Thread implements Game {

	final private static int DEFAULT_PORT = 6112; 
	
	private static Logger log = LogManager.getLogger();
	
	private Socket srv;
	private DataInputStream input;
	private DataOutputStream output;
	
	public Client(String srv) {
		this(srv, DEFAULT_PORT);
	}
	
	public Client(String hostName, int port) {
		try {
			srv = new Socket(hostName, port);
			input = new DataInputStream(new BufferedInputStream(srv.getInputStream()));
			output = new DataOutputStream(srv.getOutputStream());
		} catch (UnknownHostException e) {
			log.error("Unkown host: " + hostName);
		} catch (IOException e) {
			log.error("caught IOException while setting up");
		}
	}
	
	private Player logIn(byte[] rcvBuf) {
		try {
			output.write(ProtocolOperators.HELLO);
			input.readFully(rcvBuf);
			
			if(Arrays.equals(rcvBuf, ProtocolOperators.ACK)) {
				log.trace("login successful");
			} else {
				log.error("login failed");
			}
			
			input.readFully(rcvBuf);
			
			if(Arrays.equals(rcvBuf, ProtocolOperators.IS_WHITE)) {
				log.info("colour is white");
				output.write(ProtocolOperators.ACK);
				return Player.WHITE;
		    } else if(Arrays.equals(rcvBuf, ProtocolOperators.IS_BLACK)) {
				log.info("colour is black");
				output.write(ProtocolOperators.ACK);
				return Player.BLACK;
			} else {
				log.error("Protocol error!");
				return null;
			}
			
		} catch (IOException e) {
			log.error("caught IOException while logging in");
			return null;
		}
		
	}
	
	public void run() {
		
		boolean playing = false;
		byte[] rcvBuf = new byte[3];
		Player colour = logIn(rcvBuf);
		
		
		if(colour == Player.BLACK) {
			log.trace("reading white's first move");
			try {
				input.readFully(rcvBuf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(colour == null) {
			log.trace("teh fuck?!?");
		}
		
		while(!playing) {
			int i = 0;
			
			try {
				Thread.sleep(10000);
				log.trace("I wasn't intrerrupted: " + i); 
			} catch (InterruptedException e) {
				log.trace("you got me!");
				
			} finally {
				i++;
				if(i > 9) {
					playing = true;
					log.trace("I'm done playing");
				}
			}
			
			/*
			 * wait for command
			 * 
			 * wake up
			 * 
			 * read command
			 * 
			 * execute command
			 * 
			 * read response
			 * 
			 * wait for command/read 
			 */
			
			
			/*
			 * > hello
			 * < ack
			 * < colour
			 * if white
			 *   > movestone
			 *   .
			 *   .
			 *   .
			 * else
			 *   < movestone
			 *   .
			 *   .
			 *   .
			 * .
			 * .
			 * .
			 * > movestone
			 * < retVal
			 * if ack
			 *   < movestone/mill_created
			 * else
			 *   > movestone
			 *   .
			 *   .
			 *   .
			 * 
			 *   
			 */
			
		}
		
		log.trace("logging off");
		try {
			output.write(ProtocolOperators.BYE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public Player getActivePlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Phase getPhase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRound() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWhiteActivated() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWhiteInPlay() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWhiteLost() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBlackActivated() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBlackInPlay() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBlackLost() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveStone(int stone, int point) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeStone(int stone) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void conceed(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	public void iNeedtoRead() {
		// TODO Auto-generated method stub
		
	}

}
