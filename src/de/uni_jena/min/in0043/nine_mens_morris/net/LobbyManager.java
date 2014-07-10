package de.uni_jena.min.in0043.nine_mens_morris.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LobbyManager extends Thread {
	
	private static Logger log = LogManager.getLogger();
	
	private static final int MAX_GAMES = 32;
	private Lobby lobby;
	private volatile boolean running = true;
	private volatile boolean snoring = true;
	
	
	public LobbyManager(Lobby lobby) {
		this.lobby = lobby;
		this.setName("LobbyManger");
	}
	
	
	public void run() {


		while(running) {
			if(snoring) {
				//log.debug("snoring some more");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					log.debug("sleep was interrupted :(");
				}
				lobby.pollUsers();
				lobby.manageChallenges();
			}
		}
	}
}
