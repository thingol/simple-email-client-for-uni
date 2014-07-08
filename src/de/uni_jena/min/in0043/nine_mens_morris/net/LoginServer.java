package de.uni_jena.min.in0043.nine_mens_morris.net;

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
	private static final int DEFAULT_PORT = 6112;
	private static final String DEFAULT_USER_DB = "user.db";

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
		DataInputStream in = new DataInputStream(player.getInputStream());
		DataOutputStream out = new DataOutputStream(player.getOutputStream());
		
		log.trace("verifying player");
		in.readFully(rcvBuf);

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
			Socket player;
			
			try {
				
				player = server.accept();
				log.info("player connected from " + player.getInetAddress().getHostAddress());
				if(logIn(player)) {
					log.info("login successful");
					
				} else {
					log.info("login failed");
					player.close();
				}

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
