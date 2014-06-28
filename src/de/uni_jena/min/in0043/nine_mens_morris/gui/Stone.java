package de.uni_jena.min.in0043.nine_mens_morris.gui;

public class Stone{
	
	private final int id;
	private int x;
	private int y;
	private int placedAt = -1;
	private boolean inPlacement = false;
	
	public Stone(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.setY(y);
	}
	
	public int getID() {
		return id;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean placed() {
		return placedAt != -1;
	}

	public int placedAt() {
		return placedAt;
	}

	public void placedAt(int placedAt) {
		this.placedAt = placedAt;
	}

	public boolean inPlacement() {
		return inPlacement;
	}

	public void inPlacement(boolean inPlacement) {
		this.inPlacement = inPlacement;
	}

	public void hide() {
		x = -50;
		y = -50;
		placedAt(-1);
	}
	
	public void mark() {
		y -= 5;
	}
	
	public void unMark() {
		y -= 5;
	}
	
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
		
	}
}
