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
		
		log.trace("state is " + state);
		log.info("starting game");
		if(startGame()) {
			twoPlayers = true;
			state = GameServerState.RUNNING;
			log.info("game has started");
			while(twoPlayers == true) {
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
					twoPlayers = false;
				}
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

		log.info("thread terminating");
		log.exit();
	}
	
	private void notifyOtherPlayer(byte[] msg) throws IOException {
		log.trace("notifying other player");
		if(curr_out == w_out) {
			b_out.write(msg);
		} else {
			w_out.write(msg);
		}
	}
	
	private boolean hasDisconnected() throws Exception {
		if(Arrays.equals(rcvBuf,ProtocolOperators.BYE)) {
			curr_out.write(ProtocolOperators.ACK);
			log.info("player has left, declaring other player winner");
			notifyOtherPlayer(ProtocolOperators.YOU_WIN);
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
			curr_out.write(ProtocolOperators.ACK);
			notifyOtherPlayer(ProtocolOperators.YOU_WIN);
			state = GameServerState.GAME_OVER;
			return true;
		}
		return false;
	}
	
	private void parseInRunning() throws IOException {

		if (rcvBuf[0] == ProtocolOperators.MOVE_STONE[0]) {
			log.trace("player requests moving stone " + rcvBuf[1] + " to point " + rcvBuf[2]);
			int r = logic.moveStone(rcvBuf[1], rcvBuf[2]);
			
			switch(r) {
			case 0:
				log.trace("logic says no can do");
				curr_out.write(ProtocolOperators.NACK);
				break;
			case 1:
				log.trace("logic says ok");
				curr_out.write(ProtocolOperators.ACK);
				notifyOtherPlayer(rcvBuf);
				break;
			case 2:
				byte[] orgMove = Arrays.copyOf(rcvBuf, 3);
				log.trace("logic says ok, and we have a mill");
				curr_out.write(ProtocolOperators.ACK_W_MILL);
				parseInMillCreated(orgMove);
				break;
			default:
				curr_out.write(ProtocolOperators.NACK);
			}
			
		} else {
			curr_out.write(ProtocolOperators.ILLEGAL_OP);
		}
	}
	
	private void parseInMillCreated(byte[] orgMove) throws IOException {
		curr_in.readFully(rcvBuf);
		
		if (rcvBuf[0] == ProtocolOperators.REMOVE_STONE[0]) {
		    //  0 = stone can not be removed
	        //  1 = stone removed
            int r = logic.removeStone(rcvBuf[1]);;
			
			switch(r) {
			case 0:
				curr_out.write(ProtocolOperators.NACK);
				break;
			case 1:
				curr_out.write(ProtocolOperators.ACK);
				notifyOtherPlayer(ProtocolOperators.MILL_CREATED);
				notifyOtherPlayer(orgMove);
				notifyOtherPlayer(rcvBuf);
				break;
			default:
				curr_out.write(ProtocolOperators.NACK);
			}
			
		} else {
			curr_out.write(ProtocolOperators.ILLEGAL_OP);
		}
	}
	
    private void parseInGameOver() throws IOException {
    	if (Arrays.equals(rcvBuf,ProtocolOperators.NEW_GAME)) {
    		log.trace("player wants a new game");
    		notifyOtherPlayer(ProtocolOperators.NEW_GAME);
		} else if (Arrays.equals(rcvBuf,ProtocolOperators.NO_MORE)) {
			log.trace("player does not want a new game");
			notifyOtherPlayer(ProtocolOperators.NO_MORE);
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
