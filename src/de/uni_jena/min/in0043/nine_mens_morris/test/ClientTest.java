package de.uni_jena.min.in0043.nine_mens_morris.test;

import de.uni_jena.min.in0043.nine_mens_morris.net.Client;

public class ClientTest {
	
	static Client client = new Client("gw.kjerkreit.org");

	public static void main(String[] args) {
		System.out.println("firing up Client");
		client.start();
		
		client.interrupt();
		client.interrupt();
		
	}
}
