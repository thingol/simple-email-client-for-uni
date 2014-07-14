package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.gui.LogIn.LobbyDisplay;

public class LobbyClient extends Thread {

	final private static int DEFAULT_PORT = 6112;
	
	private static Logger log = LogManager.getLogger();
	
	private Socket srv;
	private DataInputStream input;
	private DataOutputStream output;
	private byte[] cmdBuf = new byte[3];
	private byte[] rcvBuf = new byte[3];
	private Object lock = new Object();
	private LobbyDisplay display;

	
	public LobbyClient(String hostName) {
		this(hostName, DEFAULT_PORT);
	}
	
	public LobbyClient(String hostName, int port) {
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
	
	public void addDisplay(LobbyDisplay display) {
		log.entry();
		synchronized (lock) {
			log.debug("adding display " + display);
			this.display = display;
			lock.notify();
		}
		log.exit();
	}

	private void msgExchange(int op, int opnd0) {
		log.entry(op, opnd0);
		cmdBuf = new byte[]{(byte) op, (byte) opnd0, 0};
    	
    	sendMsg();
    	receiveMsg();
    	log.exit();
		
	}

	private void receiveMsg() {
		log.entry();
		try {
			input.readFully(rcvBuf);
			log.debug("received " + Arrays.toString(rcvBuf));
		} catch (IOException e) {
			log.error("caught IOException while reading from server");
		}
		log.exit();
	}

	private void sendMsg() {
		log.entry();
		try {
			log.debug("sending " + Arrays.toString(cmdBuf));
			output.write(cmdBuf);
		} catch (IOException e) {
			log.error("caught IOException while writing to server");
		}
		log.exit();
	}
	
	private int parseResponse() {
		log.entry();
		int i = cmdBuf[0];
		int retVal = -1;
		
		switch(i) {
		case 0:
			log.debug("We said hello and tried to log in");
			log.debug("rcvBuf = " + Arrays.toString(rcvBuf)); 
			if(Arrays.equals(rcvBuf, ProtocolOperators.ACK)) {
				log.debug("LogIn suceeded");
				retVal = 1;
			} else if (Arrays.equals(rcvBuf, ProtocolOperators.WRONG_CREDS)) {
				log.debug("Wrong User/Password Combination!");
				retVal = 2;
			} else if (Arrays.equals(rcvBuf, ProtocolOperators.LOGGED_IN)) {
				log.debug("You are already logged in!");
				retVal = 3;
			} else if (Arrays.equals(rcvBuf, ProtocolOperators.NACK)) {
				log.debug("LogIn failed due to unknown reasons!");
				retVal = 0;
			} else if (Arrays.equals(rcvBuf, ProtocolOperators.SERVER_FULL)) {
				log.debug("Server is full, sorry!");
				retVal = 4;
			} else {
				log.debug("What happened?");
				retVal = -128;
			}
			break;
		case 0x1A:
			log.debug("We sent a playWith");
			if(Arrays.equals(rcvBuf, ProtocolOperators.ACK)) {
				log.debug("and got back an ACK");
				retVal = 1;
			} else if(Arrays.equals(rcvBuf, ProtocolOperators.NACK)) {
				log.debug("and got back a NACK");
				retVal = 0;
			} else if(Arrays.equals(rcvBuf, ProtocolOperators.NO_RESPONSE)) {
				log.debug("lol no response");
			} else if(Arrays.equals(rcvBuf, ProtocolOperators.DECLINED)) {
				log.debug("Declined! Guess you aren't allowed to");
			}
			log.debug("and got back something funny");
			break;
		default:
			// shouldn't happen, but hey
			log.debug("this really shouldn't happen");
			retVal = -128;
		}
		if(rcvBuf[0] == 0x1A) {
			//display.challenged(rcvBuf[1]);
		}
		log.exit(retVal);
		return retVal;
	}
	
	private void handleCmd() {
		if(cmdBuf != null) {
			//...
		}
	}
	
	public void run() {
		boolean playing = true;
		int reps = 0;
		
		synchronized (lock) {
			while(display == null) {
				log.debug("waiting for display");
				try {
					lock.wait();
				} catch (InterruptedException e) {
					log.error("was interrupted while waiting for a display");
				}
			}
		}

		while(playing) {
			
			if(reps < 10) {
				reps++;
			} else {
			    reps = 0;
			    updatePlayerList();	
			}
			
			cmdBuf = display.getCmdBuf();
			handleCmd();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				log.error("interrupted while waiting, no idea how that could happen");
			}
		}
		
		log.debug("logging off");
		disconnect();
		try {
			srv.close();
		} catch (IOException e) {
			log.error("an error was encountered while attempting to " +
		              "close the connection to the server");
		}
		log.exit();
	}

	private void updatePlayerList() {
		cmdBuf = ProtocolOperators.GET_USERLIST;
		sendMsg();
		String[] userNames;
		int[] userIDs;
		String[] users;
		
		try {
			users = new BufferedReader(new InputStreamReader(input)).readLine().split(";");//new String(input.read()).split(";");
			userNames = new String[users.length];
			userIDs = new int[users.length];
			
			String[] u;

			for(int i = 0; i < users.length; i++) {
				u = users[i].split(",");
				userNames[i] = u[0];
				userIDs[i] = Integer.parseInt(u[1]);
			}
			
			display.setPlayerList(userNames, userIDs);
			
			
		} catch (IOException e) {
			log.error("caught " + e.getClass() + " while fetching list of users");
		}
		
		
	}

	public void disconnect() {
		cmdBuf = ProtocolOperators.BYE;
		sendMsg();
	}
	
	public int logIn(String userInfo, boolean newUser) {
		cmdBuf = ProtocolOperators.HELLO;
		sendMsg();
		
		int retVal = -1;
		if(newUser)	cmdBuf = ProtocolOperators.NEW_USER;
		else cmdBuf = ProtocolOperators.EXISTING_USER;
		
		try {
			sendMsg();
			log.entry("Writing " + userInfo + " to Server");
			output.writeUTF(userInfo);
			receiveMsg();
		} catch (IOException e) {
			log.error("caught " + e.getClass() + " while logging in");
		}
		retVal = parseResponse();
		return retVal;
	}
	
	public void acceptChallenge(boolean accept) {
		if(accept) cmdBuf = ProtocolOperators.ACK;
		else cmdBuf = ProtocolOperators.NACK;
		sendMsg();
	}


}