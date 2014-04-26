/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.Init;
import de.uni_jena.min.in0043.nine_mens_morris.logic.NineMensBoard.Point;
import de.uni_jena.min.in0043.nine_mens_morris.logic.NineMensExceptions.*;

/**
 * @author mariushk
 *
 */
public class NineMensLogic {
	
	private static Logger log = LogManager.getLogger("EventLogger");
	private NineMensPhase phase;
	private NineMensBoard board;
	private NineMensStone[] stones = new NineMensStone[18];
	private NineMensStone lastMoved;
	private NineMensPlayer activePlayer;
	
	private int whiteActivated = 0; // {0, ..., 9}
	private int whiteInPlay = 0;    // {0, ..., 9}
	private int whiteLost = 0;      // {0, ..., 9}
	private int blackActivated = 0; // {0, ..., 9}
	private int blackInPlay = 0;    // {0, ..., 9}
	private int blackLost = 0;      // {0, ..., 9}
	private int round = 0;

	
	public NineMensLogic() {
		board = new NineMensBoard();
		phase = NineMensPhase.PLACING_STONES;
		activePlayer = NineMensPlayer.WHITE;
		NineMensStone.setBoard(board);
		for(int i = 0; i < 9; i++) {
			stones[i] = new NineMensStone(NineMensPlayer.WHITE);
			stones[i+9] = new NineMensStone(NineMensPlayer.BLACK);
		}
	}
	
	public NineMensPlayer getActivePlayer() {
		return activePlayer;
	}
	
	public NineMensPhase getPhase() {
		return phase;
	}
	
    public int getRound() { return round; }
	
    public int getWhiteActivated() {
		return whiteActivated;
	}

	public int getWhiteInPlay() {
		return whiteInPlay;
	}

	public int getWhiteLost() {
		return whiteLost;
	}

	public int getBlackActivated() {
		return blackActivated;
	}

	public int getBlackInPlay() {
		return blackInPlay;
	}

	public int getBlackLost() {
		return blackLost;
	}


	private void advancePhase() throws GameOver {
		switch (phase) {
			case PLACING_STONES: 
				if (whiteActivated == 9) phase = NineMensPhase.NORMAL_PLAY;       
				break;
			
			case NORMAL_PLAY: // 
				if (whiteLost == 7) {
					phase = NineMensPhase.WHITE_REDUCED;
				} else if (blackLost == 7) {
					phase = NineMensPhase.BLACK_REDUCED;
				}
				break;
			
			case WHITE_REDUCED: 
				if (blackLost == 6) { 
					phase = NineMensPhase.BOTH_REDUCED;
				} else if (blackLost == 9) {
					phase = NineMensPhase.GAME_OVER;
				}
				break;
			
			case BLACK_REDUCED:
				if (whiteLost == 6) { 
					phase = NineMensPhase.BOTH_REDUCED;
				} else if (whiteLost == 9) {
					phase = NineMensPhase.GAME_OVER;
				}
				break;
			
			case BOTH_REDUCED:
				if (whiteLost == 9 || blackLost == 9) phase = NineMensPhase.GAME_OVER;
			case GAME_OVER:
				throw new GameOver();
		}
	}
	
	public boolean advanceRound() throws GameOver {
		advancePhase();
		return true;
	}

    public boolean moveStone(int stone, int point) throws RulesViolated {
    	log.debug("entering moveStone()");
    	if (activePlayer != stones[stone].getOwner()) {
    		log.error("Rules violated");
    		throw new RulesViolated("It's the other player's turn!");
    	}
 
    	boolean moveOk;
    	
    	// not pretty...
    	if(phase == NineMensPhase.PLACING_STONES) moveOk = stones[stone].place(board.getPoint(point));
    	else moveOk = stones[stone].move(board.getPoint(point));
        
    	if(moveOk) {
    		log.debug("move was ok");
    		log.debug("Checking for newly created mills");
    		if(board.checkMills(stones[stone].getPoint())) {
    			log.debug("Mill found");   			
    		}
    		if(activePlayer == NineMensPlayer.WHITE) activePlayer = NineMensPlayer.BLACK;
    		else activePlayer = NineMensPlayer.WHITE;
    		log.debug("setting active user to " + activePlayer);
    			
    	} else {
    		log.debug("move was not ok");
    	}
    	log.debug("leaving moveStone()");
    	return moveOk;
    }
    
	
}