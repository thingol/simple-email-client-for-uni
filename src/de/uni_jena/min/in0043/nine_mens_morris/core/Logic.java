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
		log.entry("round: " + round + ", phase: " + phase);
		Phase oldPhase = phase;
		switch (phase) {
			case PLACING_STONES:
				log.trace("whiteActivated: " + whiteActivated + ", blackActivated: " + blackActivated);
				if (blackActivated == 9) {
					phase = Phase.NORMAL_PLAY;
				}
				break;
			
			case NORMAL_PLAY:
				log.trace("whiteLost: " + whiteLost + ", blackLost: " + blackLost);
				if (whiteLost == 6) {
					phase = Phase.WHITE_REDUCED;
				} else if (blackLost == 6) {
					phase = Phase.BLACK_REDUCED;
				}
				break;
			
			case WHITE_REDUCED:
				log.trace("blackLost: " + blackLost);
				if (blackLost == 6) { 
					phase = Phase.BOTH_REDUCED;
				} else if (whiteLost == 7) {
					phase = Phase.GAME_OVER;
				}
				break;
			
			case BLACK_REDUCED:
				log.trace("whiteLost: " + whiteLost);
				if (whiteLost == 6) { 
					phase = Phase.BOTH_REDUCED;
				} else if (blackLost == 7) {
					phase = Phase.GAME_OVER;
				}
				break;
			
			case BOTH_REDUCED:
				if (whiteLost == 7 || blackLost == 7) phase = Phase.GAME_OVER;
				break;

			case GAME_OVER:
				log.error("Game is over!");

		}
		if(phase == oldPhase) {
			log.info("Phase is " + phase);
		} else {
			log.info("Moving to phase " + phase);
		}
		log.exit();
	}
	
	private void advanceRound() {
		log.entry();
		round++;
		advancePhase();
		log.exit();
	}

	private boolean movingStillPossible() {
		log.entry();
		if(phase == Phase.PLACING_STONES) {
			log.trace("still placing stones, so yes");
			log.exit(true);
			return true;
		}
		int i;
		int upperBound;

		if(activePlayer == Player.WHITE) {
			i = 0;
			upperBound = 9;
		} else {
			i = 9;
			upperBound = 18;
		}

		for(; i < upperBound; i++) {
			log.trace("checking motility of stone " + stones[i]);
			if(stones[i].canMove()) {
				log.exit(true);
				return true;
			}
		}

		log.exit(false);
		return false;
	}

    public int moveStone(int stone, int point) {
    	log.entry();
    	// -2 = game is over
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
    			
    			if(activePlayer == Player.WHITE) {
    				activePlayer = Player.BLACK;
    				advancePhase();
    			} else {
        			activePlayer = Player.WHITE;
        			advanceRound();
    			}
    			log.trace("active player set to " + activePlayer);
    			
    			// UGLY
    			log.trace("checking if " + activePlayer + " can still move");
    			if(!movingStillPossible()) {
    				// game is over
    				// jumping directly to phase GAME_OVER
    				phase = Phase.GAME_OVER;
    				log.info("Game is over");
   					retVal = -2;
    				log.info(activePlayer + " lost");
    			}
    			// UGLY
    		}
    	} else {
    		log.warn("move was not ok: stone " + stone + " to point " + point);
    	}
    	log.exit(retVal);
    	return retVal;
    }
    
	public int removeStone(int stone) {
		// -2 = game is over
        //  0 = stone can not be removed
        //  1 = stone removed
		int retVal = 0;
    	log.entry(stone);
    	if(!removeStone) {
    		log.error(activePlayer + " attempted to remove stone when no mill created");
    		log.exit(retVal);
    		return retVal;
    	}
    	Stone st = stones[stone];
    	
    	if (activePlayer == st.getOwner()) {
    		log.error(activePlayer + " attempted to remove their own stone");
    	} else if(board.checkMills(stones[stone].getPoint())) {
			log.error("stone is part of a mill");
		} else {
			log.info("removing stone");
			st.remove();
			removeStone = false;
			retVal = 1;

			if(activePlayer == Player.WHITE) {
				activePlayer = Player.BLACK;
				blackLost++;
				advancePhase();
			} else {
    			activePlayer = Player.WHITE;
                whiteLost++;
    			advanceRound();
    		}
			log.trace("active player set to " + activePlayer);
			
			// UGLY
			log.trace("checking if " + activePlayer + " can still move");
			if(!movingStillPossible()) {
				// game is over
				// jumping directly to phase GAME_OVER
				phase = Phase.GAME_OVER;
				log.info("Game is over, " + activePlayer + " lost");
				retVal = -2;
			}
			// UGLY
		}

    	log.exit(retVal);
    	return retVal;
    }
}
