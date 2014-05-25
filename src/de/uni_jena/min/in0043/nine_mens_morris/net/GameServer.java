package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.net.Socket;

import de.uni_jena.min.in0043.nine_mens_morris.core.Logic;

public class GameServer extends Thread {

	private Logic logic;

	public GameServer(Socket accept) {
		logic = new Logic();
	}

	public void run() {
		/*
		 * moveStone
		 * removeStone
		 *
		 */
	}

}
