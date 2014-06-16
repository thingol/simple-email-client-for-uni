package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.*;
import java.net.*;

import de.uni_jena.min.in0043.nine_mens_morris.core.Game;
import de.uni_jena.min.in0043.nine_mens_morris.core.Phase;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;
import de.uni_jena.min.in0043.nine_mens_morris.gui.Head;

public class TestClient implements Game {

	public static Head headS;
	@Override
	public Player getActivePlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Phase getPhase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRound() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWhiteActivated() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWhiteInPlay() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWhiteLost() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBlackActivated() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBlackInPlay() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBlackLost() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveStone(int stone, int point) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean removeStone(int stone) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private static void StartClient() {
	Socket servers = null;
	try {
		servers = new Socket("gw.kjerkreit.org", 6112);
		InputStream ins = servers.getInputStream();
		OutputStream outs = servers.getOutputStream();
		DataInputStream dins = new DataInputStream(ins);
		DataOutputStream douts = new DataOutputStream(outs);
		byte[] getS = new byte[3];
		byte[] sendS = new byte[3];
		headS = new Head();
		
		System.out.println("Connecting...");
		sendS[0] = 0x00;
		sendS[1] = 0x00;
		sendS[2] = 0x00;
		System.out.println("sendSing data...");
		douts.write(sendS);
		System.out.println("Data sent!");
		dins.read(getS);
		
		if(getS[0] == 0x00) System.out.println("Connection established!");
		else {
			System.out.println("Error! Connecting failed!");
//			servers.close();
		}
		headS.BuildUp();

		while(getS[0] != 0x04)
		{
			dins.read(getS);
		}
		
		
	} catch (UnknownHostException e) {
		e.printStackTrace();
		System.out.println("Can't find host.");
	} catch (IOException e) {
		e.printStackTrace();
		System.out.println("Error connecting to host.");
	}
	  finally {
		  if(servers != null)
			  try{ servers.close(); } catch (IOException g) {System.out.println("Error closing server: " + g.getMessage());}
		  }
	}
	
	public static void main(String[] args) {
		StartClient();;
}

}
