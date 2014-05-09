package de.uni_jena.min.in0043.nine_mens_morris.gui;

public class Stone{
	
	public int posX;
	public int posY;
	public int placedAt;
	public boolean placed;
	public boolean inPlacement;
	
	public Stone()
	{
		posX = 0;
		posY = 0;
		placedAt = -1;
		inPlacement = false;
	}
	
	protected void set(int pla)
	{
		posY -= pla;
	}
	
	protected void set(int pla, int bla)
	{
		posX = pla;
		posY = bla;
		
	}

}
