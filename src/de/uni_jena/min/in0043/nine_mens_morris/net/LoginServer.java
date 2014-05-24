package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.IOException;
import java.net.ServerSocket;

public class LoginServer {
	
	private static final int MAX_GAMES = 32;
	private static final int DEFAULT_PORT = 6112;

	ServerSocket server;
	
	private LoginServer(int port) {
		try {
			server = new ServerSocket(DEFAULT_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public LoginServer() {
		
		 this(DEFAULT_PORT);
	}
	
	
	
	public void run() {
	
		while ( true ) { // einzelner Thread bearbeitet eine aufgebaute Verbindung
			
			MulServerThread mulThread = new MulServerThread(server.accept());
				mulThread.start();
		}
	}
	
}
