package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class Lobby {
	
	private ConcurrentHashMap<String, LoggedInUser> users;
	private LinkedList<GameServer> games;
	private Deque<LoggedInUser> waitingLine;
	private int gamesStarted = 0;
	
	
	public Lobby() {
		users = new ConcurrentHashMap<String, LoggedInUser>();
		games = new LinkedList<GameServer>();
	}
	
	public int getGamesStarted() {
		return this.gamesStarted;
	}
	
	public int getLoggedInUsers() {
		return users.size();
	}
	
	
	public synchronized void addUser(LoggedInUser user) {
		waitingLine.add(user);	
	}
	
	public synchronized void startGameServer(LoggedInUser player0, LoggedInUser player1) {
		gamesStarted++;
		
		GameServer gameThread = new GameServer(System.currentTimeMillis(), player0, player1);
		gameThread.start();
		games.add(gameThread);
	}
	

}
