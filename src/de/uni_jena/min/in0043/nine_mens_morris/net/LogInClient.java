package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogInClient extends Thread {

	final private static int DEFAULT_PORT = 6112;
	private Socket srv;
	private DataInputStream input;
	private DataOutputStream output;
	private static Logger log = LogManager.getLogger();
	
	public LogInClient() {
		this("gw.kjerkreit.org", DEFAULT_PORT);
	}
	
	public LogInClient(String hostName) {
		this(hostName, DEFAULT_PORT);
	}
	
	public LogInClient(String hostName, int port) {
		try {
			srv = new Socket(hostName, port);
			input = new DataInputStream(new BufferedInputStream(srv.getInputStream()));
			output = new DataOutputStream(srv.getOutputStream());
		} catch (UnknownHostException e) {
			log.error("Unkown host: " + hostName);
		} catch (IOException e) {
			log.error("caught IOException while setting up");
		}
	}
	
	public boolean loggingIn(String user, char[] pass) {
		return false;
	}
	
	public boolean register(String user, char[] pass) {
		return false;
	}
	
	public void playWith(int number) {
		
	}
	
	public void updateList() {
		
	}
	
	public boolean disconnect() {
		return true;
	}
}
