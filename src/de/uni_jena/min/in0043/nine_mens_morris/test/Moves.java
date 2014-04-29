package de.uni_jena.min.in0043.nine_mens_morris.test;
/**
 * 
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.uni_jena.min.in0043.nine_mens_morris.core.Exceptions.RulesViolated;
import de.uni_jena.min.in0043.nine_mens_morris.core.Logic;

/**
 * @author mariushk
 *
 */
public class Moves {
	private static Logic nml;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		nml = new Logic();
	}

	@Test
	public void test() throws RulesViolated {
		assertEquals("nml.moveStone(0,3)   => 1",1,nml.moveStone(0,3));
		assertEquals("nml.moveStone(9,12)  => 1",1,nml.moveStone(9,12));
		
		assertEquals("nml.moveStone(1,2)   => 1",1,nml.moveStone(1,2));
		assertEquals("nml.moveStone(10,20) => 1",1,nml.moveStone(10,20));
		
		assertEquals("nml.moveStone(2,0)   => 1",1,nml.moveStone(2,0));
		assertEquals("nml.moveStone(11,21) => 1",1,nml.moveStone(11,21));
		
		assertEquals("nml.moveStone(3,1)   => 2",2,nml.moveStone(3,1));
		assertTrue("nml.removeStone(11)    => true", nml.removeStone(11));
		assertEquals("nml.moveStone(12,23) => 1",1,nml.moveStone(12,23));
		
		assertEquals("nml.moveStone(4,22)  => 1",1,nml.moveStone(4,22));
		assertEquals("nml.moveStone(13,6)  => 1",1,nml.moveStone(13,6));
		
		assertEquals("nml.moveStone(0,3)   => 0",0,nml.moveStone(0,3));
		assertEquals("nml.moveStone(13,6)  => 0",0,nml.moveStone(13,6));
		assertEquals("nml.moveStone(5,14)  => 1",1,nml.moveStone(5,14));
		assertFalse("nml.removeStone(11)    => false", nml.removeStone(13));
		assertEquals("nml.moveStone(14,9)  => 1",1,nml.moveStone(14,9));
		
		assertEquals("nml.moveStone(6,11)  => 1",1,nml.moveStone(6,11));
		assertEquals("nml.moveStone(15,16) => 1",1,nml.moveStone(15,16));
		
		assertEquals("nml.moveStone(7,4)   => 1",1,nml.moveStone(7,4));
		assertEquals("nml.moveStone(16,15) => 1",1,nml.moveStone(16,15));
		
		assertEquals("nml.moveStone(8,18)  => 1",1,nml.moveStone(8,18));
		assertEquals("nml.moveStone(17,17) => 2",2,nml.moveStone(17,17));
		assertTrue("nml.removeStone(8)    => true", nml.removeStone(8));
		}

}
