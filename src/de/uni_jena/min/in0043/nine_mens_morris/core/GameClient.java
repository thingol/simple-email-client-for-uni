package de.uni_jena.min.in0043.nine_mens_morris.core;

public interface GameClient {

	public void reset();
	
	public void moveStone(int stone, int goal);
	
	public void removeStone(int stone);
	
	public boolean newGame(boolean didIWin);
	
	public void noMore();
	
	public void setColour(Player colour);
	
	public void youWon(boolean loserGone);
	

}