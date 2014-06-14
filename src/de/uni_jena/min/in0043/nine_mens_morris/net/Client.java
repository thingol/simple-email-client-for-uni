package de.uni_jena.min.in0043.nine_mens_morris.net;

import de.uni_jena.min.in0043.nine_mens_morris.core.Game;
import de.uni_jena.min.in0043.nine_mens_morris.core.Phase;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;
import de.uni_jena.min.in0043.nine_mens_morris.gui.Head;

import java.net.*;
import java.io.*;

public class Client implements Game {

	Socket server;
	InputStream in;
	OutputStream out;
	DataInputStream din;
	DataOutputStream dout;
	byte[] get;
	byte[] send;
	public static Head head;
	
	public Client()
	{
		try {
			server = new Socket("gw.kjerkreit.org", 6112);
			in = server.getInputStream();
			out = server.getOutputStream();
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
		din = new DataInputStream(in);
		dout = new DataOutputStream(out);
		get = new byte[3];
		send = new byte[3];
	}
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
		
		return 0;
	}

	@Override
	public boolean removeStone(int stone) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void StartUp()
	{
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
			server.close();
		}
		
		send[0] = 0x04;
		dout.write(send);
		din.read(get);
		if(get[0] == 0x004)
		{
			System.out.println("Server closing...");
			server.close();
			System.out.println("Server closed.");
		}
		
		
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("Can't find host.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error connecting to host.");
		}
	}
	
	public static void main(String[] args) {
			StartClient();
			Client c = new Client();
			c.StartUp();
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
				servers.close();
			}
			
			sendS[0] = 0x04;
			System.out.println("Sending good bye...");
			douts.write(sendS);
			dins.read(getS);
			if(getS[0] == 0x004)
			{
				System.out.println("Server closing...");
				servers.close();
				System.out.println("Server closed.");
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

}
