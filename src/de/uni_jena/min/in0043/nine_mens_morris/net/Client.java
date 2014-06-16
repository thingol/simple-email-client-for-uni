package de.uni_jena.min.in0043.nine_mens_morris.net;

import de.uni_jena.min.in0043.nine_mens_morris.core.Game;
import de.uni_jena.min.in0043.nine_mens_morris.core.Phase;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;
import de.uni_jena.min.in0043.nine_mens_morris.gui.Head;

import java.net.*;
import java.io.*;

public class Client implements Game {

	Head head;
	Socket server;
	DataInputStream in;
	DataOutputStream out;
	byte get;
	byte[] send;
//	public static Head headS;
	
	public Client()
	{
		try {
			server = new Socket("gw.kjerkreit.org", 6112);
			send = new byte[3];
			in = new DataInputStream(server.getInputStream());
			out = new DataOutputStream(server.getOutputStream());
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
		head = new Head();
	}
	@Override
	public Player getActivePlayer() {
		try {
			send[0] = 0x06;
			out.write(send);
			
			get = in.readByte();
			if(get == 0x00)
				return Player.WHITE;
			else return Player.BLACK;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Phase getPhase() {
		try {
			send[0] = 0x05;
			out.write(send);
			
			get = in.readByte();
			if(get == 0x00)
				return Phase.PLACING_STONES;
			else if(get == 0x01)
				return Phase.NORMAL_PLAY;
			else if(get == 0x02)
				return Phase.WHITE_REDUCED;
			else if(get == 0x03)
				return Phase.BLACK_REDUCED;
			else if(get == 0x04)
				return Phase.BOTH_REDUCED;
			else if(get == 0x05)
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWhiteActivated() {
		try {
			send[0] = 0x07;
			out.write(send);
			
			get = in.readByte();
			return (int) get;
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
			
			get = in.readByte();
			return (int) get;
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
			
			get = in.readByte();
			return (int) get;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getBlackActivated() {
		try {
			send[0] = 0x10;
			out.write(send);
			
			get = in.readByte();
			return (int) get;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getBlackInPlay() {
		try {
			send[0] = 0x11;
			out.write(send);
			
			get = in.readByte();
			return (int) get;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getBlackLost() {
		try {
			send[0] = 0x12;
			out.write(send);
			
			get = in.readByte();
			return (int) get;
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
			get = in.readByte();
			if(get == 0x00)
			{
				System.out.println("Everything's fine!");
				return 1;
			}
			else if(get == 0x01)
			{
				System.out.println("Mill!");
				return 2;
			}
			else if(get == 0xfc)
			{
				System.out.println("Can't move enemy stone!");
			}
			else if(get == 0xfd)
			{
				System.out.println("A stone must be removed first!");
			}
			else if(get == 0xfe)
			{
				System.out.println("Illegal Move!");
			}
			else if(get == 0xff)
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
			get = in.readByte();
			if(get == 0x00)
			{
				System.out.println("Everything's fine!");
				return true;
			}
			else if(get == 0xfd)
			{
				System.out.println("Can't remove your own stone!");
			}
			else if(get == 0xfe)
			{
				System.out.println("Stone is part of a mill");
			}
			else if(get == 0xff)
			{
				System.out.println("Game is over!");
			}
			else
				{
				System.out.println("Strange message sent, closing connection!");
				send[0] = 0x04;
				out.write(send);
				}
		} catch (IOException e) {}
		return false;
	}
	
	
	public void StartUp()
	{
		try {
		System.out.println("Connecting...");
		send[0] = 0x00;
		send[1] = 0x00;
		send[2] = 0x00;
		System.out.println("sending data..." + send[0] + " " + send[1] + " " + send[2]);
		out.write(send);
		System.out.println("Data sent!");
		get = in.readByte();
		
		if(get == 0x00) System.out.println("Connection established!");
		else {
			System.out.println("Error! Connecting failed!");
			server.close();
		}
		
		head.BuildUp();
		
//		send[0] = 0x04;
//		out.write(send);
//		get = in.readByte();
//		if(get == 0x04)
//		{
//			System.out.println("Server closing...");
//			server.close();
//			System.out.println("Server closed.");
//		}
//		
		while(get != 0x04)
		{
			get = in.readByte();
		}
		
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("Can't find host.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error connecting to host! " + e);
		}
		finally {
			  if(server != null)
				  try{ server.close(); } catch (IOException g) {System.out.println("Error closing server: " + g.getMessage());}
			  }
		
	}
	
	public static void main(String[] args) {
//			StartClient();
			Client c = new Client();
			c.StartUp();
	}

//	private static void StartClient() {
//		Socket servers = null;
//		try {
//			servers = new Socket("gw.kjerkreit.org", 6112);
//			InputStream ins = servers.getInputStream();
//			OutputStream outs = servers.getOutputStream();
//			DataInputStream dins = new DataInputStream(ins);
//			DataOutputStream douts = new DataOutputStream(outs);
//			byte[] getS = new byte[3];
//			byte[] sendS = new byte[3];
//			
//			System.out.println("Connecting...");
//			sendS[0] = 0x00;
//			sendS[1] = 0x00;
//			sendS[2] = 0x00;
//			System.out.println("sendSing data...");
//			douts.write(sendS);
//			System.out.println("Data sent!");
//			dins.read(getS);
//			
//			if(getS[0] == 0x00) System.out.println("Connection established!");
//			else {
//				System.out.println("Error! Connecting failed!");
//				servers.close();
//			}
//			
//			sendS[0] = 0x04;
//			System.out.println("Sending good bye...");
//			douts.write(sendS);
//			dins.read(getS);
//			if(getS[0] == 0x004)
//			{
//				System.out.println("Server closing...");
//				servers.close();
//				System.out.println("Server closed.");
//			}
//			
//			
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//			System.out.println("Can't find host.");
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.out.println("Error connecting to host.");
//		}
//		  finally {
//			  if(servers != null)
//				  try{ servers.close(); } catch (IOException g) {System.out.println("Error closing server: " + g.getMessage());}
//			  }
//		}

}
