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
	private Player colour = null;
	private byte[] cmdBuf = new byte[3];
	private byte[] rcvBuf = new byte[3];
	private Object lock = new Object();
	private boolean cmdSent = false;
	private boolean playing = false;
	private ClientState state = ClientState.WAITING;
	
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
	
	private void logIn() {
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
				colour =  Player.WHITE;
				playing = true;
		    } else if(Arrays.equals(rcvBuf, ProtocolOperators.IS_BLACK)) {
				log.info("colour is black");
				output.write(ProtocolOperators.ACK);
				colour =  Player.BLACK;
				playing = true;
			} else {
				log.error("Protocol error!");
			}
			
		} catch (IOException e) {
			log.error("caught IOException while logging in");
		}
		
	}
	
	private void pokeMe() {
		synchronized (lock) {
			cmdSent = true;
			lock.notify();
		}
	}

	private void msgExchange(int op, int opnd0) {
		cmdBuf[0] = (byte) op;
    	cmdBuf[1] = (byte) opnd0;
    	cmdBuf[2] = 0;
    	
    	sendMsg();
    	receiveMsg();
	}
	
	private void msgExchange(int op, int opnd0, int opnd1) {
		cmdBuf[0] = (byte) op;
    	cmdBuf[1] = (byte) opnd0;
    	cmdBuf[2] = (byte) opnd1;
    	
    	sendMsg();
    	receiveMsg();
	}
	
	private void receiveFromOtherPlayer() {
		receiveMsg();
		
		if(rcvBuf[0] == ProtocolOperators.MOVE_STONE[0]) {
			log.trace("I'd call Head.moveStone(" + rcvBuf[1] + "," + rcvBuf[2] + ")");
			state = ClientState.SINGLE_MOVE;
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.MILL_CREATED)) {
			log.trace("Seems there's more than one message on the way");
			state = ClientState.MILL_CREATED;
		} else if(rcvBuf[0] == ProtocolOperators.REMOVE_STONE[0]) {
			log.trace("I'd call Head.moveStone(" + rcvBuf[1] + ")");
			state = ClientState.SINGLE_MOVE;
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.YOU_WIN)) {
			state = ClientState.GAME_WON;
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.YOU_LOSE)) {
			state = ClientState.GAME_LOST;
			log.trace("I'd call Head.newGame() or so");
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.NEW_GAME)) {
			log.trace("I'd call Head.reset()");
		} else if(Arrays.equals(rcvBuf, ProtocolOperators.NO_MORE))  {
			log.trace("guess there's not to be another round...");
		}		
	}
	
	private int parseResponse() {
		int i = cmdBuf[0];
		
		switch(i) {
		case 1:
			if(Arrays.equals(rcvBuf, ProtocolOperators.ACK)) {
				return 1;
			} else if (Arrays.equals(rcvBuf, ProtocolOperators.ACK_W_MILL)) {
				return 2;
			} else {
				return 0;
			}
		case 2:
			if(Arrays.equals(rcvBuf, ProtocolOperators.ACK)) {
				return 1;
			}
			return 0;
		default:
			// shouldn't happen, but hey
			return -128;
		}
	}
	
	private void receiveMsg() {
		try {
			input.readFully(rcvBuf);
		} catch (IOException e) {
			log.error("caught IOException while reading from server");
		}
	}
	
    private void sendMsg() {
    	try {
			output.write(cmdBuf);
		} catch (IOException e) {
			log.error("caught IOException while writing to server");
		}
	}
	
	public void run() {
		logIn();
		
		if(colour == Player.BLACK) {
			log.trace("reading white's first move");
			receiveMsg();
			parseResponse();
			
			try {
				input.readFully(rcvBuf);
			} catch (IOException e) {
				log.error("the game has not really started and already we have an error");
			}
		} else if(colour == null) {
			log.trace("teh fuck?!? how could we not get a colour?");
		}
		
		while(playing) {
						
			synchronized (lock) {
				
				while(!cmdSent) {
					try {
						log.trace("waiting for something to do");
						lock.wait();
					} catch (InterruptedException e) {
						log.error("was interrupted while waiting for something to do, this is not normal");
					}
					
					receiveMsg();
					receiveFromOtherPlayer();
					
					switch (state) {
					case SINGLE_MOVE:
						break;
					case MILL_CREATED:
						receiveFromOtherPlayer();
						receiveFromOtherPlayer();
						break;
					case GAME_WON:
						receiveFromOtherPlayer();
					}
				}
			}
			
			log.trace("the message sent was supposed to be: " + Arrays.toString(cmdBuf));
			cmdSent = false;
			cmdBuf[0] = -128;
			cmdBuf[1] = -128;
			cmdBuf[2] = -128;
		
		}
		
		log.trace("logging off");
		disconnect();
		try {
			srv.close();
		} catch (IOException e) {
			log.error("an error was encountered while attempting to " +
		              "close the connection to the server");
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
		msgExchange(ProtocolOperators.MOVE_STONE[0], (byte) stone, (byte) point);
		int retVal = parseResponse(); 
		
		// we don't want to start working if the request fails
		if(retVal == 1 || retVal == 2) {
			pokeMe();
		}
		return retVal;
	}

	@Override
	public int removeStone(int stone) {
		msgExchange(ProtocolOperators.REMOVE_STONE[0], (byte) stone);
		
		pokeMe();
		return parseResponse();
	}

	public void conceed(boolean newGame) {
		cmdBuf = ProtocolOperators.CONCEDE;
		receiveMsg();
		if(newGame) {
			cmdBuf = ProtocolOperators.NEW_GAME;
			sendMsg();
			receiveMsg();
		} else {
			cmdBuf = ProtocolOperators.NO_MORE;
			sendMsg();
			playing = false;
		}
		
	}

	public void disconnect() {
		cmdBuf = ProtocolOperators.BYE;
		sendMsg();
		playing = false;
	}

	public void iNeedtoRead() {
		// TODO Auto-generated method stub
		
	}

}
