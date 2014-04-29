/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author mariushk
 *
 */
public class Logic {
	
	private static Logger log = LogManager.getLogger();
	private Phase phase;
	private Board board;
	private Stone[] stones = new Stone[18];
	private Player activePlayer;
	private boolean removeStone = false;
	
	private int whiteActivated = 0; // {0, ..., 9}
	private int whiteInPlay = 0;    // {0, ..., 9}
	private int whiteLost = 0;      // {0, ..., 9}
	private int blackActivated = 0; // {0, ..., 9}
	private int blackInPlay = 0;    // {0, ..., 9}
	private int blackLost = 0;      // {0, ..., 9}
	private int round = 0;

	
	public Logic() {
		board = new Board();
		phase = Phase.PLACING_STONES;
		activePlayer = Player.WHITE;
		Stone.setBoard(board);
		for(int i = 0; i < 9; i++) {
			stones[i] = new Stone(Player.WHITE);
			stones[i+9] = new Stone(Player.BLACK);
		}
	}
	
	public Player getActivePlayer() { return activePlayer;	}
	
	public Phase getPhase() { return phase;	}
	
    public int getRound() { return round; }
	
    public int getWhiteActivated() { return whiteActivated;	}

	public int getWhiteInPlay() { return whiteInPlay; }

	public int getWhiteLost() {	return whiteLost; }

	public int getBlackActivated() { return blackActivated;	}

	public int getBlackInPlay() { return blackInPlay; }

	public int getBlackLost() {	return blackLost; }


	private void advancePhase() {
		log.entry();
		switch (phase) {
			case PLACING_STONES: 
				if (whiteActivated == 9) {
					phase = Phase.NORMAL_PLAY;
				}
				break;
			
			case NORMAL_PLAY:
				if (whiteLost == 7) {
					phase = Phase.WHITE_REDUCED;
				} else if (blackLost == 7) {
					phase = Phase.BLACK_REDUCED;
				}
				break;
			
			case WHITE_REDUCED: 
				if (blackLost == 6) { 
					phase = Phase.BOTH_REDUCED;
				} else if (blackLost == 9) {
					phase = Phase.GAME_OVER;
				}
				break;
			
			case BLACK_REDUCED:
				if (whiteLost == 6) { 
					phase = Phase.BOTH_REDUCED;
				} else if (whiteLost == 9) {
					phase = Phase.GAME_OVER;
				}
				break;
			
			case BOTH_REDUCED:
				if (whiteLost == 9 || blackLost == 9) phase = Phase.GAME_OVER;
				break;

			case GAME_OVER:
				log.error("Game is over!");
		}
		log.info("Moving to phase " + phase);
		log.exit();
	}
	
	public boolean advanceRound() {
		log.entry();
		round++;
		advancePhase();
		log.exit();
		return true;
	}

    public int moveStone(int stone, int point) {
    	log.entry();
        // -1 = stone must be removed first 
        //  0 = illegal move
        //  1 = legal move
        //  2 = legal move resulting in mill
    	int retVal = 0;
    	Stone st = stones[stone];
    	
        if (removeStone) {
    		log.error(activePlayer + " must remove a stone before next round can start!");
    		retVal = -1;
    		log.exit(retVal);
    		return retVal;
    	} else if (activePlayer != st.getOwner()) {
    		log.error(activePlayer + " attempted to move " + st.getOwner() + "'s stone");
    		log.exit(retVal);
    		return retVal;
    	}
    	
    	if(phase == Phase.PLACING_STONES) {
    		retVal = (stones[stone].place(board.getPoint(point))) ? 1 : 0;
    		if(retVal != 0)  {
    			if(activePlayer == Player.BLACK) {
    				blackActivated++;
    				log.trace("blackActivated => " + blackActivated);
    			} else {
    				whiteActivated++;
    				log.trace("whiteActivated => " + whiteActivated);
    			}
    		}
    	} else {
    		retVal = (stones[stone].move(board.getPoint(point))) ? 1 : 0;
    	}
        
    	if(retVal == 1) {
    		log.info("move was ok, stone " + stone + " to point " + point);
    		log.trace("Checking for newly created mills");
    		if(board.checkMills(stones[stone].getPoint())) {
    			log.info("Mill found"); 
    			removeStone = true;
    			retVal++;
    		} else {
    			log.info("Mill not found");
    			if(activePlayer == Player.WHITE) activePlayer = Player.BLACK;
        		else {
        			activePlayer = Player.WHITE;
        			advanceRound();
        		}
        		log.trace("setting active user to " + activePlayer);
    		}
    		
    			
    	} else {
    		log.warn("move was not ok, stone " + stone + " to point " + point);
    	}
    	log.exit(retVal);
    	return retVal;
    }
    
	public boolean removeStone(int stone) {
    	log.entry(stone);
    	if(!removeStone) {
    		log.error(activePlayer + " attempted to remove stone when no mill created");
    		log.exit(false);
    		return false;
    	}
    	Stone st = stones[stone];
    	
    	if (activePlayer == st.getOwner()) {
    		log.error(activePlayer + " attempted to remove their own stone");
    		log.exit(false);
    		return false;
    	} else if(board.checkMills(stones[stone].getPoint())) {
			log.error("stone is part of a mill");
			log.exit(false);
			return false;
		} else {
			log.info("removing stone");
			st.remove();
			removeStone = false;
			if(activePlayer == Player.WHITE) activePlayer = Player.BLACK;
    		else {
    			activePlayer = Player.WHITE;
    			advanceRound();
    		}
    		log.trace("setting active user to " + activePlayer);
			log.exit(true);
			return true;
		}
    }
}
