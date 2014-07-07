package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
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
			log.info("binding to :" + port);
			server = new ServerSocket(port);
		} catch (IOException e) {
			if(e instanceof BindException) {
				log.error("port " + port + " is in use, exiting");
				System.exit(1);
			}
		}
	}
	
	public LoginServer() {
		
		 this(DEFAULT_PORT);
	}
	
	public boolean logIn(Socket player) throws IOException {
		log.entry(player);
		
		byte[] rcvBuf = new byte[3];
		DataInputStream in = new DataInputStream(new BufferedInputStream(player.getInputStream()));
		DataOutputStream out = new DataOutputStream(player.getOutputStream());
		
		log.trace("verifying player");
		in.readFully(rcvBuf);
		log.trace("received [" + rcvBuf[0] + "," + rcvBuf[1] + "," + rcvBuf[2] + "]");
		if(rcvBuf[0] != 0) {
			log.warn("player cannot be verified, closing socket");
			player.close();
			
			log.exit(false);
			return false;
		} else {
			log.info("player verified");
			out.write(ProtocolOperators.ACK);
			
			log.exit(true);
			return true;
		}
	}
	
	public void startServer() {
		log.entry();
		
		while (true) {

			log.trace("top of main loop");
			GameServer gameThread;
			boolean player0_verified = false;
			boolean player1_verified = false;
			Socket player0;
			Socket player1;
			
			try {
				
				do {
					player0 = server.accept();
					log.info("first player connected from " + player0.getInetAddress().getHostAddress());
					player0_verified = logIn(player0);
				} while (!player0_verified);
				
				do {
					player1 = server.accept();
					log.info("second player connected from " + player1.getInetAddress().getHostAddress());
					player1_verified = logIn(player1);
				} while(!player1_verified);
				
				gameThread = new GameServer(System.currentTimeMillis(), player0, player1);
				gameThread.start();
				gameCount++;
				log.trace("number of games started: " + gameCount);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		
		log.info("starting server");
		LoginServer ls = new LoginServer();
		
		ls.startServer();
	}
}
