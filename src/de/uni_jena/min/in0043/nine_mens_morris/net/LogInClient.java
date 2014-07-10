package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogInClient extends Thread {

	final private static int DEFAULT_PORT = 6112;
	private Socket srv;
	private DataInputStream input;
	private DataOutputStream output;
	private byte[] rcvBuf;
	private static Logger log = LogManager.getLogger();
	private String hostname;
	private int port;
	private boolean loggedIn = false;
	
	public LogInClient() {
		this("localhost", DEFAULT_PORT);
	}
	
	public LogInClient(String hostName) {
		this(hostName, DEFAULT_PORT);
	}
	
	public LogInClient(String hostName, int port) {
		this.hostname = hostName;
		this.port = port;
		
		rcvBuf = new byte[3];
	}
	
	public void run() {
		
	}
	
	private boolean initiateHandshake(boolean newUser) throws IOException {
		log.entry();
		boolean retVal = true;
		
		output.write(ProtocolOperators.HELLO);
		if(newUser) {
			log.debug("new user");
			output.write(ProtocolOperators.NEW_USER);
		} else {
			output.write(ProtocolOperators.EXISTING_USER);
			log.debug("old user");
		}
	
		log.exit();
		return retVal;
	}
	
	private void loggingIn(String user, String pass, boolean newUser) {
		log.entry();
		if(!loggedIn) {
			try {
				log.debug("connection to " + hostname + ":" + port);
				srv = new Socket(hostname, port);
				input = new DataInputStream(srv.getInputStream());
				output = new DataOutputStream(srv.getOutputStream());
			} catch (UnknownHostException e) {
				log.error("Unkown host: " + hostname);
			} catch (IOException e) {
				log.error("caught IOException while setting up");
			}
			
			try {
				if(initiateHandshake(newUser)) {
					output.writeUTF(user+","+new String(pass));
					input.readFully(rcvBuf);
					
					log.debug("received " + Arrays.toString(rcvBuf) + "from server");
					if(!Arrays.equals(rcvBuf, ProtocolOperators.ACK)) {
						log.error("login failed");
					
					} else {
						loggedIn = true;
					}
				}
			} catch (IOException e) {
				log.error("caught " + e.getClass() + " while logging in");
			}
		}
		log.exit();
	}
	
	
	public void playWith(int number) {
		
	}
	
	private String updateList() {
		String s = "ups";
		try {
			log.debug("requsting user list");
			output.write(ProtocolOperators.GET_USERLIST);
			log.debug("receiving");
			System.out.println("there's this much waiting for us: " + input.available());
			s = new BufferedReader(new InputStreamReader(input, "utf-8")).readLine();
			//s = input.readUTF();
			log.debug("received " + s);
			
		} catch (IOException e) {
			log.error("caught " + e.getClass() + " while updating user list");
		}
		return s;
	}
	
	private void disconnect() {
		
		try {
			srv.close();
		} catch (IOException e) {
			log.error("caught " + e.getClass() + " while logging out");
		}
	}
}
