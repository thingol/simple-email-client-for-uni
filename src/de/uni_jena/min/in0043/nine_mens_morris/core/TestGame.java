package de.uni_jena.min.in0043.nine_mens_morris.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.core.Exceptions.RulesViolated;

public class TestGame {

	private static Logger log = LogManager.getLogger();
	private static Logic nml;
	
	public static void main(String[] args) throws RulesViolated {
		log.trace("Starting test game");
		nml = new Logic();
		
		System.out.println("nml.moveStone(0,3)   => " + nml.moveStone(0,3));
		System.out.println("nml.moveStone(9,12)  => " + nml.moveStone(9,12));
		
		System.out.println("nml.moveStone(1,2)   => " + nml.moveStone(1,2));
		System.out.println("nml.moveStone(10,20) => " + nml.moveStone(10,20));
		
		System.out.println("nml.moveStone(2,0)   => " + nml.moveStone(2,0));
		System.out.println("nml.moveStone(11,21) => " + nml.moveStone(11,21));
		
		System.out.println("nml.moveStone(3,1)   => " + nml.moveStone(3,1));
		System.out.println("nml.moveStone(12,23) => " + nml.moveStone(12,23));
		
		System.out.println("nml.moveStone(4,22)  => " + nml.moveStone(4,22));
		System.out.println("nml.moveStone(13,6)  => " + nml.moveStone(13,6));
		
		System.out.println("nml.moveStone(0,3)   => " + nml.moveStone(0,3));
		System.out.println("nml.moveStone(13,6)  => " + nml.moveStone(13,6));
		System.out.println("nml.moveStone(5,14)  => " + nml.moveStone(5,14));
		System.out.println("nml.moveStone(14,9)  => " + nml.moveStone(14,9));
		
		System.out.println("nml.moveStone(6,11)  => " + nml.moveStone(6,11));
		System.out.println("nml.moveStone(15,16) => " + nml.moveStone(15,16));
		
		System.out.println("nml.moveStone(7,4)   => " + nml.moveStone(7,4));
		System.out.println("nml.moveStone(16,15) => " + nml.moveStone(16,15));
		
		System.out.println("nml.moveStone(8,18)  => " + nml.moveStone(8,18));
		System.out.println("nml.moveStone(17,17) => " + nml.moveStone(17,17));
		log.trace("Test game complete");
	}
}
