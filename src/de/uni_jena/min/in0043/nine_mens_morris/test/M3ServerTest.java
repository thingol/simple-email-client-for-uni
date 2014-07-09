package de.uni_jena.min.in0043.nine_mens_morris.test;

import de.uni_jena.min.in0043.nine_mens_morris.net.Lobby;
import de.uni_jena.min.in0043.nine_mens_morris.net.LobbyManager;
import de.uni_jena.min.in0043.nine_mens_morris.net.LoginServer;

public class M3ServerTest {
	
	private static Lobby lobby;
	private static LoginServer loginServer;
	private static LobbyManager lobbyManager;

	private static void init() {
		lobby = new Lobby();
		lobbyManager = new LobbyManager(lobby);
		loginServer = new LoginServer(lobby);
		
	}
	
	public static void main(String[] args) {
		
		init();
		
		lobbyManager.start();
		loginServer.startServer();
		
		
	}
}
