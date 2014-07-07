package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.rmi.PortableRemoteObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.core.Game;
import de.uni_jena.min.in0043.nine_mens_morris.core.GameClient;
import de.uni_jena.min.in0043.nine_mens_morris.core.Phase;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;

public class Client extends Thread implements Game {

	final private static int DEFAULT_PORT = 6112; 
	
	private static Logger log = LogManager.getLogger();
	
	private Socket srv;
	private DataInputStream input;
	private DataOutputStream output;
	private Player colour = null;
	private byte[] cmdBuf = new byte[3];
	private byte[] rcvBuf = new byte[3];
	private Object lock = new Object();
	private boolean cmdSent = false;
	private boolean playing = false;
	private ClientState state = ClientState.WAITING;
	private GameClient display; 
	

	public Client(String hostName) {
		this(hostName, DEFAULT_PORT);
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
	
	
	/*
	 * situational handlers
	 */
	private void handleAwaitingNewGame() {
		log.entry();
		if(Arrays.equals(rcvBuf, ProtocolOperators.ACK)) {
			log.info("new game has been accepted");
			display.reset();
			handleColour();
			
			if(colour == Player.BLACK) {
				log.debug("reading white's first move");
				receiveFromOtherPlayer();
			} else if(colour == null) {
				log.debug("teh fuck?!? how could we not get a colour?");
			}
			
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.NACK)) {
			log.info("new game has not been accepted, terminating");
			playing = false;
		} else {
			log.debug("received " + Arrays.toString(rcvBuf));
			log.error("Protocol error!");
			playing = false;
		}
		
		log.exit();
	}
	
	private void handleColour() {
		receiveMsg();
		if(Arrays.equals(rcvBuf, ProtocolOperators.IS_WHITE)) {
			log.info("colour is white");
			colour =  Player.WHITE;
	    } else if(Arrays.equals(rcvBuf, ProtocolOperators.IS_BLACK)) {
			log.info("colour is black");
			colour =  Player.BLACK;
		} else {
			log.debug("received " + Arrays.toString(rcvBuf));
			log.error("Protocol error!");
			return;
		}

		playing = true;
		display.setColour(colour);
		ack();
	}
	
	private void handleGameOver() {
		log.entry();
		receiveMsg();
		if(Arrays.equals(rcvBuf, ProtocolOperators.NEW_GAME)) {
			if(display.newGame(true)) {
				cmdBuf = ProtocolOperators.NEW_GAME;
				handleColour();
				
			} else {
				cmdBuf = ProtocolOperators.NO_MORE;
			}
			
			sendMsg();
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.NO_MORE)){
			playing = false;
		}
		
		
		log.exit();
	}
	
	
	/*
	 * misc methods
	 */
	public void addDisplay(GameClient display) {
		log.entry();
		synchronized (lock) {
			log.debug("adding display " + display);
			this.display = display;
			lock.notify();
		}
		log.exit();
	}
	
	private void logIn() {
		log.entry();
		try {
			output.write(ProtocolOperators.HELLO);
			input.readFully(rcvBuf);
			
			if(Arrays.equals(rcvBuf, ProtocolOperators.ACK)) {
				log.debug("login successful");
			} else {
				log.error("login failed");
			}		
		} catch (IOException e) {
			log.error("caught IOException while logging in");
		}
		
		log.exit();
	}
	
	private void pokeMe() {
		log.entry();
		synchronized (lock) {
			log.debug("poking client");
			cmdSent = true;
			lock.notify();
		}
		log.exit();
	}

	/*
	 * communication
	 */
	private void ack() {
		log.entry();
		log.debug("ACK'ing");
		cmdBuf = ProtocolOperators.ACK;
		sendMsg();
		log.exit();
	}
	
	public void conceed(boolean newGame) {
		log.entry(newGame);
		
		cmdBuf = ProtocolOperators.CONCEDE;
		sendMsg();
		if(newGame) {
			state = ClientState.AWAITING_NEW_GAME;
			cmdBuf = ProtocolOperators.NEW_GAME;
			log.debug("requesting new game");
			sendMsg();
			receiveMsg();
			pokeMe();
		} else {
			cmdBuf = ProtocolOperators.NO_MORE;
			log.debug("signing off");
			sendMsg();
			playing = false;
			log.debug("quitting");
			cmdBuf = ProtocolOperators.BYE;
			sendMsg();
		}
		
		log.exit();
	}
	
	public void disconnect() {
		log.entry();
		cmdBuf = ProtocolOperators.BYE;
		sendMsg();
		playing = false;
		log.exit();
	}
	
	private void msgExchange(int op, int opnd0) {
		log.entry(op, opnd0);
		cmdBuf[0] = (byte) op;
    	cmdBuf[1] = (byte) opnd0;
    	
    	sendMsg();
    	receiveMsg();
    	log.exit();
	}
	
	private void msgExchange(int op, int opnd0, int opnd1) {
		log.entry();
		cmdBuf[0] = (byte) op;
    	cmdBuf[1] = (byte) opnd0;
    	cmdBuf[2] = (byte) opnd1;
    	log.debug("sending " + Arrays.toString(cmdBuf));
    	sendMsg();
    	receiveMsg();
    	log.debug("received " + Arrays.toString(rcvBuf));
    	log.exit();
	}
	
	private void receiveFromOtherPlayer() {
		log.entry();
		receiveMsg();
		
		if(rcvBuf[0] == ProtocolOperators.MOVE_STONE[0]) {
			state = ClientState.SINGLE_MOVE;
			log.debug("calling display.moveStone(" + rcvBuf[1] + "," + rcvBuf[2] + ")");
			display.moveStone(rcvBuf[1], rcvBuf[2]);
			
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.MILL_CREATED)) {
			state = ClientState.MILL_CREATED;
			log.debug("Seems there's more than one message on the way");
			
		} else if(rcvBuf[0] == ProtocolOperators.REMOVE_STONE[0]) {
			state = ClientState.SINGLE_MOVE;
			log.debug("calling display.removeStone(" + rcvBuf[1] + ")");
			display.removeStone(rcvBuf[1]);
			
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.YOU_WIN)) {
			state = ClientState.GAME_WON;
			
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.YOU_LOSE)) {
			state = ClientState.GAME_LOST;
			log.debug("state is: " + state + ",calling handleGameOver()");
			handleGameOver();
			
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.NEW_GAME)) {
			// if both say NEW_GAME
			log.debug("calling display.reset()");
			display.reset();
			
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.NO_MORE))  {
			//if the other says NO_MORE
			log.debug("guess there's not to be another round...");
			playing = false;
		}
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
	
	/*
	 * Parsing of messages
	 */
	private int parseResponse() {
		log.entry();
		int i = cmdBuf[0];
		int retVal;
		
		switch(i) {
		case 1:
			log.debug("we sent a moveStone");
			log.debug("rcvBuf = " + Arrays.toString(rcvBuf)); 
			if(Arrays.equals(rcvBuf, ProtocolOperators.ACK)|| rcvBuf[0] == -2) {
				log.debug("and got back an ACK");
				retVal = 1;
			} else if (Arrays.equals(rcvBuf, ProtocolOperators.ACK_W_MILL)) {
				log.debug("and got back an ACK_W_MILL");
				retVal = 2;
			} else if(Arrays.equals(rcvBuf, ProtocolOperators.NACK)){
				log.debug("and got back a NACK");
				retVal = 0;
			} else {
				log.debug("teh fuck?!?");
				retVal = -128;
			}
			break;
		case 2:
			log.debug("we sent a moveStone");
			if(Arrays.equals(rcvBuf, ProtocolOperators.ACK)) {
				log.debug("and got back an ACK");
				retVal = 1;
			}
			log.debug("and got back a NACK");
			retVal = 0;
			break;
		default:
			// shouldn't happen, but hey
			log.debug("this really shouldn't happen");
			retVal = -128;
		}
		log.exit(retVal);
		return retVal;
	}
	
	
	/*
	 * the core
	 */
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
		
		logIn();
		handleColour();
		
        if(colour == Player.BLACK) {
			log.debug("reading white's first move");
			receiveFromOtherPlayer();
		} else if(colour == null) {
			log.debug("teh fuck?!? how could we not get a colour?");
		}
		
        /*
         * main loop
         */
		while(playing) {
			synchronized (lock) {
				while(!cmdSent) {
					try {
						log.debug("waiting for something to do");
						lock.wait();
					} catch (InterruptedException e) {
						log.error("was interrupted while waiting for something to do, this is not normal");
					}
				}
			}
					
			receiveFromOtherPlayer();
			log.debug("state is " + state);

			switch (state) {
			case MILL_CREATED:
				// we get a moveStone followed by a removeStone
				receiveFromOtherPlayer();
				receiveFromOtherPlayer();
				break;
			case GAME_WON:
				handleGameOver();
				break;
			case GAME_LOST:
				handleGameOver();
				break;
			case AWAITING_NEW_GAME:
				handleAwaitingNewGame();
				break;
			default:
				break;
			}
			
			cmdSent = false;
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
	
	/*
	 * overridden methods from Game
	 */
	@Override
	public Player getActivePlayer() {
		return null;
	}

	@Override
	public Phase getPhase() {
		return null;
	}

	@Override
	public int getRound() {
		return 0;
	}

	@Override
	public int getWhiteActivated() {
		return 0;
	}

	@Override
	public int getWhiteInPlay() {
		return 0;
	}

	@Override
	public int getWhiteLost() {
		return 0;
	}

	@Override
	public int getBlackActivated() {
		return 0;
	}

	@Override
	public int getBlackInPlay() {
		return 0;
	}

	@Override
	public int getBlackLost() {
		return 0;
	}

	@Override
	public int moveStone(int stone, int point) {
		log.entry();
		msgExchange(ProtocolOperators.MOVE_STONE[0], (byte) stone, (byte) point);
		int retVal = parseResponse(); 
		
		// we don't want to start working if the request fails
		if(retVal == 1) {
			pokeMe();
		}
		log.exit(retVal);
		return retVal;
	}

	@Override
	public int removeStone(int stone) {
		log.entry(stone);
		msgExchange(ProtocolOperators.REMOVE_STONE[0], (byte) stone);
		
		
		int retVal = parseResponse();
		
		if(retVal == 1) {
			pokeMe();
		}
		log.exit(retVal);
		return retVal;
	}

}
