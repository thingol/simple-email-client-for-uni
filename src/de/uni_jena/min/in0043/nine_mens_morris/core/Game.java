/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris.core;

/**
 * @author marius
 *
 */
public interface Game {

	public Player getActivePlayer();
	
	public Phase getPhase();
	
    public int getRound();
	
    public int getWhiteActivated();

	public int getWhiteInPlay();

	public int getWhiteLost();

	public int getBlackActivated();

	public int getBlackInPlay();

	public int getBlackLost();
	
	public int moveStone(int stone, int point);
	
	public int removeStone(int stone);

}
