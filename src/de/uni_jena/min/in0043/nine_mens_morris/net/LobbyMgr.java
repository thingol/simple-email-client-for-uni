package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyMgr extends Thread {
	
	private static final int MAX_GAMES = 32;
	private int gamesStarted = 0;
	private Lobby lobby;
	
	
	public LobbyMgr() {
		
		lobby = new Lobby();
	}
	
	private void startGameServer(LoggedInUser player0, LoggedInUser player1) {
		gamesStarted++;
		
		GameServer gameThread = new GameServer(System.currentTimeMillis(), player0, player1);
		gameThread.start();
		games.add(gameThread);
	}
	
	public void run() {
		
		
		while(true) {
			
		}
	}
}
