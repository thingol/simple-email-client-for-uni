/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris;

import java.util.Iterator;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.traverse.DepthFirstIterator;;

/**
 * @author mariushk
 *
 */
public class NineMensBoard {

	class Point {
		private int id;
		private boolean inUse = false;
		
		public Point(int id) {
			this.id = id;
		}
		
		public String toString() {
			return "ID: " + id; 
		}
		
		public boolean inUse() { 
			return inUse;
		}
		
		public void use() {
			inUse = true;
		}
		
	}
	
	private Pseudograph<Point, DefaultEdge> boardGraph =
			new Pseudograph<NineMensBoard.Point, DefaultEdge>(DefaultEdge.class);
	
	private Point[] points = new Point[24];
	
	public NineMensBoard() {
		
		for(int i = 0; i < 24; i++) {
			points[i] = new Point(i);
			boardGraph.addVertex(points[i]);
		}
		
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

	}
	
	public boolean oneStep(Point p1, Point p2) {
		if(boardGraph.getEdge(p1, p2) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public Point getPoint(int id) {
		return points[id];
	}
	
	public void printBG() {
		Iterator<Point> iter =
	            new DepthFirstIterator<Point, DefaultEdge>(boardGraph);
	        Point vertex;
	        while (iter.hasNext()) {
	            vertex = iter.next();
	            System.out.println(
	                "Vertex " + vertex.toString() + " is connected to: "
	                + boardGraph.edgesOf(vertex).toString());
	        }
	}
}
