/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris.logic;

import de.uni_jena.min.in0043.nine_mens_morris.logic.NineMensBoard.Point;

/**
 * @author mariushk
 *
 */
public class NineMensStone {
	private static NineMensBoard board;
	private NineMensPlayer owner;
	private Point point;
	
	public NineMensStone(NineMensPlayer owner) {
		this.owner = owner;
	}
	
	public static void setBoard(NineMensBoard board) {
		NineMensStone.board = board;
	}
	
	public NineMensPlayer getOwner() { return owner; }
	public Point getPoint() { return point; }
	
	protected boolean move(Point p) {
		if(p.isOccupied() || !(board.oneStep(this.point, p))) {
			return false;
		}
		
		p.occupy(this);
		this.point = p;

		return true;		
	}
	
	protected boolean place(Point p) {
		if(p.isOccupied() || point != null) {
			return false;
		}
		
		p.occupy(this);
		this.point = p;

		return true;		
	}

}
