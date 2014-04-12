/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris;

/**
 * @author mariushk
 *
 */
public class NineMensLogic {
	private short phase = 0;          // {0,1,2,4,8,16}
	private short whiteActivated = 0; // {0, ..., 9}
	private short whiteInPlay = 0;    // {0, ..., 9}
	private short whiteLost = 0;      // {0, ..., 9}
	private short blackActivated = 0; // {0, ..., 9}
	private short blackInPlay = 0;    // {0, ..., 9}
	private short blackLost = 0;      // {0, ..., 9}
	// board
	
	public short getPhase() {
		return phase;
	}

	public short getWhiteActivated() {
		return whiteActivated;
	}

	public short getWhiteInPlay() {
		return whiteInPlay;
	}

	public short getWhiteLost() {
		return whiteLost;
	}

	public short getBlackActivated() {
		return blackActivated;
	}

	public short getBlackInPlay() {
		return blackInPlay;
	}

	public short getBlackLost() {
		return blackLost;
	}


	public void advancePhase() {
		switch (phase) {
			case 0: // nine stones for each player to place on the board 
				if (whiteActivated == 9) phase = 1; // both players are done with phase 0 in       
				break;
			
			case 1: // 
				if (whiteLost == 7) {
					phase = 2;
				} else if (blackLost == 7) {
					phase = 4;
				}
				break;
			
			case 2: // white in phase two
				if (blackLost == 7) phase = 8;
				break;
			
			case 4: // black in phase two
				if (whiteLost == 7) phase = 8;
				break;
			
			case 8:  // both in phase two
				if (whiteLost == 9 || blackLost == 9) phase = 16;
				break;
		}
	}
}  
