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
	
	
	public LoggedInUser(String username, Socket sock, DataInputStream in, DataOutputStream out) {
		this.username = username;
		this.sock = sock;
		this.in = in;
		this.out = out;
		this.playing = false;
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
	
	public void close() throws IOException {
		this.sock.close();
	}
	
	public boolean isPlaying() {
		return this.playing;
	}

}
