/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris;

import de.uni_jena.min.in0043.nine_mens_morris.NineMensExceptions.*;

/**
 * @author mariushk
 *
 */
public class NineMensLogic {
	
	private NineMensPhase phase;
	private NineMensBoard board;
	private NineMensStones[] stones = new NineMensStones[18];
	private NineMensStones lastMoved;
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
		NineMensStones.setBoard(board);
		for(int i = 0; i < 18;) {
			stones[i++] = new NineMensStones(NineMensPlayer.BLACK);
			stones[i] = new NineMensStones(NineMensPlayer.WHITE);
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


	public boolean advanceRound() throws GameOver {
		advancePhase();
		return true;
	}
	
	public boolean checkMills() {
		return true;
	}
	
    public boolean moveStone(NineMensPlayer player, int stone, int point) throws RulesViolated {
    	if (activePlayer != player) {
    		throw new RulesViolated("It's the other player's turn!");
    	}
    	
    	boolean moveOk = stones[stone].move(board.getPoint(point));
    	
    	if(moveOk) {
    		lastMoved = stones[stone];
    		checkMills();
    	} else {
    		lastMoved = null;
    	}
    	
    	return moveOk;
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

}