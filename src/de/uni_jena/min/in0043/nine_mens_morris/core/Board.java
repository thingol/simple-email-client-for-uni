/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris.core;

import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Pseudograph;

/**
 * @author mariushk
 *
 */
public class Board {

	class Point {
		private int id;
		private Stone stone;
		
		
		public Point(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}
		
		public Stone getStone() {
			return stone;
		}
		
		public String toString() {
			return "ID: " + id; 
		}
		
		
		public boolean isOccupied() { 
			return stone != null;
		}
		
		public void occupy(Stone stone) {
			this.stone = stone;
		}
		
	}
	
	private static Logger log = LogManager.getLogger();
	
	private Pseudograph<Point, DefaultEdge> boardGraph =
			new Pseudograph<Board.Point, DefaultEdge>(DefaultEdge.class);
	
	private ArrayList<LinkedList<Point>> mills = new ArrayList<LinkedList<Point>>(16);
	private Point[] points = new Point[24];
	
	@SuppressWarnings("unchecked")
	public Board() {
		log.entry();
		
		log.trace("creating points");
		for(int i = 0; i < 24; i++) {
			points[i] = new Point(i);
			boardGraph.addVertex(points[i]);
		}
		log.trace("done");
		
		// setting up the board
		log.trace("creating graph of board");
		boardGraph.addEdge(points[0], points[1]);
		boardGraph.addEdge(points[0], points[9]);

		boardGraph.addEdge(points[1], points[2]);
		boardGraph.addEdge(points[1], points[4]);
		
		boardGraph.addEdge(points[2], points[14]);
		
		boardGraph.addEdge(points[3], points[4]);
		boardGraph.addEdge(points[3], points[10]);
		
		boardGraph.addEdge(points[4], points[5]);
		boardGraph.addEdge(points[4], points[7]);
		
		boardGraph.addEdge(points[5], points[13]);
		
		boardGraph.addEdge(points[6], points[7]);
		boardGraph.addEdge(points[6], points[11]);
		
		boardGraph.addEdge(points[7], points[8]);
		
		boardGraph.addEdge(points[8], points[12]);
		
		boardGraph.addEdge(points[9], points[10]);
		boardGraph.addEdge(points[9], points[21]);
		
		boardGraph.addEdge(points[10], points[11]);
		boardGraph.addEdge(points[10], points[18]);
		
		boardGraph.addEdge(points[11], points[15]);
		
		boardGraph.addEdge(points[12], points[13]);
		boardGraph.addEdge(points[12], points[17]);
		
		boardGraph.addEdge(points[13], points[14]);
		boardGraph.addEdge(points[13], points[20]);
		
		boardGraph.addEdge(points[14], points[23]);
		
		boardGraph.addEdge(points[15], points[16]);
		
		boardGraph.addEdge(points[16], points[17]);
		boardGraph.addEdge(points[16], points[19]);
		
		boardGraph.addEdge(points[18], points[19]);

		boardGraph.addEdge(points[19], points[20]);
		boardGraph.addEdge(points[19], points[22]);
		
		boardGraph.addEdge(points[21], points[22]);
		
		boardGraph.addEdge(points[22], points[23]);
		log.trace("done");
		
		// setting up the possible mills for easy checking
		log.trace("setting up structures for easy checking of mills");
		LinkedList<Point> lst = new LinkedList<Point>();
		lst.add(points[0]);
		lst.add(points[1]);
		lst.add(points[2]);
		mills.add((LinkedList<Point>)lst.clone());

		lst.clear();
		lst.add(points[3]);
		lst.add(points[4]);
		lst.add(points[5]);
		mills.add((LinkedList<Point>)lst.clone());

		lst.clear();
		lst.add(points[6]);
		lst.add(points[7]);
		lst.add(points[8]);
		mills.add((LinkedList<Point>)lst.clone());

		lst.clear();
		lst.add(points[9]);
		lst.add(points[10]);
		lst.add(points[11]);
		mills.add((LinkedList<Point>)lst.clone());

		lst.clear();
		lst.add(points[12]);
		lst.add(points[13]);
		lst.add(points[14]);
		mills.add((LinkedList<Point>)lst.clone());

		lst.clear();
		lst.add(points[15]);
		lst.add(points[16]);
		lst.add(points[17]);
		mills.add((LinkedList<Point>)lst.clone());

		lst.clear();
		lst.add(points[18]);
		lst.add(points[19]);
		lst.add(points[20]);
		mills.add((LinkedList<Point>)lst.clone());

		lst.clear();
		lst.add(points[21]);
		lst.add(points[22]);
		lst.add(points[23]);
		mills.add((LinkedList<Point>)lst.clone());

		lst.clear();
		lst.add(points[0]);
		lst.add(points[9]);
		lst.add(points[21]);
		mills.add((LinkedList<Point>)lst.clone());

		lst.clear();
		lst.add(points[3]);
		lst.add(points[10]);
		lst.add(points[18]);
		mills.add((LinkedList<Point>)lst.clone());

		lst.clear();
		lst.add(points[6]);
		lst.add(points[11]);
		lst.add(points[15]);
		mills.add((LinkedList<Point>)lst.clone());

		lst.clear();
		lst.add(points[1]);
		lst.add(points[4]);
		lst.add(points[7]);
		mills.add((LinkedList<Point>)lst.clone());

		lst.clear();
		lst.add(points[16]);
		lst.add(points[19]);
		lst.add(points[22]);
		mills.add((LinkedList<Point>)lst.clone());

		lst.clear();
		lst.add(points[8]);
		lst.add(points[12]);
		lst.add(points[17]);
		mills.add((LinkedList<Point>)lst.clone());

		lst.clear();
		lst.add(points[5]);
		lst.add(points[13]);
		lst.add(points[20]);
		mills.add((LinkedList<Point>)lst.clone());

		lst.clear();
		lst.add(points[2]);
		lst.add(points[14]);
		lst.add(points[23]);
		mills.add((LinkedList<Point>)lst.clone());
		log.trace("done");
		log.exit();
	}
	
	public boolean oneStep(Point p1, Point p2) {
		log.entry();
		log.trace("checking distance between points " + p1 + " and " + p2);
		if(boardGraph.getEdge(p1, p2) != null) {
			log.exit(true);
			return true;
		} else {
			log.exit(false);
			return false;
		}
	}
	
	public Point getPoint(int id) {
		return points[id];
	}
	
	public boolean checkMills(Point point) {
		
		Player owner = point.getStone().getOwner();
		for(LinkedList<Point> l : mills) {
			
			if(l.contains(point)) {
				boolean gotMill = true;
				for(Point p : l) {
					if(p.getStone() == null || p.getStone().getOwner() != owner) {
						gotMill = false;
						break;
					}
				}
				
				if(gotMill) return true;
			}		
		}
		return false;
	}
}
