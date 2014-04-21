/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris;

/**
 * @author mariushk
 *
 */
public class NineMensExceptions {
	
	static class GameOver extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public GameOver() {
			super("The game is over. D'oh!");
		}

	}
	
	static class RulesViolated extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public RulesViolated(String msg) {
			super(msg);
		}

	}

}
