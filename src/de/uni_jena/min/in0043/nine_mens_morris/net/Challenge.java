package de.uni_jena.min.in0043.nine_mens_morris.net;

public class Challenge {
	private LoggedInUser challenger;
	private LoggedInUser challenged;
	private long timestamp;
	private boolean accepted = false;
	private boolean completed = false;
	
	public Challenge(LoggedInUser challenger, LoggedInUser challenged, long timestamp) {
		this.challenger = challenger;
		this.challenged = challenged;
		this.timestamp = timestamp;
	}
	
	public void accepted(boolean a) {
		this.accepted = a;
	}
	
	public boolean accepted() {
		return this.accepted;
	}
	
	public void completed(boolean c) {
		this.completed = c;
	}
	
	public boolean completed() {
		return this.completed;
	}
	
	public LoggedInUser getChallenger() {
		return this.challenger;
	}
	
	public LoggedInUser getChallenged() {
		return this.challenged;
	}
	
	public long getTimestamp() {
		return this.timestamp;
	}

}