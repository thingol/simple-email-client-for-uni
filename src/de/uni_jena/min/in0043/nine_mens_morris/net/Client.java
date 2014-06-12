package de.uni_jena.min.in0043.nine_mens_morris.net;

import de.uni_jena.min.in0043.nine_mens_morris.core.Game;
import de.uni_jena.min.in0043.nine_mens_morris.core.Phase;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;

import java.net.*;
import java.io.*;

public class Client implements Game {

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
	
	public static void main(String[] args) {
			StartClient();
	}

	private static void StartClient() {
		Socket server = null;
		try {
			server = new Socket("iaxp16.inf.uni-jena.de", 1234);
			InputStream in = server.getInputStream();
			OutputStream out = server.getOutputStream();
			
			DataInputStream din = new DataInputStream(in);
			DataOutputStream dout = new DataOutputStream(out);
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Can't find host.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error connecting to host.");
		}
		  finally {
			  if(server != null)
				  try{ server.close(); } catch (IOException g) {System.out.println("Error closing server");}
			  }
		}

}
