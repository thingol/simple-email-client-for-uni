package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.core.Logic;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;

public class GameServer extends Thread {

	private static Logger log = LogManager.getLogger();
	private Logic logic;
	private Socket[] players = new Socket[2];
	private DataInputStream w_in, b_in;
	private OutputStream w_out, b_out;
	private static byte[] blurb = {72,69,76,76,79,33,10};
	private long sid;

	public GameServer(long sid, Socket player0, Socket player1) {
		log.entry(sid, player0, player1);
		logic = new Logic();
		this.sid = sid;
		
		players[0] = player0;
		players[1] = player1;
		
		// gotta be random who gets to be which colour		
		int ran = (int)Math.random();
		
		log.trace(sid + " assigning players to colours and getting network streams");
		log.trace(sid + "player[" + (1 - ran) + "] is white");
		try {
			w_in = new DataInputStream(new BufferedInputStream(players[1 - ran].getInputStream()));
			w_out = new DataOutputStream(players[1 - ran].getOutputStream());
			b_in = new DataInputStream(new BufferedInputStream(players[ran].getInputStream()));
			b_out = new DataOutputStream(players[ran].getOutputStream());
		} catch (IOException e) {
			log.error(sid + "caught IOException when assigning colours");
			e.printStackTrace();
		}
		
		
		
		log.exit(sid);
	}

	public void run() {
		log.entry(sid);
		/*
		 * moveStone
		 * removeStone
		 *
		 */
		startGame();
		
		try {
			log.trace(sid + " closing player sockets");
			players[0].close();
			players[1].close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.exit();
	}
	
	private void startGame() {
		log.entry();
		
		try {
			log.trace("notifying client of colour");
			log.trace("Player.WHITE.ordinal() => " + Player.WHITE.ordinal());
			w_out.write(blurb); //Player.WHITE.ordinal());
			b_out.write(blurb); //Player.BLACK.ordinal());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.exit();
	}

}
