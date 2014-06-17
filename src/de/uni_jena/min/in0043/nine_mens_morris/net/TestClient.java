package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.*;
import java.net.*;

import de.uni_jena.min.in0043.nine_mens_morris.core.Game;
import de.uni_jena.min.in0043.nine_mens_morris.core.Phase;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;
import de.uni_jena.min.in0043.nine_mens_morris.gui.Head;

public class TestClient implements Game {

	public static Head headS;
	Socket server;
	InputStream in;
	OutputStream out;
	DataInputStream din;
	DataOutputStream dout;
	byte[] get;
	byte[] send;
	public Head head;
	
	public TestClient()
	{
		try {
			server = new Socket("gw.kjerkreit.org", 6112);
			in = server.getInputStream();
			out = server.getOutputStream();
			din = new DataInputStream(in);
			dout = new DataOutputStream(out);
			get = new byte[3];
			send = new byte[3];
			head = new Head();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public Player getActivePlayer() {
		try {
			send[0] = 0x06;
			out.write(send);
			
			in.read(get);
			if(get[0] == 0x00)
				return Player.WHITE;
			else if(get[0] == 0x01) return Player.BLACK;
			else System.out.println("Error happened!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Phase getPhase() {
		try {
			send[0] = 0x05;
			out.write(send);
			
			in.read(get);
			if(get[0] == 0x00)
				return Phase.PLACING_STONES;
			else if(get[0] == 0x01)
				return Phase.NORMAL_PLAY;
			else if(get[0] == 0x02)
				return Phase.WHITE_REDUCED;
			else if(get[0] == 0x03)
				return Phase.BLACK_REDUCED;
			else if(get[0] == 0x04)
				return Phase.BOTH_REDUCED;
			else if(get[0] == 0x05)
				return Phase.GAME_OVER;
			else System.out.println("Error! Couldnt grab phase!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getRound() {
		try {
			send[0] = 0x0D;
			out.write(send);
			
			in.read(get);
			return (int) get[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int getWhiteActivated() {
		try {
			send[0] = 0x07;
			out.write(send);
			
			in.read(get);
			return (int) get[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getWhiteInPlay() {
		try {
			send[0] = 0x08;
			out.write(send);
			
			in.read(get);
			return (int) get[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getWhiteLost() {
		try {
			send[0] = 0x09;
			out.write(send);
			
			in.read(get);
			return (int) get[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getBlackActivated() {
		try {
			send[0] = 0x0a;
			out.write(send);
			
			in.read(get);
			return (int) get[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getBlackInPlay() {
		try {
			send[0] = 0x0b;
			out.write(send);
			
			in.read(get);
			return (int) get[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getBlackLost() {
		try {
			send[0] = 0x0c;
			out.write(send);
			
			in.read(get);
			return (int) get[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int moveStone(int stone, int point) {
		try {
			send[0] = 0x01;
			send[1] = (byte) stone;
			send[2] = (byte) point;
			out.write(send);
			in.read(get);
			if(get[0] == 0x00)
			{
				System.out.println("Everything's fine!");
				return 1;
			}
			else if(get[0] == 0x01)
			{
				System.out.println("Mill!");
				return 2;
			}
			else if(get[0] == 0x02)
			{
				System.out.println("Can't move enemy stone!");
			}
			else if(get[0] == 0x03)
			{
				System.out.println("A stone must be removed first!");
			}
			else if(get[0] == 0x04)
			{
				System.out.println("Illegal Move!");
			}
			else if(get[0] == 0x05)
			{
				System.out.println("Game is over!");
			}
		} catch (IOException e) {}
		return 0;
	}

	@Override
	public boolean removeStone(int stone) {
		try {
			send[0] = 0x02;
			send[1] = (byte) stone;
			send[2] = 0x00;
			out.write(send);
			in.read(get);
			if(get[0] == 0x00)
			{
				System.out.println("Everything's fine!");
				return true;
			}
			else if(get[0] == 0xfd)
			{
				System.out.println("Can't remove your own stone!");
			}
			else if(get[0] == 0xff)
			{
				System.out.println("Part of a Mill!");
			}
			else
				{
				System.out.println("Strange message sent!");
				}
		} catch (IOException e) {}
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
		System.out.println("sending data...");
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
//		StartClient();
		TestClient c = new TestClient();
		c.StartUp();
}
	private void StartUp() {
		try {
			System.out.println("Connecting...");
			send[0] = 0x00;
			send[1] = 0x00;
			send[2] = 0x00;
			System.out.println("sending data...");
			dout.write(send);
			System.out.println("Data sent!");
			din.read(get);
			
			if(get[0] == 0x00) System.out.println("Connection established!");
			else {
				System.out.println("Error! Connecting failed!");
//				servers.close();
			}
			head.BuildUp();

			while(get[0] != 0x04)
			{
				din.read(get);
			}
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("Can't find host.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error connecting to host.");
		}
		  finally {
			  if(server != null)
				  try{ server.close(); } catch (IOException g) {System.out.println("Error closing server: " + g.getMessage());}
			  }
		}

}
