package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import de.uni_jena.min.in0043.nine_mens_morris.core.Logic;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;

public class GameServer extends Thread {

	private static Logger log = LogManager.getLogger();
	private Logic logic;
	private Socket[] players = new Socket[2];
	private DataInputStream w_in, b_in;
	private OutputStream w_out, b_out;
	private GameServerState state = GameServerState.WAITING_TO_START;
	private final UUID sid;

	public GameServer(UUID sid, Socket player0, Socket player1) {
		ThreadContext.put("sID", "" + sid);
		log.entry(sid, player0, player1);
		logic = new Logic();
		this.sid = sid;
		
		players[0] = player0;
		players[1] = player1;
		
		// gotta be random who gets to be which colour		
		int ran = (int)Math.random();
		
		log.trace("assigning colours to players and getting network streams");
		log.trace("player[" + (1 - ran) + "] is white");
		try {
			w_in = new DataInputStream(new BufferedInputStream(players[1 - ran].getInputStream()));
			w_out = new DataOutputStream(players[1 - ran].getOutputStream());
			b_in = new DataInputStream(new BufferedInputStream(players[ran].getInputStream()));
			b_out = new DataOutputStream(players[ran].getOutputStream());
		} catch (IOException e) {
			log.error("caught IOException when assigning colours");
			e.printStackTrace();
		}
		log.exit(sid);
	}

	public void run() {
		ThreadContext.put("sID", "" + sid);
		log.entry();
		
		
		if(startGame(rcvBuf)) {
			while(!gameOver) {
				
				try {
					if(logic.getActivePlayer() == Player.WHITE) {
						parseThatShit(rcvBuf, w_out);
					} else {
						parseThatShit(rcvBuf, b_out);
					}
					
					
					
				} catch (Exception e) {
					log.error(" caught exception: " + e);
					log.info(" game is over due an untimely exception");
					gameOver = true;
				}
			}
		} else {
			log.info(" we have lost a player, and thus have no game");
			
			try {
				players[0].close();
				players[1].close();
			} catch (IOException e) {
				log.error(" caught an exception while shutting down");
			}
		}
		
		log.exit(sid);
	}
	
	private boolean hasDisconnected(byte[] rcvBuf, OutputStream sendersInput) throws Exception {
		if(rcvBuf.equals(ProtocolOperators.BYE)) {
			if(sendersInput == w_out) {
				b_out.write(ProtocolOperators.YOU_WIN);
			} else {
				w_out.write(ProtocolOperators.YOU_WIN);
			}
			state = GameServerState.GAME_OVER;
			return true;
		}
		return false;
	}
	
	private boolean hasConceded(byte[] rcvBuf, OutputStream sendersInput) throws Exception {
		if(rcvBuf.equals(ProtocolOperators.CONCEDE)) {
			if(sendersInput == w_out) {
				b_out.write(ProtocolOperators.YOU_WIN);
			} else {
				w_out.write(ProtocolOperators.YOU_WIN);
			}
			state = GameServerState.GAME_OVER;
			return true;
		}
		return false;
	}
	
	private void parseInStateRunning(byte[] rcvBuf, OutputStream sendersInput) {
		
	}
	
    private void parseInStateGameOver(byte[] rcvBuf, OutputStream sendersInput) {
		
	}
    
    private void parseInStateWaitingToStart(byte[] rcvBuf, OutputStream sendersInput) {
		
	}
	
	private void parseThatShit(byte[] rcvBuf, OutputStream sendersInput) throws Exception {

	public void run() {
		log.entry(sid);
		/*
		 * moveStone
		 * removeStone
		 *
		 */
		if(!hasDisconnected(rcvBuf, sendersInput)) {
			log.trace("player is still here");
			
			switch(state) {
			case GAME_OVER:
				if (rcvBuf.equals(ProtocolOperators.NEW_GAME)) {
					
				} else if (rcvBuf.equals(ProtocolOperators.NO_MORE)) {
					
				} else {
					sendersInput.write(ProtocolOperators.ILLEGAL_OP);
				}
				break;
			case MILL_CREATED:

				if(rcvBuf[0] == ProtocolOperators.BYE[0]) {
					if(sendersInput == w_out) {
						b_out.write(ProtocolOperators.YOU_WIN);
					} else {
						w_out.write(ProtocolOperators.YOU_WIN);
					}
					state = GameServerState.GAME_OVER;
				} else if (rcvBuf[0] == ProtocolOperators.REMOVE_STONE[0]) {
					logic.removeStone(rcvBuf[1]);
				} else if (rcvBuf[0] == ProtocolOperators.CONCEDE[0]) {
					// other guy wins
				} else {
					sendersInput.write(ProtocolOperators.ILLEGAL_OP);
				}
				break;
			case RUNNING:
				/*
				 * bye               0x040000
				 * moveStone         0x01{00 .. 11}{00 .. 17}
			     * removeStone       0x02{00 .. 11}00
			     * concede           0x030000
				 */
				if(rcvBuf[0] == ProtocolOperators.BYE[0]) {
					if(sendersInput == w_out) {
						b_out.write(ProtocolOperators.YOU_WIN);
					} else {
						w_out.write(ProtocolOperators.YOU_WIN);
					}
					state = GameServerState.GAME_OVER;
				} else if (rcvBuf[0] == ProtocolOperators.MOVE_STONE[0]) {
					logic.moveStone(rcvBuf[1], rcvBuf[2]);
				} else if (rcvBuf[0] == ProtocolOperators.REMOVE_STONE[0]) {
					logic.removeStone(rcvBuf[1]);
				} else if (rcvBuf[0] == ProtocolOperators.CONCEDE[0]) {
					// other guy wins
				} else {
					sendersInput.write(ProtocolOperators.ILLEGAL_OP);
				}
				break;
			case WAITING_TO_START:
				/*
				 * bye               0x040000
				 * concede           0x030000
				 */
				break;
			default:
				log.warn(" how the hell did this happen?!?");
				break;
			
			}
		}
		log.exit();
	}
	
	private boolean startGame(byte[] rcvBuf) {
		log.entry(rcvBuf[0], rcvBuf[1], rcvBuf[2]);
		boolean didItWork = false;
		
		try {
			log.trace("notifying client of colour");
			w_out.write(ProtocolOperators.IS_WHITE);
			w_in.readFully(rcvBuf);
			if(rcvBuf[0] != 0) {
				log.error("white does not ack, notifying black");
				b_out.write(ProtocolOperators.YOU_WIN);
				log.exit(didItWork);
				return didItWork;
			}
			b_out.write(ProtocolOperators.IS_BLACK);
			b_in.readFully(rcvBuf);
			if(rcvBuf[0] != 0) {
				log.error("black does not ack, notifying white");
				w_out.write(ProtocolOperators.YOU_WIN);
				log.exit(didItWork);
				return didItWork;
			}
		} catch (IOException e) {
			log.error("caught exception: " + e);
		}
		log.exit();
	}

}
