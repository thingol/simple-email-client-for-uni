package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.*;
import java.net.*;
import java.util.Arrays;

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
	public boolean white;
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
			head = new Head(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int moveStone(int stone, int point) {
		try {
			send = ProtocolOperators.MOVE_STONE;
			send[1] = (byte) stone;
			send[2] = (byte) point;
			out.write(send);
			in.read(get);
			if(Arrays.equals(get, ProtocolOperators.ACK))
			{
				System.out.println("Everything's fine!");
				return 1;
			}
			else
			{
				System.out.println("Game is over!");
			}
		} catch (IOException e) {
			// TODO moveStone
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int removeStone(int stone) {
		try {
			send = ProtocolOperators.REMOVE_STONE;
			send[1] = (byte) stone;
			send[2] = 0x00;
			out.write(send);
			in.read(get);
			if(Arrays.equals(get, ProtocolOperators.ACK))
			{
				System.out.println("Worked");
				return 1;
			}
			else
				{
				System.out.println("You can't remove this stone");
				}
		} catch (IOException e) {
			// TODO removeStone
			e.printStackTrace();
		}
		return 0;
	}
	
	public void conceed(boolean newgame)
	{
		try {
			send = ProtocolOperators.CONCEDE;
			if(newgame) send[1] = 0x01;
			else send[1] = 0x00;
			out.write(send);
			in.read(get);
			if(Arrays.equals(get, ProtocolOperators.ACK))
			{
				System.out.println("Conceeded!");
			}
			
		} catch (IOException e) {
			// TODO conceed
			e.printStackTrace();
		}
	}
	
	@Override
	public Phase getPhase() {
		try {
			send[0] = 0x05;
			out.write(send);
			
			in.read(get);
			if(get[0] == 0)
				return Phase.PLACING_STONES;
			else if(get[0] == 1)
				return Phase.NORMAL_PLAY;
			else if(get[0] == 2)
				return Phase.WHITE_REDUCED;
			else if(get[0] == 3)
				return Phase.BLACK_REDUCED;
			else if(get[0] == 4)
				return Phase.BOTH_REDUCED;
			else if(get[0] == 5)
				return Phase.GAME_OVER;
			else System.out.println("Error! Couldnt grab phase!");
		} catch (IOException e) {
			// TODO getPhase
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public Player getActivePlayer() {
		try {
			send = ProtocolOperators.GET_ACTIVE_PLAYER;
			out.write(send);
			
			in.read(get);
			if(get[0] == 0)
				return Player.WHITE;
			else if(get[0] == 1) return Player.BLACK;
			else System.out.println("Error happened!");
		} catch (IOException e) {
			// TODO getActivePlayer
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int getWhiteActivated() {
		try {
			send = ProtocolOperators.GET_WHITE_ACTIVATED;
			out.write(send);
			
			in.read(get);
			return (int) get[0];
		} catch (IOException e) {
			// TODO getWhiteActivated
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int getWhiteInPlay() {
		try {
			send = ProtocolOperators.WHITE_IN_PLAY;
			out.write(send);
			
			in.read(get);
			return (int) get[0];
		} catch (IOException e) {
			// TODO getWhiteInPlay
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int getWhiteLost() {
		try {
			send = ProtocolOperators.WHITE_LOST;
			out.write(send);
			
			in.read(get);
			return (int) get[0];
		} catch (IOException e) {
			// TODO getWhiteLost
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int getBlackActivated() {
		try {
			send = ProtocolOperators.GET_BLACK_ACTIVATED;
			out.write(send);
			
			in.read(get);
			return (int) get[0];
		} catch (IOException e) {
			// TODO getBlackActivated
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int getBlackInPlay() {
		try {
			send = ProtocolOperators.BLACK_IN_PLAY;
			out.write(send);
			
			in.read(get);
			return (int) get[0];
		} catch (IOException e) {
			// TODO getBlackInPlay
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int getBlackLost() {
		try {
			send = ProtocolOperators.BLACK_LOST;
			out.write(send);
			
			in.read(get);
			return (int) get[0];
		} catch (IOException e) {
			// TODO getBlackLost
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int getRound() {
		try {
			send = ProtocolOperators.GET_PHASE;
			out.write(send);
			
			in.read(get);
			return (int) get[0];
		} catch (IOException e) {
			// TODO getRound
			e.printStackTrace();
		}
		return -1;
	}
	
	public byte YOU_WIN()
	{
		try {
			send = ProtocolOperators.YOU_WIN;
			out.write(send);
			
			in.read(get);
			if(Arrays.equals(get, ProtocolOperators.HELLO))
				return get[0];
		} catch (IOException e) {
			// TODO YOU_WIN
			e.printStackTrace();
		}
		return -1;
	}
	
	public byte YOU_LOSE()
	{
		try {
			send = ProtocolOperators.YOU_LOSE;
			out.write(send);
			
			in.read(get);
			if(Arrays.equals(get, ProtocolOperators.HELLO))
				return get[0];
		} catch (IOException e) {
			// TODO YouLose
			e.printStackTrace();
		}
		return -2;
	}
	
	public boolean NEW_GAME()
	{
		try {
			send = ProtocolOperators.NEW_GAME;
			out.write(send);
			
			in.read(get);
			if(Arrays.equals(get, ProtocolOperators.HELLO))
				return true;
		} catch (IOException e) {
			// TODO NewGame
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean NO_MORE()
	{
		try {
			send = ProtocolOperators.NO_MORE;
			out.write(send);
			
			in.read(get);
			if(Arrays.equals(get, ProtocolOperators.HELLO))
				return true;
		} catch (IOException e) {
			// TODO NoMore
			e.printStackTrace();
		}
		return false;
	}

	private void handlingStuff() {
		// TODO Implement moveStone.
		if(Arrays.equals(get, ProtocolOperators.MOVE_STONE))
			head.moveStone((int) get[1], (int) get[2]);
		else if(Arrays.equals(get, ProtocolOperators.REMOVE_STONE))
			head.delete((int) get[1]);
		else if(Arrays.equals(get, ProtocolOperators.CONCEDE))
			// TODO implement WIN method maybe
			System.out.println("WIN");
		else if(Arrays.equals(get, ProtocolOperators.BYE))
			// TODO implement WIN method maybe
			System.out.println("User disconnected");
		else if(Arrays.equals(get, ProtocolOperators.NEW_GAME))
			head.reset();
		else System.out.println(get.toString());
	}
	
	private void StartUp() {
		try {
			System.out.println("Connecting...");
			send = ProtocolOperators.HELLO;
			System.out.println("sending data...");
			dout.write(send);
			System.out.println("Data sent!");
			din.read(get);
			byte[] check = new byte[3];
			if(Arrays.equals(get, ProtocolOperators.ACK)) {
			
			if(Arrays.equals(get, ProtocolOperators.HELLO)) System.out.println("Connection established!");
			else {
				System.out.println("Error! Connecting failed! " + get[0]);
//				servers.close();
			}
			head.BuildUp();
			din.read(get);
			if(Arrays.equals(get, ProtocolOperators.IS_WHITE))
				head.color = Player.WHITE;
			else if(Arrays.equals(get, ProtocolOperators.IS_BLACK))
				head.color = Player.BLACK;

			while(!Arrays.equals(get, ProtocolOperators.YOU_WIN) ||
					!Arrays.equals(get, ProtocolOperators.YOU_LOSE) ||
					!Arrays.equals(get, ProtocolOperators.ILLEGAL_OP) ||
					!Arrays.equals(get, ProtocolOperators.UNKNOW_OP) ||
					!Arrays.equals(get, ProtocolOperators.GENERAL_ERROR))
			{
				check = get.clone();
				din.read(get);
				if(check.equals(get))
					handlingStuff();
			}
			
			
			if(Arrays.equals(get, ProtocolOperators.UNKNOW_OP))
					System.out.println("Unknown Operation... Closing Connection");
			else if(Arrays.equals(get, ProtocolOperators.ILLEGAL_OP))
					System.out.println("Illegal Operation... Closing Connection");
			else if(Arrays.equals(get, ProtocolOperators.GENERAL_ERROR))
					System.out.println("General Error detected... Closing Connection");
			else if(Arrays.equals(get, ProtocolOperators.YOU_WIN))
				System.out.println("YOU WIN, CONGRATULATIONS!");
			else if(Arrays.equals(get, ProtocolOperators.YOU_LOSE))
				System.out.println("YOU LOST, CONGRATULATIONS!");
			else
				System.out.println("Something unexpected happened... closing server");
			
			}//ack
			
			
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
	

	public static void main(String[] args) {
		// TODO Main-Methode
		TestClient c = new TestClient();
		c.StartUp();
	}

}
