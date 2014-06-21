package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import de.uni_jena.min.in0043.nine_mens_morris.core.Logic;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;

public class GameServer extends Thread {

	private static Logger log = LogManager.getLogger();
	private Logic logic;
	private Socket[] players = new Socket[2];
	private DataInputStream w_in, b_in, curr_in;
	private OutputStream w_out, b_out, curr_out;
	private GameServerState state = GameServerState.WAITING_TO_START;
	private final long sid;
	private byte[] rcvBuf = new byte[3];
	private final static Random rg = new Random();
	private boolean twoPlayers = false;

	public GameServer(long sid, Socket player0, Socket player1) {
		ThreadContext.put("sID", "" + sid);
		log.entry(sid, player0, player1);
		this.sid = sid;
		
		players[0] = player0;
		players[1] = player1;
		
		log.exit(sid);
	}

	public void run() {
		ThreadContext.put("sID", "" + sid);
		log.entry();
		int i = 0;
		
		log.trace("state is " + state);
		log.info("starting game");
		if(startGame()) {
			twoPlayers = true;
			state = GameServerState.RUNNING;
			log.info("game has started");
			while(twoPlayers == true && i < 10) {
				if (logic.getActivePlayer() == Player.WHITE) {
					log.trace("white's move");
					curr_in = w_in;
					curr_out = w_out;
				} else {
					log.trace("black's move");
					curr_in = b_in;
					curr_out = b_out;
				}
				
				try {
					curr_in.readFully(rcvBuf);
					parseThatShit();
				} catch (Exception e) {
					log.error("caught exception: " + e);
					log.info("game is over due an untimely exception");
				}
				i++;
			}
		} else {
			log.info("we have lost a player, and thus have no game");
			try {
				players[0].close();
				players[1].close();
			} catch (IOException e) {
				log.error("caught an exception while shutting down");
			}
		}
		try {
			if(rg.nextBoolean()) {
				w_out.write(ProtocolOperators.YOU_WIN);
				b_out.write(ProtocolOperators.YOU_LOSE);
			} else {
				w_out.write(ProtocolOperators.YOU_LOSE);
				b_out.write(ProtocolOperators.YOU_WIN);
			}
		} catch (IOException e) {
			
		}
		log.info("we no longer have two players connected, thread terminating");
		log.exit();
	}
	
	private boolean hasDisconnected() throws Exception {
		if(Arrays.equals(rcvBuf,ProtocolOperators.BYE)) {
			if(curr_out == w_out) {
				b_out.write(ProtocolOperators.YOU_WIN);
			} else {
				w_out.write(ProtocolOperators.YOU_WIN);
			}
			state = GameServerState.GAME_OVER;
			twoPlayers = false;
			return true;
		}
		return false;
	}
	
	private boolean hasConceded() throws IOException {
		if(Arrays.equals(rcvBuf,ProtocolOperators.CONCEDE)) {
			state = GameServerState.GAME_OVER;
			log.info("state is now " + state);
			if(curr_out == w_out) {
				b_out.write(ProtocolOperators.YOU_WIN);
			} else {
				w_out.write(ProtocolOperators.YOU_WIN);
			}
			state = GameServerState.GAME_OVER;
			return true;
		}
		return false;
	}
	
	private void parseInRunning() throws IOException {
		/*
		 * moveStone         0x01{00 .. 11}{00 .. 17}*/
		if (rcvBuf[0] == ProtocolOperators.MOVE_STONE[0]) {
			logic.moveStone(rcvBuf[1], rcvBuf[2]);
		} else {
			curr_out.write(ProtocolOperators.ILLEGAL_OP);
		}
	}
	
	private void parseInMillCreated() throws IOException {
		if (rcvBuf[0] == ProtocolOperators.REMOVE_STONE[0]) {
			logic.removeStone(rcvBuf[1]);
		} else {
			curr_out.write(ProtocolOperators.ILLEGAL_OP);
		}
	}
	
    private void parseInGameOver() throws IOException {
    	if (Arrays.equals(rcvBuf,ProtocolOperators.NEW_GAME)) {
    		log.trace("player wants anew game");
		} else if (Arrays.equals(rcvBuf,ProtocolOperators.NO_MORE)) {
			log.trace("player does not want a new game");
		} else {
			log.trace("illegal operator received");
			curr_out.write(ProtocolOperators.ILLEGAL_OP);
		}
	}
	
    private void parseThatShit() throws Exception {
    	log.entry();

    	if(hasDisconnected()) {
    		log.trace("player has left");
    	} else if(state == GameServerState.GAME_OVER) {
    		parseInGameOver();
    	} else if(!hasConceded()) {
    		switch(state) {
    		case MILL_CREATED:
    			parseInMillCreated();
    			break;
    		case RUNNING:
    			parseInRunning();
    			break;
    		default:
    			log.warn("how the hell did this happen?!?");
    			break;
    		}
    	}
    	log.exit();
    }
	
    private boolean startGame() {
    	log.entry();
    	boolean didItWork = false;
    	int ran = rg.nextInt(2);

    	log.trace("assigning colours to players and getting network streams");
    	log.trace("player[" + (1 - ran) + "] is white");
    	try {
    		w_in = new DataInputStream(new BufferedInputStream(players[1 - ran].getInputStream()));
    		w_out = new DataOutputStream(players[1 - ran].getOutputStream());
    		b_in = new DataInputStream(new BufferedInputStream(players[ran].getInputStream()));
    		b_out = new DataOutputStream(players[ran].getOutputStream());
    	
    		log.trace("notifying client of colour");
    		w_out.write(ProtocolOperators.IS_WHITE);
    		w_in.readFully(rcvBuf);
    		log.trace("received " + Arrays.toString(rcvBuf));
    		if(rcvBuf.equals(ProtocolOperators.ACK)) {
    			log.error("white does not ack, notifying black");
    			b_out.write(ProtocolOperators.YOU_WIN);
    			log.exit(didItWork);
    			return didItWork;
    		}
    		log.trace("white acks");
    		b_out.write(ProtocolOperators.IS_BLACK);
    		b_in.readFully(rcvBuf);
    		if(rcvBuf.equals(ProtocolOperators.ACK)) {
    			log.error("black does not ack, notifying white");
    			w_out.write(ProtocolOperators.YOU_WIN);
    			log.exit(didItWork);
    			return didItWork;
    		}
    		log.trace("black acks");
    	} catch (IOException e) {
    		log.error("caught exception while attempting to start game: " + e);
    		return didItWork;
    	}
    	logic = new Logic();
    	didItWork = true;
    	log.exit(didItWork);
    	return didItWork;
    }

}
