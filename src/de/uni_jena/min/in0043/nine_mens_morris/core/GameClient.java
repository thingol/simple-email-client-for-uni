package de.uni_jena.min.in0043.nine_mens_morris.core;

public interface GameClient {

	public void moveStone(int stone, int goal);
	
	public boolean newGame(boolean didIWin);
	
	public void noMore();
	
	public void removeStone(int stone);
	
	public void reset();
	

}