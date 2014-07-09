package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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
	private ServerSocket server;
	
	private static final String DEFAULT_USER_DB_FNAME = "user.db";
	private String dbFname = null;
	private File userDb;
	private HashMap<String, String> cachedUserDb;
	private Lobby lobby;
	private int usersAuthenticated = 0;

	
	
	private LoginServer(int port, String dbFname, Lobby lobby) {
		
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
		
		this.lobby = lobby;
	}
	
	public LoginServer(String dbFname, Lobby lobby) {
		
		 this(DEFAULT_PORT, dbFname, lobby);
	}
	
	public LoginServer(int port, Lobby lobby) {
		
		 this(port, DEFAULT_USER_DB_FNAME, lobby);
	}
	
	public LoginServer(Lobby lobby) {
		
		 this(DEFAULT_PORT, DEFAULT_USER_DB_FNAME, lobby);
	}
	
	
	public boolean addUser(String[] user, String entry) {
		log.entry();
		
		boolean retVal = false;
		
		if(cachedUserDb.get((String)user[0]) != null) {
			log.error("a user '" + user[0] + "' already exists");
		} else {
			cachedUserDb.put(user[0], user[1]);
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(userDb);
				pw.append(entry);
				pw.flush();
				retVal = true;
				log.info("added '" + user[0] + "'");
			} catch (FileNotFoundException e) {
				log.error("caught " + e.getClass() + " while adding new user to db");
			} finally {
				log.debug("closing writer");
				pw.close();
			}
		}
		log.exit();
		return retVal;
	}
	
	private boolean checkUser(String[] user) {
		boolean retVal = false;
		
		if(cachedUserDb.containsKey(user[0])
				&& cachedUserDb.get((String)user[0]).equals(user[0])) {
			retVal = true;
			log.info("user '" + user[0] + "' has been authenticated");
		} else {
			log.warn("user '" + user[0] + "' has not been authenticated!");
		}
		
		return retVal;
	}
	
	public boolean logIn(Socket player) throws IOException {
		log.entry();
		
		boolean retVal = false;
		byte[] rcvBuf = new byte[3];
		DataInputStream in = new DataInputStream(player.getInputStream());
		DataOutputStream out = new DataOutputStream(player.getOutputStream());
		
		log.trace("verifying player");
		in.readFully(rcvBuf);

		if(Arrays.equals(rcvBuf, ProtocolOperators.HELLO)) {
			
			in.readFully(rcvBuf);
			if(Arrays.equals(rcvBuf, ProtocolOperators.EXISTING_USER)) {
				log.debug("checking existing user");
				String s = in.readUTF();
				String[] userinfo = s.split(",");
				if(checkUser(userinfo)) {
					usersAuthenticated++;
					retVal = true;
					out.write(ProtocolOperators.ACK);
					lobby.add(new LoggedInUser(usersAuthenticated, userinfo[0], player, in, out));
				} else {
					out.write(ProtocolOperators.NACK);
				}
			} else if(Arrays.equals(rcvBuf, ProtocolOperators.NEW_USER)) {
				log.debug("add new user");
				String s = in.readUTF();
				String[] userinfo = s.split(",");
				if(addUser(userinfo, s)) {
					usersAuthenticated++;
					retVal = true;
					out.write(ProtocolOperators.ACK);
					lobby.add(new LoggedInUser(usersAuthenticated,userinfo[0], player, in, out));
				} else {
					out.write(ProtocolOperators.NACK);
				}
			} else {
				log.error("Protocol error: Illegal operator after initial 'HELLO'");
			}
			
			// old
			log.info("player verified");
			out.write(ProtocolOperators.ACK);
			
		} else {
			log.error("Protocol error: no 'HELLO' received");
			player.close();
		}
		
		log.exit();
		return retVal;
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
}
