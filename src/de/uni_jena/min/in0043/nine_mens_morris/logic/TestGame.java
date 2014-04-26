package de.uni_jena.min.in0043.nine_mens_morris.logic;

import de.uni_jena.min.in0043.nine_mens_morris.logic.NineMensExceptions.RulesViolated;

public class TestGame {

	private static final NineMensLogic nml = new NineMensLogic();
	
	public static void main(String[] args) throws RulesViolated {
		nml.moveStone(0,3);
		nml.moveStone(9,12);
		
		nml.moveStone(1,2);
		nml.moveStone(10,20);
		
		nml.moveStone(2,0);
		nml.moveStone(11,21);
		
		nml.moveStone(3,1);
		nml.moveStone(12,23);
		
		nml.moveStone(4,22);
		nml.moveStone(13,6);
		
		nml.moveStone(0,3);
		nml.moveStone(5,14);
		nml.moveStone(14,9);
		
		nml.moveStone(6,11);
		nml.moveStone(15,16);
		
		nml.moveStone(7,4);
		nml.moveStone(16,15);
		
		nml.moveStone(8,18);
		nml.moveStone(17,17);
	}
}
