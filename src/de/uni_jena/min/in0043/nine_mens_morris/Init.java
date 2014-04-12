/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author mariushk
 *
 */
public class Init {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Frame myFrame = new Frame();
		myFrame.add(new Spielfeld());
		myFrame.setSize(800,800);
		myFrame.setVisible(true);
			
		myFrame.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent we){
			    System.exit(0);
			  }
		});
	}
}
