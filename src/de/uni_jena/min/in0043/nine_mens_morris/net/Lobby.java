package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Lobby {
	
	private static Logger log = LogManager.getLogger();
	
	private ConcurrentHashMap<String, LoggedInUser> users;
	private LinkedList<GameServer> games;
	private LinkedList<Challenge> challenges;
	private int gamesStarted = 0;
	
	
	public Lobby() {
		users = new ConcurrentHashMap<String, LoggedInUser>();
		games = new LinkedList<GameServer>();
		challenges = new LinkedList<Challenge>();
	}
	
	public int getGamesStarted() {
		return this.gamesStarted;
	}
	
	public int getLoggedInUsers() {
		return users.size();
	}
	
	public void add(LoggedInUser user) {
		users.put(user.getUsername(), user);
		log.debug("admitted user " + user + " to lobby");
	}
	
	public synchronized void manageChallenges() {
		long now = System.currentTimeMillis();
		DataOutputStream out;
		DataInputStream challenged;
		byte[] rcvBuf = new byte[3];
		for(Challenge c : challenges) {
			challenged = c.getChallenged().getInputStream();
			out  = c.getChallenger().getOutputStream();
			if(!c.accepted()) {
				try {
					if(challenged.available() > 0) {
						challenged.readFully(rcvBuf);
						if(rcvBuf[0] == ProtocolOperators.CHALLENGE[0]) {
							out.write(ProtocolOperators.ACK);
							c.accepted(true);
							startGameServer(c, c.getChallenger(), c.getChallenged());
						} else {
							out.write(ProtocolOperators.NACK);
							challenges.remove(c);
						}
					} else if(c.getTimestamp() - now > 5000) {
						
						try {
							out.write(ProtocolOperators.NO_RESPONSE);
							out = c.getChallenged().getOutputStream();
							out.write(ProtocolOperators.NACK);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							challenges.remove(c);
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(c.completed()) {
				challenges.remove(c);
			}
			
		}
	}
	
	public synchronized void pollUsers() {
		DataInputStream in;
		DataOutputStream out;
		byte[] rcvBuf = new byte[3];
		for(LoggedInUser u : users.values()) {
			if(u.getSocket().isClosed()) {
				users.remove(u.getUsername());
				log.debug(u.getUsername() + " has left the building");
			} else if(!u.isPlaying()) {
				
				in = u.getInputStream();
				try {
					if(in.available() > 2) {
						out = u.getOutputStream();
						in.readFully(rcvBuf);
						log.debug(u.getUsername() + " had something to say" + Arrays.toString(rcvBuf));
						log.debug("output belongs to " + u.getUsername() + " at " + u.getSocket().getInetAddress());
						handleRequest(rcvBuf, u);
					}
				} catch (IOException e) {
					log.error("caught IOException while polling user sockets");
				}
			}
			
		}
	}
	
	private void startGameServer(Challenge challenge, LoggedInUser player0, LoggedInUser player1) {
		log.info("starting game between " + player0 + " and " + player1);
		gamesStarted++;
		
		player0.isPlaying(true);
		
		GameServer gameThread = new GameServer(System.currentTimeMillis(), challenge, player0, player1);
		gameThread.setName("Game nr. " + gamesStarted);
		gameThread.start();
		games.add(gameThread);
	}
	
	private LoggedInUser getUserByID(int id) {
		for(Entry<String, LoggedInUser> u : users.entrySet()) {
			if(u.getValue().getID() == id) {
				return u.getValue();
			}
		}
		log.error("oops, that users seems to have disappeared :(");
		return null;
	}
	
	private void handleRequest(byte[] rcvBuf, LoggedInUser user) throws IOException {
		/*
		 * GET_USER_LIST
		 * CHALLENGE
		 */
		DataOutputStream out = user.getOutputStream();
		
		if(Arrays.equals(rcvBuf, ProtocolOperators.GET_USERLIST)) {
			log.debug("list of users requested");
			String outPut = "";
			for(Entry<String, LoggedInUser> u : users.entrySet()) {
				outPut = outPut + u.getKey() + "," + u.getValue().getID() + ";"; 
			}
			log.debug("sending user '" + outPut + "'");
			outPut += '\n';
			byte[] bla = outPut.getBytes();
			System.out.println(Arrays.toString(bla));
			out.write(bla);
			log.debug("sent");
		} else if(rcvBuf[0] == ProtocolOperators.CHALLENGE[0]) {
			log.debug("challenge issued to user currently assigned id " + rcvBuf[1]);
			LoggedInUser challenged = getUserByID(rcvBuf[1]);
			challenges.add(new Challenge(user, challenged, System.currentTimeMillis()));
			user.isPlaying(true);
			challenged.isPlaying(true);
		} else {
			log.error("got an illegal operator");
			out.write(ProtocolOperators.ILLEGAL_OP);
		}
		
		log.debug("flushing stream");
		out.flush();
	}
}
