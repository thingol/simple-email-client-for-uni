package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import de.uni_jena.min.in0043.nine_mens_morris.core.Logic;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;

public class GameServer extends Thread {

	private Logic logic;
	private Socket[] players = new Socket[2];
	private DataInputStream w_in, b_in;
	private OutputStream w_out, b_out;

	public GameServer(Socket player0, Socket player1) {
		logic = new Logic();
		
		// gotta be random who gets to be which colour
		int ran = (int)Math.random();
		
		players[1 - ran] = player0;
		players[ran] = player1;
	}

	public void run() {
		/*
		 * moveStone
		 * removeStone
		 *
		 */
		startGame();
	}
	
	private void startGame() {
		
		try {
			w_out.write(Player.WHITE.ordinal());
			b_out.write(Player.BLACK.ordinal());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
