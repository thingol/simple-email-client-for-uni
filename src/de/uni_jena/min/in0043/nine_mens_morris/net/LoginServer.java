package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginServer {
	
	private static Logger log = LogManager.getLogger();
	private static final int MAX_GAMES = 32;
	private static final int DEFAULT_PORT = 6112;
	private int gameCount = 0;

	private ServerSocket server;
	
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
	
	
	
	public void startServer() {
	
		log.info("starting server");
		
		while ( true ) {

			log.trace("top of main loop");
			GameServer gameThread;
			try {
				Socket player0 = server.accept();
				log.info("first player connected from " + player0.getInetAddress().getHostAddress());
				Socket player1 = server.accept();
				log.info("second player connected from " + player1.getInetAddress().getHostAddress());
				gameThread = new GameServer(player0, player1);
				gameThread.start();
				gameCount++;
				log.trace("number of games started: " + gameCount);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) {
		LoginServer ls = new LoginServer();
		
		ls.startServer();
	}
	
}
