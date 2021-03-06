package de.uni_jena.min.in0043.nine_mens_morris.net;

import java.io.*;
import java.net.*;
import java.util.Arrays;

import de.uni_jena.min.in0043.nine_mens_morris.core.Game;
import de.uni_jena.min.in0043.nine_mens_morris.core.Phase;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;
import de.uni_jena.min.in0043.nine_mens_morris.gui.Head;

public class TestClient extends Thread implements Game{

	Socket server;
	InputStream in;
	OutputStream out;
	DataInputStream din;
	DataOutputStream dout;
	byte[] get;
	byte[] send;
	public boolean white;
	public Head head;
	public boolean connected;
	
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
	
	public void run() {
		while(connected)
		{
			try {
				din.read(get);
				handlingStuff();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public synchronized int moveStone(int stone, int point) {
		try {
			System.out.println("Moving stone...");
			send = ProtocolOperators.MOVE_STONE;
			send[1] = (byte) stone;
			send[2] = (byte) point;
			dout.write(send);
			System.out.println("Reading...");
			din.readFully(get);
			System.out.println("Writing...");
			if(Arrays.equals(get, ProtocolOperators.ACK))
			{
				System.out.println("Moving was k!");
//				head.moveStone((int) send[1], (int) send[2], 0);
				return 1;
			}
			else if(Arrays.equals(get, ProtocolOperators.ACK_W_MILL))
			{
				System.out.println("Mill found");
				return 2;
			}
			else
			{
				System.out.println("Move was not okay!");
			}
		} catch (IOException e) {
			// TODO moveStone
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public synchronized int removeStone(int stone) {
		try {
			send = ProtocolOperators.REMOVE_STONE;
			send[1] = (byte) stone;
			send[2] = 0x00;
			dout.write(send);
			din.readFully(get);
			if(Arrays.equals(get, ProtocolOperators.ACK))
			{
				System.out.println("Worked");
//				din.readFully(get);
//				handlingStuff();
				return 1;
			}
			else
				{
				System.out.println("You can't remove this stone");
				return 0;
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
			dout.write(send);
			din.readFully(get);
			if(Arrays.equals(get, ProtocolOperators.ACK))
			{
				System.out.println("Conceeded!");
				din.readFully(get);
				handlingStuff();
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
			dout.write(send);
			
			din.readFully(get);
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
			dout.write(send);
			din.readFully(get);
			if(get[0] == 0)
				return Player.WHITE;
			else if(get[0] == 1) return Player.BLACK;
			else System.out.println("Error happened in active player! " + get[0] +
					" " + get[2]);
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
			dout.write(send);
			
			din.readFully(get);
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
			dout.write(send);
			
			din.readFully(get);
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
			dout.write(send);
			
			din.readFully(get);
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
			dout.write(send);
			
			din.readFully(get);
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
			dout.write(send);
			
			din.readFully(get);
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
			dout.write(send);
			
			din.readFully(get);
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
			dout.write(send);
			
			din.readFully(get);
			return (int) get[0];
		} catch (IOException e) {
			// TODO getRound
			e.printStackTrace();
		}
		return -1;
	}
	
	public boolean NEW_GAME()
	{
		try {
			send = ProtocolOperators.NEW_GAME;
			dout.write(send);
			
			din.readFully(get);
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
			dout.write(send);
			
			din.readFully(get);
			if(Arrays.equals(get, ProtocolOperators.HELLO))
				return true;
		} catch (IOException e) {
			// TODO NoMore
			e.printStackTrace();
		}
		return false;
	}

	private void handlingStuff() throws IOException {
		// TODO Implement moveStone.
		System.out.println("Handling stuff: " + get[0] + " " + get[1] + " "
				+ get[2] + " in " + head.getColour());
		if (get[0] == 1) {
			head.moveStone((int) get[1], (int) get[2]);
			System.out.println("Moved stone");
			System.out.println((int) get[1] + " " + (int) get[2]);
			head.repaint();
			dout.write(ProtocolOperators.ACK);
		} else if (get[0] == 2) {
			head.removeStone((int) get[1]);
			System.out.println("Removed stone");
			dout.write(ProtocolOperators.ACK);
		} else if (Arrays.equals(get, ProtocolOperators.CONCEDE)) {
			// TODO implement WIN method maybe
			System.out.println("WIN");
			head.setWinner(head.getColour());
		} else if (Arrays.equals(get, ProtocolOperators.NACK)) {
			System.out.println("Not acknowledged");
		} else if (Arrays.equals(get, ProtocolOperators.ACK)) {
			System.out.println("Acknowledged");
		} else if (Arrays.equals(get, ProtocolOperators.BYE)) {
			// TODO implement WIN method maybe
			System.out.println("User disconnected, you won.");
			head.setWinner(head.getColour());
		} else if (Arrays.equals(get, ProtocolOperators.NEW_GAME)) {
			head.reset();
			dout.write(ProtocolOperators.ACK);
		} else if (Arrays.equals(get, ProtocolOperators.UNKNOW_OP))
			System.out.println("Unknown Operation... Closing Connection");
		else if (Arrays.equals(get, ProtocolOperators.ILLEGAL_OP))
			System.out.println("Illegal Operation... Closing Connection");
		else if (Arrays.equals(get, ProtocolOperators.GENERAL_ERROR))
			System.out.println("General Error detected... Closing Connection");
		else if (Arrays.equals(get, ProtocolOperators.YOU_WIN)) {
			System.out.println("YOU WIN, CONGRATULATIONS!");
			head.setWinner(head.getColour());
		}
		else if (Arrays.equals(get, ProtocolOperators.YOU_LOSE)) {
			System.out.println("YOU LOST, CONGRATULATIONS!");
			if(head.getColour() == Player.BLACK)
				head.setWinner(Player.WHITE);
			else head.setWinner(Player.BLACK);
		}
		else if (Arrays.equals(get, ProtocolOperators.MILL_CREATED)) {
			System.out.println("Other player has a mill...");
			din.readFully(get);
			handlingStuff();
			din.readFully(get);
			handlingStuff();
		}
		else if (Arrays.equals(get, ProtocolOperators.ACK_W_MILL)) {
			System.out.println("Mill found");
//			din.readFully(get);
//			handlingStuff();
		} else {
			System.out
					.println("Something unexpected happened... closing server");
			System.out.println(get[0] + " " + get[1] + " " + get[2] + " in "
					+ head.getColour());
		}
	}
	
	public void iNeedtoRead()
	{
		try {
			System.out.println("I'm reading");
			din.readFully(get);
			handlingStuff();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Couldn't read ;_;");
			e.printStackTrace();
		}
	}
	
	private void StartUp() {
		try {
			System.out.println("Connecting...");
			send = ProtocolOperators.HELLO;
			System.out.println("sending data...");
			dout.write(send);
			System.out.println("Data sent!");
			din.readFully(get);
			System.out.println("Data recieved " + get[0]);
			if(Arrays.equals(get, ProtocolOperators.ACK)) {
			
//			if(Arrays.equals(get, ProtocolOperators.HELLO)) System.out.println("Connection established!");
//			else {
//				System.out.println("Error! Connecting failed! " + get[0]);
//				servers.close();
//			}
			head.init();

			din.readFully(get);
			
			if(Arrays.equals(get, ProtocolOperators.IS_WHITE))
			{
				head.setColour(Player.WHITE);
				System.out.println("I am less pigmented");
				dout.write(ProtocolOperators.ACK);
			}
			else if(Arrays.equals(get, ProtocolOperators.IS_BLACK))
			{
				head.setColour(Player.BLACK);
				System.out.println("The black one always dies first, GG");
				dout.write(ProtocolOperators.ACK);
				din.readFully(get);
				handlingStuff();
			}
			connected = true;
			
			while(connected)
			{			}
			
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

	public void disconnect() {
		try {
			send = ProtocolOperators.BYE;
			dout.write(send);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
