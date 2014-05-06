package de.uni_jena.min.in0043.nine_mens_morris.gui;

public class Stone{
	
	public int posX;
	public int posY;
	public int placedAt;
	public boolean placed;
	public boolean inPlacement;
	public boolean now;
	
	public Stone()
	{
		posX = 0;
		posY = 0;
		placedAt = -1;
		inPlacement = false;
	}
	
	public void set()
	{
		if(inPlacement)
		{posY -= 1;}
		if(placedAt == -2)
		{posX = -50;
		 posY = -50;
		}
	}

}
