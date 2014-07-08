package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyServer extends Thread {
	
	private static final int MAX_GAMES = 32;
	private ConcurrentHashMap<String, LoggedInUser> users;
	private LinkedList<GameServer> games;
	private int gamesStarted = 0;
	
	
	public LobbyServer() {
		users = new ConcurrentHashMap<String, LoggedInUser>();
		games = new LinkedList<GameServer>();
	}
	
	public int getGamesStarted() {
		return this.gamesStarted;
	}
	
	public int getLoggedInUsers() {
		return users.size();
	}
	
	
	public void addUser(LoggedInUser user) {
		users.put(user.getUsername(), user);
		
	}
	
	public void startGameServer(LoggedInUser player0, LoggedInUser player1) {
		gamesStarted++;
		
		GameServer gameThread = new GameServer(System.currentTimeMillis(), player0, player1);
		gameThread.start();
		games.add(gameThread);
	}
	

}
