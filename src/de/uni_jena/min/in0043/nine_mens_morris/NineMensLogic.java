/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris;

/**
 * @author mariushk
 *
 */
public class NineMensLogic {
	// phase 01: place stones
	// phase 02: move one step to free point
	// phase 03: when less than three stones, move to any free point
	
	/*   0123456789ABC
	 * 0 X-----X-----X
	   1 |     |     |
	   2 | X---X---X |
	   3 | |   |   | |
	   4 | | X-X-X | |
	   5 | | |   | | |
	   6 X-X-X   X-X-X
	   7 | | |   | | |
	   8 | | X-X-X | |
	   9 | |   |   | |
	   A | X---X---X |
	   B |     |     |    
	   C X-----X-----X
	   
	 * 
	 * outer:  (0,0), (6,0), (C,0)
	 *         (0,6),        (C,6)
	 *         (0,C), (6,C)  (C,C)
	 * 
	 * middle: (2,2), (6,2), (A,2)
	 *         (2,6),        (A,6)
	 *         (A,2), (6,A), (A,A)
	 *         
	 * inner:  (4,4), (6,4), (8,4)
	 *         (4,6),        (8,6)
	 *         (4,8), (6,8), (8,8)
	 *
	     0123456
	   0 X--X--X
	   1 |X-X-X|
	   2 ||XXX||
	   3 XXX XXX
	   4 ||XXX||
	   5 |X-X-X|
	   6 X--X--X
	   
	 * outer:  (0,0), (3,0), (6,0)
	 *         (0,3),        (6,3)
	 *         (0,6), (3,6)  (6,6)
	 * 
	 * middle: (1,1), (3,1), (6,1)
	 *         (2,6),        (A,6)
	 *         (A,2), (6,A), (A,A)
	 *         
	 * inner:  (4,4), (6,4), (8,4)
	 *         (4,6),        (8,6)
	 *         (4,8), (6,8), (8,8)
	 *         
	 *         
	 * outer:  (0,0), (3,0), (6,0)
	 *         (0,3),        (6,3)
	 *         (0,6), (3,6)  (6,6)
	 * 
	 * middle: (1,1), (3,1), (6,1)
	 *         (2,6),        (A,6)
	 *         (A,2), (6,A), (A,A)
	 *         
	 * inner:  (4,4), (6,4), (8,4)
	 *         (4,6),        (8,6)
	 *         (4,8), (6,8), (8,8)
	 *         
	 *         phase               = {0,1,2}
	 *         countWhiteActivated = {0, ..., 9}
	 *         countWhiteInPlay    = {0, ..., 9}
	 *         countWhiteLost      = {0, ..., 9}
	 *         countBalckActivated = {0, ..., 9}
	 *         countBlackInPlay    = {0, ..., 9}
	 *         countBlackLost      = {0, ..., 9}
	 *         board
	 * */
}  
