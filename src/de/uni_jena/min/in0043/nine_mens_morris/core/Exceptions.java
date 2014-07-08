/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris.core;

/**
 * @author mariushk
 *
 */
public class Exceptions {
	
	public static class GameOver extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public GameOver() {
			super("The game is over. D'oh!");
		}

	}
	
	public static class RulesViolated extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public RulesViolated(String msg) {
			super(msg);
		}

	}

}
