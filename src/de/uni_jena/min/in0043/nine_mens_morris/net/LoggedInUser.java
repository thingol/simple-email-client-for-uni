package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LoggedInUser {
	
	private String username;
	private Socket sock;
	private DataInputStream in;
	private DataOutputStream out;
	private boolean playing;
	private volatile boolean challenged;
	private int id;
	
	
	public LoggedInUser(int id, String username, Socket sock, DataInputStream in, DataOutputStream out) {
		this.id = id;
		this.username = username;
		this.sock = sock;
		this.in = in;
		this.out = out;
		this.challenged  = false;
		this.playing = false;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public DataInputStream getInputStream() {
		return this.in;
	}
	
	public DataOutputStream getOutputStream() {
		return this.out;
	}
	
	public Socket getSocket() {
		return this.sock;
	}
	
	public void close() throws IOException {
		this.sock.close();
	}
	
	public boolean isPlaying() {
		return this.playing;
	}
	
	public void isPlaying(boolean b) {
		this.playing = b;
	}
	
	public boolean isChallenged() {
		return this.challenged;
	}
	
	public void isChallenged(boolean b) {
		this.challenged = b;
	}
	
	public String toString() {
		return this.username;
	}

}
