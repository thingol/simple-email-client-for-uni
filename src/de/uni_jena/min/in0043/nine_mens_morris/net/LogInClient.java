package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
	
	public LogInClient() {
		this("localhost", DEFAULT_PORT);
	}
	
	public LogInClient(String hostName) {
		this(hostName, DEFAULT_PORT);
	}
	
	public LogInClient(String hostName, int port) {
		try {
			log.debug("connection to " + hostName + ":" + port);
			srv = new Socket(hostName, port);
			input = new DataInputStream(new BufferedInputStream(srv.getInputStream()));
			output = new DataOutputStream(srv.getOutputStream());
		} catch (UnknownHostException e) {
			log.error("Unkown host: " + hostName);
		} catch (IOException e) {
			log.error("caught IOException while setting up");
		}
		rcvBuf = new byte[3];
	}
	
	private boolean initiateHandshake(boolean newUser) throws IOException {
		log.entry();
		boolean retVal = false;
		
		output.write(ProtocolOperators.HELLO);
		if(newUser) {
			log.debug("new user");
			output.write(ProtocolOperators.NEW_USER);
		} else {
			output.write(ProtocolOperators.EXISTING_USER);
			log.debug("old user");
		}
		input.readFully(rcvBuf);
		
		if(Arrays.equals(rcvBuf, ProtocolOperators.ACK)) {
			retVal = true;
			log.debug("handshake went ok");
		}
	
		log.exit();
		return retVal;
	}
	
	public boolean loggingIn(String user, char[] pass, boolean newUser) {
		log.entry();
		boolean retVal = false;
		try {
			if(initiateHandshake(newUser)) {
				output.writeUTF(user+","+new String(pass));
				input.readFully(rcvBuf);
				if(Arrays.equals(rcvBuf, ProtocolOperators.ACK)) {
					retVal = true;
				}
			}
		} catch (IOException e) {
			log.error("caught " + e.getClass() + " while logging in");
		}
		log.exit();
		return retVal;
	}
	
	
	public void playWith(int number) {
		
	}
	
	public void updateList() {
		
	}
	
	public boolean disconnect() {
		return true;
	}
}
