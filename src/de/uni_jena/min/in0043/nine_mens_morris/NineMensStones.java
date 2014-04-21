/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris;

import de.uni_jena.min.in0043.nine_mens_morris.NineMensBoard.Point;

/**
 * @author mariushk
 *
 */
public class NineMensStones {
	private static NineMensBoard board;
	private NineMensPlayer owner;
	private Point point;
	
	public NineMensStones(NineMensPlayer owner) {
		this.owner = owner;
	}
	
	public static void setBoard(NineMensBoard board) {
		NineMensStones.board = board;
	}
	
	public NineMensPlayer getOwner() { return owner; }
	public Point getPoint() { return point; }
	
	protected boolean move(Point p) {
		if(p.inUse() || !(board.oneStep(this.point, p))) {
			return false;
		}
		
		p.use();
		this.point = p;

		return true;		
	}

}
