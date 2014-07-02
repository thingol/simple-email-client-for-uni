package de.uni_jena.min.in0043.nine_mens_morris.test;

import de.uni_jena.min.in0043.nine_mens_morris.core.GameClient;
import de.uni_jena.min.in0043.nine_mens_morris.gui.Head;
import de.uni_jena.min.in0043.nine_mens_morris.net.Client;

public class ClientTest {
	
	//static Client client = new Client("gw.kjerkreit.org");
	static Client client = new Client("localhost");
	static GameClient display = new Head(client);

	public static void main(String[] args) {
		System.out.println("firing up Client");
		client.start();
		client.addDisplay(display);
		((Head)display).init();
		
	}
}
