package de.uni_jena.min.in0043.nine_mens_morris.gui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.*;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.core.*;
import de.uni_jena.min.in0043.nine_mens_morris.net.*;

public class Head extends Panel implements MouseListener, GameClient {

	private static final long serialVersionUID = -5704850734397028920L;
	private static Logger log = LogManager.getLogger();
	private Spielfeld sF;
	private Stone[] Black;
	private Stone[] White;
	private Frame mFra;
	private Game nmm;
	private boolean mill;
	public Player color = Player.WHITE;
	public byte winner = -1;
	int z = 0;

	public Head() {
		mFra = new Frame("Nine Men's Morris - Retro Style");
		mFra.setSize(1024, 720);
		sF = new Spielfeld();
		Black = new Stone[9];
		White = new Stone[9];
		mill = false;
		nmm = new TestClient();

		for (int i = 0; i < 9; i++) {
			int r = mFra.getSize().width * mFra.getSize().height / 30000;
			Black[i] = new Stone();
			Black[i].posX = 40 + i * r;
			Black[i].posY = mFra.getSize().height - 80;

			White[i] = new Stone();
			White[i].posX = mFra.getSize().width - 80 - i * r;
			White[i].posY = mFra.getSize().height - 80;
		}
	}

	public Head(Game nnn) {
		mFra = new Frame("Nine Men's Morris - Retro Style");
		mFra.setSize(1024, 720);
		sF = new Spielfeld();
		Black = new Stone[9];
		White = new Stone[9];
		mill = false;
		nmm = nnn;

		for (int i = 0; i < 9; i++) {
			int r = mFra.getSize().width * mFra.getSize().height / 30000;
			Black[i] = new Stone();
			Black[i].posX = 40 + i * r;
			Black[i].posY = mFra.getSize().height - 50;

			White[i] = new Stone();
			White[i].posX = mFra.getSize().width - 80 - i * r;
			White[i].posY = mFra.getSize().height - 50;
		}
	}
	
	public synchronized void reset()
	{
		sF.reset();
		mill = false;
		for (int i = 0; i < 9; i++) {
			Black[i].reset();
			White[i].reset();
			int r = mFra.getSize().width * mFra.getSize().height / 30000;
			Black[i] = new Stone();
			Black[i].posX = 40 + i * r;
			Black[i].posY = mFra.getSize().height - 80;

			White[i] = new Stone();
			White[i].posX = mFra.getSize().width - 80 - i * r;
			White[i].posY = mFra.getSize().height - 80;
		}
	}

	public void paint(Graphics g) {
		int height = mFra.getSize().height;
		int width = mFra.getSize().width;
		int middleX = width / 2;
		int middleY = height / 2;
		int addX = middleX - middleX / 4;
		int addY = middleY - middleY / 4;
		int r = width * height / 30000;

		/*
		 * for (int i = 0; i < 9; i++) { Black[i].posX = 40 + i * r;
		 * Black[i].posY = mFra.getSize().height - 80; White[i].posX =
		 * mFra.getSize().width - 80 - i * r; White[i].posY =
		 * mFra.getSize().height - 80; }
		 */

		{// Board
			// Outer
			g.drawRect(middleX / 4, middleY / 4, (addX) * 2, (addY) * 2);
			// Middle
			g.drawRect(middleX / 4 + addX / 3, middleY / 4 + addY / 3,
					(middleX - (middleX / 4 + addX / 3)) * 2,
					(middleY - (middleY / 4 + addY / 3)) * 2);
			// Inner
			g.drawRect(middleX / 4 + 2 * addX / 3, middleY / 4 + 2 * addY / 3,
					(middleX - (middleX / 4 + 2 * addX / 3)) * 2,
					(middleY - (middleY / 4 + 2 * addY / 3)) * 2);

			// g.drawLine(middleX, middleY, middleX, middleY);
			// Y Lines
			g.drawLine(middleX, middleY / 4, middleX, middleY / 4 + 2 * addY
					/ 3);
			g.drawLine(middleX, middleY + middleY / 4, middleX, middleY
					+ (middleY / 4 + 2 * addY / 3));
			// X Lines
			g.drawLine(middleX / 4, middleY, middleX / 4 + 2 * addX / 3,
					middleY);
			g.drawLine(middleX + middleX / 4, middleY, middleX + middleX / 4
					+ 2 * addX / 3, middleY);

			// Now, this is getting fat
			g.drawRect(middleX / 4 + 1, middleY / 4 + 1, (addX) * 2, (addY) * 2);
			g.drawRect(middleX / 4 + 2, middleY / 4 + 2, (addX) * 2, (addY) * 2);
			g.drawRect(middleX / 4 + addX / 3 + 1, middleY / 4 + addY / 3 + 1,
					(middleX - (middleX / 4 + addX / 3)) * 2,
					(middleY - (middleY / 4 + addY / 3)) * 2);
			g.drawRect(middleX / 4 + addX / 3 + 2, middleY / 4 + addY / 3 + 2,
					(middleX - (middleX / 4 + addX / 3)) * 2,
					(middleY - (middleY / 4 + addY / 3)) * 2);
			g.drawRect(middleX / 4 + 2 * addX / 3 + 1, middleY / 4 + 2 * addY
					/ 3 + 1, (middleX - (middleX / 4 + 2 * addX / 3)) * 2,
					(middleY - (middleY / 4 + 2 * addY / 3)) * 2);
			g.drawRect(middleX / 4 + 2 * addX / 3 + 2, middleY / 4 + 2 * addY
					/ 3 + 2, (middleX - (middleX / 4 + 2 * addX / 3)) * 2,
					(middleY - (middleY / 4 + 2 * addY / 3)) * 2);
			g.drawLine(middleX + 1, middleY / 4, middleX + 1, middleY / 4 + 2
					* addY / 3);
			g.drawLine(middleX + 2, middleY / 4, middleX + 2, middleY / 4 + 2
					* addY / 3);
			g.drawLine(middleX + 1, middleY + middleY / 4, middleX + 1, middleY
					+ (middleY / 4 + 2 * addY / 3));
			g.drawLine(middleX + 2, middleY + middleY / 4, middleX + 2, middleY
					+ (middleY / 4 + 2 * addY / 3));
			g.drawLine(middleX / 4, middleY + 1, middleX / 4 + 2 * addX / 3,
					middleY + 1);
			g.drawLine(middleX / 4, middleY + 2, middleX / 4 + 2 * addX / 3,
					middleY + 2);
			g.drawLine(middleX + middleX / 4, middleY + 1, middleX + middleX
					/ 4 + 2 * addX / 3, middleY + 1);
			g.drawLine(middleX + middleX / 4, middleY + 2, middleX + middleX
					/ 4 + 2 * addX / 3, middleY + 2);
			
		}

		for (int i = 0; i < 9; i++) {
			if (Black[i] != null && White[i] != null) {
				g.fillOval(Black[i].posX, Black[i].posY, r, r);
				g.drawOval(White[i].posX, White[i].posY, r, r);
			} else
				;
		}
		sF.places(mFra);
		
		if(winner == 0 || winner == 1)
		{
			String s;
			if(winner == 0) {
				if(color == Player.WHITE)
					s = "Black";
				else s = "White";
			}
			else {
				if(color == Player.WHITE)
					s = "White";
				else s = "Black";
			}
			g.drawString(s + " won! Congratulations!", (mFra.getSize().width/2) - 50 , mFra.getSize().height - 50);
		}
		else if(mill)
		{
			g.drawString("Mill found! Take a stone!", (mFra.getSize().width/2) - 50 , mFra.getSize().height - 50);
		}
		else
		{
			String z;
			if(color == Player.BLACK)
				z = "Black";
			else z = "White";
			g.drawString("I am " + z + "!", (mFra.getSize().width/2) - 50 , 50);
			g.drawString("Someone's turn! Make a move.", (mFra.getSize().width/2) - 50 , mFra.getSize().height - 50);
		}
	}

	public static void main(String args[]) {
		new Head().BuildUp();
	}

	public void BuildUp() {

		mFra.add(this);
		mFra.setForeground(new Color(255, 134, 13));
		mFra.setBackground(new Color(94, 47, 0));
		sF.places(mFra);

		this.addMouseListener(this);
		mFra.setVisible(true);

		mFra.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				Object[] options = {"Disconnect!",
	                    "Concede!",
	                    "Stahp!"};
	int n = JOptionPane.showOptionDialog(mFra,
	    "Do you want to disconnect or to concede?",
	    "Exit Dialog",
	    JOptionPane.YES_NO_CANCEL_OPTION,
	    JOptionPane.QUESTION_MESSAGE,
	    null,
	    options,
	    options[2]);
	if(n == 0)
	{
		nmm.disconnect();
		System.exit(0);
	}
	else if(n == 1)
	{
		Object[] opt = {"Yes",
                "No",
                "Stahp!"};
		int n2 = JOptionPane.showOptionDialog(mFra,
				"Do you want to start a new Game?",
				"Concede Dialog",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				opt,
				opt[2]);
		if(n2 == 0)
		{
			// TODO Client Reset
			nmm.conceed(true);
		}
		else if(n2 == 1)
		{
			nmm.disconnect();
			nmm.conceed(false);
			System.exit(0);
		}
	}
			}
		});
	}

	public void moveS(MouseEvent e) {
		Stone[] st = new Stone[9];
		boolean yep = true;
		int r = mFra.getSize().width * mFra.getSize().height / 30000 / 2;
		int x = e.getX();
		int y = e.getY();
		if (color == Player.WHITE) {
			st = White;
		} else {
			st = Black;
		}
		int first_or_other_phase = 0;
		 //Which Stone do we want to move?
		for(int iter = 0; iter < Black.length; iter++)
		{
			if(Black[iter].placed)
				first_or_other_phase++;
		}

		if (first_or_other_phase < 9) {
			for (int i = 0; i < 9; i++) {

				// This sets the stone to a corner
				if (st[i].inPlacement == true && st[i].placed == false) { // Did I choose a piece?
																		  //Is it already placed?
					for (int l = 0; l < 24; l++) {
						for (int j = 0; j < 2 * r; j++) {
							for (int k = 0; k < 2 * r; k++) {
								if (e.getX() == sF.placement[l][0] - r + j
										&& e.getY() == sF.placement[l][1] - r
												+ k && sF.placed[l] == false) {
									int s;
									if (color == Player.WHITE) {
										s = i;
									}
									else {
										s = i + 9;
									}
									z = nmm.moveStone(s, l);
									if (z > 0) {
										st[i].inPlacement = false;
										st[i].posX = sF.placement[l][0] - r;
										st[i].posY = sF.placement[l][1] - r;
										sF.placed[l] = true;
										st[i].placedAt = l;
										st[i].placed = true;
										yep = true;
										if (z == 2)
											mill = true;
										break;
									}
								}
							}
						}
					}//for l
				} //Stone is placed
				//Still in placing Stones for i loop
				
				// This just wants to know if one stone has been selected yet
				for (int j = 0; j < 9; j++) {
					if (st[j].inPlacement == true)
						yep = false;
				}

				// Selects the Stone
				if (st[i].inPlacement == false && yep == true
						&& st[i].placed == false) {
					for (int j = 0; j < 2 * r; j++) {
						for (int k = 0; k < 2 * r; k++) {
							if (x == st[i].posX + j) {
								if (y == st[i].posY + k
										&& st[i].inPlacement == false) { // else this function is not deterministic
									st[i].inPlacement = true;
									st[i].set(5);
								}
							}
						}
					}// forJ
				}// if
				//Now we have everything done: Stone can be selected and moved
			}// forI
		}//Phase.End	
					
		//Every other phase
		else {
			
			for(int i = 0; i < 9; i++)
			{
				if(st[i].inPlacement == true)
				{  //Did I choose a piece?            Is it already placed?
					for(int l = 0; l < 24; l++)
					{
						for(int j = 0; j < 2*r; j++)
						{
							for(int k = 0; k < 2*r; k++)
							{
								if(e.getX() == sF.placement[l][0] - r + j && e.getY() == sF.placement[l][1] -r + k)
								{
									int s;
									if(color == Player.WHITE) s = i;
									else
										s = i + 9;
									z = nmm.moveStone(s, l);
									if (z > 0) {
										st[i].inPlacement = false;
										st[i].posX = sF.placement[l][0] - r;
										st[i].posY = sF.placement[l][1] - r;
										sF.placed[l] = true;
										st[i].placedAt = l;
										st[i].placed = true;
										yep = true;
										log.trace("st[i].inPlacement: " + st[i].inPlacement);
										if (z == 2)
											mill = true;
										repaint();
									} else {
										st[i].inPlacement = false;
										st[i].set(-5);
										log.error("ERROR!");
										log.trace("st["+i+"].inPlacement: " + st[i].inPlacement);
									}
								}
							}
						}
					}
				}
				// This just wants to know if one stone has been selected yet
				for (int j = 0; j < 9; j++) {
					if (st[j].inPlacement == true)
						yep = false;
				}

				// Selects the Stone
				if (st[i].inPlacement == false && yep == true) {
					for (int j = 0; j < 2 * r; j++) {
						for (int k = 0; k < 2 * r; k++) {
							if (x == st[i].posX + j) {
								if (y == st[i].posY + k
										&& st[i].inPlacement == false) {
									//else this function would trigger set(5) more often
									st[i].inPlacement = true;
									st[i].set(5);
								}
							}
						}
					}// forJ
				}// if

			}// forI
		} //else
	}

	public void removeStone(MouseEvent e) {
		Stone[] st = new Stone[9];
		if (color == Player.BLACK) {
			st = White;
		} else
			st = Black;
		int r = mFra.getSize().width * mFra.getSize().height / 30000 / 2;

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 2 * r; j++) {
				for (int k = 0; k < 2 * r; k++) {
					if (e.getX() == st[i].posX + j
							&& e.getY() == st[i].posY + k) {
						int m2;
						if(color == Player.WHITE)
							m2 = nmm.removeStone(i+9);
						else m2 = nmm.removeStone(i);
						if (m2 == 1) {
							sF.placed[st[i].placedAt] = false;
							st[i].placedAt = -2;
							st[i].set(-50, -50);
							mill = false;
						}
					}
				}
			}
		}
	}
	
	public synchronized void moveStone(int stone, int goal)
	{
		int r = mFra.getSize().width * mFra.getSize().height / 30000 / 2;
		if (color == Player.WHITE) {
			 Black[stone-9].posX = sF.placement[goal][0]-r;
			 Black[stone-9].posY = sF.placement[goal][1]-r;
			 Black[stone-9].placed = true;
			 Black[stone-9].placedAt = goal;
		}
		else
		{ White[stone].posX = sF.placement[goal][0] -r;
		 White[stone].posY = sF.placement[goal][1] -r;
		 White[stone].placed = true;
		 White[stone].placedAt = goal;}
		sF.placed[goal] = true;
		repaint();
	}
	
	public synchronized void removeStone(int stone)
	{
		if (color == Player.WHITE) {
		White[stone].posX = -50;
		White[stone].posY = -50; }
		else {
			Black[stone-9].posX = -50;
			Black[stone-9].posY = -50; }
		repaint();
	}
	
	public synchronized boolean newGame(boolean win){
		if(win = false) winner = 0;
		else winner = 1;
		repaint();
		Object[] options = {"Yes, please",
                "No",};
	int n = JOptionPane.showOptionDialog(mFra,
	    "Do you to start a new Game?",
	    "New Game Dialog",
	    JOptionPane.YES_NO_CANCEL_OPTION,
	    JOptionPane.QUESTION_MESSAGE,
	    null,
	    options,
	    options[1]);
	if(n == 0)
		return true;
	else return false;
	}
	
	public synchronized void noMore() {
		JOptionPane.showMessageDialog(mFra,
			    "User denied. Disconnecting...",
			    "No more!",
			    JOptionPane.PLAIN_MESSAGE);
		System.exit(0);
	}

	public void mouseClicked(MouseEvent e) {
		log.trace("did we get a mill:" + mill);
//Logic now checks if players can move at all, calls game over if not
		if(winner != -1)
		{
			repaint();
		}
		else if (mill) {
			removeStone(e);
			repaint();
			if(!mill) nmm.iNeedtoRead();
		} else {
				moveS(e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				repaint();
				this.paint(mFra.getGraphics());
				if( z == 1 && !mill) { z = 0; nmm.iNeedtoRead(); }
		}

}
	
	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}
