/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.core.Board.Point;

/**
 * @author mariushk
 *
 */
public class Stone {
	private static Logger log = LogManager.getLogger();
	private static Board board;
	private Player owner;
	private Point point;
	
	public Stone(Player owner) {
		this.owner = owner;
	}
	
	public static void setBoard(Board board) {
		Stone.board = board;
	}
	
	public Player getOwner() { return owner; }
	public Point getPoint() { return point; }
	
	protected boolean canMove() {
		log.entry();
		boolean retVal = false;
		if(board.hasUnoccupiedNeighbour(point)) {
			retVal = true;
		}
		log.exit(retVal);
		
		return retVal;
		
	}
	
	protected boolean move(Point p) {
		log.entry(p);
		if(p.isOccupied() || !(board.oneStep(this.point, p))) {
			log.error("stone could not be moved");
			log.exit(false);
			return false;
		}
		
		this.point.withdraw();
		p.occupy(this);
		this.point = p;

		log.trace("stone moved");
		log.exit(true);
		return true;		
	}
	
	protected boolean place(Point p) {
		log.entry(p);
		if(p.isOccupied() || point != null) {
			log.error("stone could not be placed");
			log.exit(false);
			return false;
		}
		
		p.occupy(this);
		this.point = p;

		log.trace("stone placed");
		log.exit(true);
		return true;		
	}
	
	protected void remove() {
		log.entry();
		this.point.withdraw();
		this.point = null;
		log.exit();
	}
}
