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

import de.uni_jena.min.in0043.nine_mens_morris.core.GameClient;
import de.uni_jena.min.in0043.nine_mens_morris.gui.Head;
import de.uni_jena.min.in0043.nine_mens_morris.gui.LobbyDisplay;

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
	private LobbyClientState state = LobbyClientState.WAITING;
	private boolean playing = true;
	private String myName = "";
	private int myID = -1;

	
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

	private void receiveMsg(boolean checkInput) {
		try {
			if(checkInput) {
				if(input.available() != 0) {
					log.debug("receiving message");
					log.debug("input.available() => " + input.available());
					input.readFully(rcvBuf);
					log.debug("received " + Arrays.toString(rcvBuf));
				} else {
					rcvBuf = null;
				}
     		} else {
     			input.readFully(rcvBuf);
     		}
		} catch (IOException e) {
			log.error("caught IOException while reading from server");
		}
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
	
	private void handleMsgFromServer() {
		if(rcvBuf == null) {
			rcvBuf = new byte[3];
	    } else if(rcvBuf[0] == ProtocolOperators.CHALLENGE[0]) {
	    	log.info("challenge received");
			state = LobbyClientState.CHALLENGE_RECEIVED;
			display.setChallenge();
			display.challenged(rcvBuf[2]);
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.GET_USERLIST)) {
			log.debug("receiving updated list of users");
			updatePlayerList();
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.PING)) {
			log.debug("received PING");
			cmdBuf = ProtocolOperators.PONG;
			sendMsg();
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.ACK)) {
			state = LobbyClientState.CHALLENGE_ACCEPTED;
			display.setVisible(false);
			startGame();
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.DECLINED)) {
			state = LobbyClientState.NORMAL;
			display.setNormal();
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.BUSY)) {
			state = LobbyClientState.NORMAL;
			display.setNormal();
		}
	}
	
	private void handleCmdFromUser() {
		if(cmdBuf != null) {
			if(cmdBuf[0] == ProtocolOperators.CHALLENGE[0]) {
				state = LobbyClientState.CHALLENGE_ISSUED;
				cmdBuf[2] = (byte)myID;
				display.setChallenge();
			} else if(Arrays.equals(cmdBuf, ProtocolOperators.NACK)) {
				state = LobbyClientState.NORMAL;
				display.setNormal();
			} else if(Arrays.equals(cmdBuf, ProtocolOperators.ACK)) {
				state = LobbyClientState.CHALLENGE_ACCEPTED;
				display.setVisible(false);
				startGame();
			} else if(Arrays.equals(cmdBuf, ProtocolOperators.BYE)) {
				playing = false;
				log.debug("logging off");
			}
			sendMsg();
		}
	}
	
	public void run() {
		log.entry();
		
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

			cmdBuf = display.getCmdBuf();
			handleCmdFromUser();

			while(state == LobbyClientState.CHALLENGE_ACCEPTED) {
				synchronized (this) {
					log.debug("we've accepted a challenge, I'll be waiting when we're done");
					try {
						this.wait();
						log.debug("we woke up");
					} catch (InterruptedException e) {
						log.error("was interrupted while waiting for game to end");
					}
				}
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				log.error("interrupted while waiting, no idea how that could happen");
			}

			receiveMsg(true);
			handleMsgFromServer();

			while(state == LobbyClientState.CHALLENGE_ACCEPTED) {
				synchronized (this) {
					log.debug("our challenge has been accepted, I'll be waiting when we're done");
					try {
						this.wait();
						log.debug("we woke up");
					} catch (InterruptedException e) {
						log.error("was interrupted while waiting for game to end");
					}
				}
			}
		}

		try {
			srv.close();
		} catch (IOException e) {
			log.error("an error was encountered while attempting to " +
		              "close the connection to the server");
		}
		log.exit();
	}

	private void updatePlayerList() {
		log.entry();
		String[] userNames;
		int[] userIDs;
		String[] users;
		
		try {
			users = new BufferedReader(new InputStreamReader(input)).readLine().split(";");
			if(users.length > 1) {
				userNames = new String[users.length-1];
				userIDs = new int[users.length-1];
				
				String[] u;

				int n = 0;
				for(int i = 0; i < users.length; i++) {
					u = users[i].split(",");
					if(!u[0].equals(myName)) {
						userNames[n] = u[0];
						userIDs[n] = Integer.parseInt(u[1]);
						n++;
					} else {
						myID = Integer.parseInt(u[1]);
					}
				}
				
				log.debug("updating list of active users");
				display.setPlayerList(userNames, userIDs);
			} else {
				log.debug("we are alone");
				display.setPlayerList(null, null);
			}
		} catch (IOException e) {
			log.error("caught " + e.getClass() + " while fetching list of users");
		}
	}

	private int parseResponse() {
		log.entry();
		int retVal = -1;

		log.debug("We said hello and tried to log in"); 
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

		log.exit(retVal);
		return retVal;
	}
	
	private void startGame() {
		log.entry();
		Client client = new Client(input,output,this);
		GameClient display = new Head(client);

		log.debug("firing up Client");
		client.start();
		client.addDisplay(display);
		((Head)display).init();
			
		log.exit();
	}

	public synchronized void gameOver() {
		log.debug("notifying LobbyClient of game over");
		state = LobbyClientState.NORMAL;
		display.setNormal();
		display.setVisible(true);
		this.notify();
		log.exit();
	}
	
	public int logIn(String userInfo, boolean newUser) {
		cmdBuf = ProtocolOperators.HELLO;
		sendMsg();
		
		int retVal = -1;
		if(newUser)	cmdBuf = ProtocolOperators.NEW_USER;
		else cmdBuf = ProtocolOperators.EXISTING_USER;
		
		try {
			sendMsg();
			log.debug("Writing " + userInfo + " to Server");
			output.writeUTF(userInfo);
			receiveMsg(false);
		} catch (IOException e) {
			log.error("caught " + e.getClass() + " while logging in");
		}
		retVal = parseResponse();
		return retVal;
	}
	
	public void setUserName(String userName) {
		myName = userName;
	}
}