package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.core.Logic;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;

public class GameServer extends Thread {

	private static Logger log = LogManager.getLogger();
	private static Random rg = new Random(); 
	private Logic logic;
	private Socket[] players = new Socket[2];
	private DataInputStream w_in, b_in;
	private OutputStream w_out, b_out;
	private long sid;
	private GameServerState state = GameServerState.WAITING_TO_START;

	public GameServer(long sid, Socket player0, Socket player1) {
		log.entry(sid, player0, player1);
		logic = new Logic();
		this.sid = sid;
		
		players[0] = player0;
		players[1] = player1;
		
		// gotta be random who gets to be which colour		
		int ran = rg.nextInt(2);
		
		log.trace(sid + " assigning players to colours and getting network streams");
		log.trace(sid + " player[" + (1 - ran) + "] is white");
		try {
			w_in = new DataInputStream(new BufferedInputStream(players[1 - ran].getInputStream()));
			w_out = new DataOutputStream(players[1 - ran].getOutputStream());
			b_in = new DataInputStream(new BufferedInputStream(players[ran].getInputStream()));
			b_out = new DataOutputStream(players[ran].getOutputStream());
		} catch (IOException e) {
			log.error(sid + "caught IOException when assigning colours");
			e.printStackTrace();
		}
		log.exit(sid);
	}

	public void run() {
		log.entry(sid);
		
		boolean gameOver = false;
		byte[] rcvBuf = new byte[3];
		
		if(startGame(rcvBuf)) {
			while(!gameOver) {
				
				try {
					if(logic.getActivePlayer() == Player.WHITE) {
						parseThatShit(rcvBuf, w_out);
					} else {
						parseThatShit(rcvBuf, b_out);
					}
					
					
					
				} catch (Exception e) {
					log.error(sid + " caught exception: " + e);
					log.info(sid + " game is over due an untimely exception");
					gameOver = true;
				}
			}
		} else {
			log.info(sid + " we have lost a player, and thus have no game");
			
			try {
				players[0].close();
				players[1].close();
			} catch (IOException e) {
				log.error(sid + " caught an exception while shutting down");
			}
		}
		
		log.exit(sid);
	}
	
	private void parseThatShit(byte[] rcvBuf, OutputStream sendersInput) throws Exception {

		/*
		 * w = waiting to start
		 * r = running
		 * at = any time
		 * go = game over
		 * 
		 * r   moveStone         0x01{00 .. 11}{00 .. 17}
		 * r   removeStone       0x02{00 .. 11}00
		 * !go concede           0x030000
		 * at  bye               0x040000
		 * r   getPhase          0x050000
		 * r   getActivePlayer   0x060000
		 * r   getWhiteActivated 0x070000
		 * r   whiteInPlay       0x080000
		 * r   whiteLost         0x090000
		 * r   getBlackActivated 0x0A0000
		 * r   blackInPlay       0x0B0000
		 * r   blackLost         0x0C0000
		 * r   getRound          0x0D0000
		 * go  NEW_GAME          0x100000
		 * go  NO_MORE           0x110000
		 */
		
		switch(state) {
		case GAME_OVER:
			if(rcvBuf.equals(ProtocolOperators.BYE)) {
				if(sendersInput == w_out) {
					b_out.write(ProtocolOperators.YOU_WIN);
				} else {
					w_out.write(ProtocolOperators.YOU_WIN);
				}
				state = GameServerState.GAME_OVER;
			} else if (rcvBuf.equals(ProtocolOperators.NEW_GAME)) {
				
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
			log.warn(sid + " how the hell did this happen?!?");
			break;
		
		}
	}
	
	private boolean startGame(byte[] rcvBuf) {
		log.entry(rcvBuf);
		boolean didItWork = false;
		
		try {
			log.trace(sid + " notifying client of colour");
			w_out.write(new byte[]{12,0,0});
			w_in.readFully(rcvBuf);
			if(rcvBuf[0] != 0) {
				log.error(sid + " white does not ack, notifying black");
				b_out.write(ProtocolOperators.YOU_WIN);
				log.exit(didItWork);
				return didItWork;
			}
			b_out.write(new byte[]{13,0,0});
			b_in.readFully(rcvBuf);
			if(rcvBuf[0] != 0) {
				log.error(sid + " black does not ack, notifying white");
				w_out.write(ProtocolOperators.YOU_WIN);
				log.exit(didItWork);
				return didItWork;
			}
		} catch (IOException e) {
			log.error(sid + " caught exception: " + e);
		}
		
		state = GameServerState.RUNNING;
		
		didItWork = true;
		log.exit(didItWork);
		return didItWork;
	}

}
