package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginServer {
	
	private static Logger log = LogManager.getLogger();
	private boolean running = true;
	private static final int DEFAULT_PORT = 6112;
	private int port = -1;
	private static final String DEFAULT_USER_DB_FNAME = "user.db";
	private String dbFname = null;
	private File userDb;
	private HashMap<String, String> cachedUserDb;

	private ServerSocket server;
	
	private LoginServer(int port, String dbFname) {
		
		if(dbFname != null) {
			this.dbFname = dbFname;
		} else {
			this.dbFname = DEFAULT_USER_DB_FNAME;
		}
		
		if(port > 0) {
			this.port = port;
		} else {
			this.port = DEFAULT_PORT;
		}
	}
	
	public LoginServer(String dbFname) {
		
		 this(DEFAULT_PORT, dbFname);
	}
	
	public LoginServer(int port) {
		
		 this(port, DEFAULT_USER_DB_FNAME);
	}
	
	public LoginServer() {
		
		 this(DEFAULT_PORT, DEFAULT_USER_DB_FNAME);
	}
	
	
	public boolean logIn(Socket player) throws IOException {
		log.entry();
		
		byte[] rcvBuf = new byte[3];
		DataInputStream in = new DataInputStream(player.getInputStream());
		DataOutputStream out = new DataOutputStream(player.getOutputStream());
		
		log.trace("verifying player");
		in.readFully(rcvBuf);

		if(Arrays.equals(rcvBuf, ProtocolOperators.HELLO)) {
			in.readUTF();
			
			
			log.info("player verified");
			out.write(ProtocolOperators.ACK);
			
			log.exit();
			return true;
		} else {
			log.warn("player cannot be verified, closing socket");
			player.close();
			
			log.exit(false);
			return false;
		}
	}
	
	private void init(int port, String dbFileName) {
		log.entry();
		
		log.info("initializing server");
		
		try {
			log.info("binding to :" + port);
			server = new ServerSocket(port);
		} catch (IOException e) {
			if(e instanceof BindException) {
				log.error("port " + port + " is in use, exiting");
				System.exit(1);
			}
		}
		
		userDb = new File(dbFname);
		if(!userDb.exists()) {
			log.info("creating new user database");
			try {
				userDb.createNewFile();
			} catch (IOException e) {
				log.error("caught IOException while creating new user database");
				log.error("shutting down");
				System.exit(1);
			}
		} else {
			log.debug("user database exists");
			log.info("loading users from database");
			cachedUserDb = new HashMap<String, String>();
			Scanner db = null;
			int userCount = 0;
			try {
				db = new Scanner(userDb);
				while(db.hasNext()) {
					String[] user = db.nextLine().split(",");
					cachedUserDb.put(user[0], user[1]);
					log.debug("loaded " + Arrays.toString(user));
					userCount++;
				}
			} catch (FileNotFoundException e) {
				log.error("caught FileNotFoundException while loading users");
				log.info("exiting");
				System.exit(1);
			} finally {
				db.close();
			}
			log.info("loaded " + userCount + " users");
		}
		log.exit();
	}
	
	public void startServer() {
		log.entry();
		
		init(port, dbFname);
		
		while (running) {

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
				log.error("caught exception while processing attempted login: " + e.getClass());
			}
		}
	}
	
	public static void main(String[] args) {
		
		log.info("starting server");
		LoginServer ls = new LoginServer();
		
		ls.startServer();
	}
}
