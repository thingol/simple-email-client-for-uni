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

import de.uni_jena.min.in0043.nine_mens_morris.gui.TestLogIn;

public class LogInClient extends Thread {

	final private static int DEFAULT_PORT = 6112; 
	
	private static Logger log = LogManager.getLogger();
	
	private Socket srv;
	private DataInputStream input;
	private DataOutputStream output;
	private volatile byte[] cmdBuf = new byte[3];
	private volatile byte[] rcvBuf = new byte[3];
	private Object lock = new Object();
	private ClientState state = ClientState.WAITING;
	private TestLogIn display;

	private boolean cmdSent;
	private boolean login; //If Player didnt enter username/pw yet
	

	public LogInClient(String hostName) {
		this(hostName, DEFAULT_PORT);
	}
	
	public LogInClient(String hostName, int port) {
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
	
	public void addDisplay(TestLogIn display) {
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
		int retVal;
		
		switch(i) {
		case 0x00:
			log.debug("We said hello and tried to log in");
			log.debug("rcvBuf = " + Arrays.toString(rcvBuf)); 
			if(Arrays.equals(rcvBuf, ProtocolOperators.ACK)) {
				log.debug("LogIn suceeded");
				retVal = 1;
				break;
			} else if (Arrays.equals(rcvBuf, ProtocolOperators.WRONG_CREDS)) {
				log.debug("Wrong User/Password Combination!");
				retVal = 2;
				break;
			} else if (Arrays.equals(rcvBuf, ProtocolOperators.LOGGED_IN)) {
				log.debug("You are already logged in!");
				retVal = 3;
				break;
			} else if (Arrays.equals(rcvBuf, ProtocolOperators.NACK)) {
				log.debug("LogIn failed due to unknown reasons!");
				retVal = 0;
				break;
			} else if (Arrays.equals(rcvBuf, ProtocolOperators.SERVER_FULL)) {
				log.debug("Server is full, sorry!");
				retVal = 4;
				break;
			} else {
				log.debug("What happened?");
				retVal = -128;
			}
			break;
		case 0x14:
			log.debug("We sent a playWith");
			if(Arrays.equals(rcvBuf, ProtocolOperators.ACK)) {
				log.debug("and got back an ACK");
				retVal = 1;
				break;
			} else if(Arrays.equals(rcvBuf, ProtocolOperators.NACK)) {
				log.debug("and got back a NACK");
				retVal = 0;
				break;
			} else if(Arrays.equals(rcvBuf, ProtocolOperators.NO_RESPONSE)) {
				log.debug("lol no response");
			} else if(Arrays.equals(rcvBuf, ProtocolOperators.DECLINED)) {
				log.debug("Declined! Guess you aren't allowed to");
			}
			log.debug("and got back something funny");
		default:
			// shouldn't happen, but hey
			log.debug("this really shouldn't happen");
			retVal = -128;
		}
		log.exit(retVal);
		return retVal;
	}
	
	public void run() {
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
		
		cmdBuf = ProtocolOperators.HELLO;
		sendMsg();
		while(login) {
			synchronized (lock) {
				while(!cmdSent) {
					try {
						log.debug("waiting for Username and Password");
						lock.wait();
					} catch (InterruptedException e) {
						log.error("was interrupted while waiting for something to do, this is not normal");
					}
				}
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

	public String getPlayerList() {
		msgExchange(6, 0);
		String g = "";
		try {
			g = input.readUTF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return g;
		
		
	}

	public void disconnect() {
		cmdBuf = ProtocolOperators.BYE;
		sendMsg();
	}
	
	public int sendLogIn(String UP, boolean newUser) {
		int retVal;
		if(newUser) {
			cmdBuf[0] = 0;
			cmdBuf[1] = 1;
			cmdBuf[2] = 1;
			}
		else {
			cmdBuf[0] = 0;
			cmdBuf[1] = 1;
			cmdBuf[2] = 0;
		}
		try {
		sendMsg();
		output.writeChars(UP);
		receiveMsg();
		} catch (IOException e) {
			e.printStackTrace();
		}
		retVal = parseResponse();
		return retVal;
	}

	public int duel(int number) {
		msgExchange(0x14, number);
		int retVal = parseResponse();
		return retVal;
	}
	
	public void acceptChallenge(boolean g) {
		if(g) cmdBuf = ProtocolOperators.ACK;
		else cmdBuf = ProtocolOperators.NACK;
		sendMsg();
	}

}