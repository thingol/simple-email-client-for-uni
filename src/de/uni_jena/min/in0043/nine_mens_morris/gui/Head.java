package de.uni_jena.min.in0043.nine_mens_morris.gui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.core.Game;
import de.uni_jena.min.in0043.nine_mens_morris.core.GameClient;
import de.uni_jena.min.in0043.nine_mens_morris.core.Logic;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;
import de.uni_jena.min.in0043.nine_mens_morris.net.Client;

public class Head extends Panel implements MouseListener, GameClient {

	private static final long serialVersionUID = -5704850734397028920L;
	private static Logger log = LogManager.getLogger();
	private Spielfeld sF;
	private Stone[] black;
	private Stone[] white;
	private Frame mFra;
	private JFrame debugFrame;
	private JLabel mouseClick, offsetMouseClick;
	private Game game;
	private boolean mill;
	private boolean donePlacing = true;
	private Player color;
	private Player winner;
	private int globalRetVal = 0;
	private boolean debug = true;
	
	private int width = 1024;
	private int height = 720;
	private int radius = width * height / 30000;

	public Head() {
		this(new Logic());
		this.color = Player.WHITE;
	}

	public Head(Game game) {
		mFra = new Frame("Nine Men's Morris - Retro Style");
		mFra.setSize(width, height);
		sF = new Spielfeld();
		black = new Stone[9];
		white = new Stone[9];
		mill = false;
		this.game = game;
		
		if(debug) {
			long ts = System.currentTimeMillis();
			mFra.setTitle(mFra.getTitle() + ", " + ts);
			mouseClick = new JLabel();
			offsetMouseClick = new JLabel();
			
			debugFrame = new JFrame("Debug frame, " + ts);
			debugFrame.setSize(200, 200);
			debugFrame.setLayout(new GridLayout(3,2));
			debugFrame.add(new JLabel("ID:"));
			debugFrame.add(new JLabel(""+ts));
			debugFrame.add(new JLabel("mouse clicked at:"));
			debugFrame.add(mouseClick);
			debugFrame.add(new JLabel("offset click:"));
			debugFrame.add(offsetMouseClick);
			debugFrame.setVisible(true);
		}

		setUpStones();
	}
	
	private void setUpStones() {
		log.trace("setting up stones, radius: " + radius + ", width: " + width + ",height: " + height);
		for (int i = 0; i < 9; i++) {
			black[i] = new Stone(i+9,40 + i * radius, height - 50);
			white[i] = new Stone(i,width - 80 - i * radius, height - 50);
		}
	}

	public synchronized void reset() {
		log.entry();
		width = mFra.getSize().width;
		height = mFra.getSize().height;
		radius =  width * height / 30000;
		log.trace("width: "+ width + ",height: " + height + ",radius: " + radius);
		sF.reset();
		mill = false;
		
		setUpStones();
	}

	public void paint(Graphics g) {
		height = mFra.getSize().height;
		width = mFra.getSize().width;
		radius = width * height / 30000;
		int middleX = width / 2;
		int middleY = height / 2;
		int addX = middleX - middleX / 4;
		int addY = middleY - middleY / 4;

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
			g.drawLine(middleX, middleY / 4, middleX, middleY / 4 + 2 * addY / 3);
			g.drawLine(middleX, middleY + middleY / 4, middleX, middleY
					+ (middleY / 4 + 2 * addY / 3));
			// X Lines
			g.drawLine(middleX / 4, middleY, middleX / 4 + 2 * addX / 3, middleY);
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
			g.drawLine(middleX + 1, middleY / 4, middleX + 1, middleY / 4 + 2 * addY / 3);
			g.drawLine(middleX + 2, middleY / 4, middleX + 2, middleY / 4 + 2 * addY / 3);
			g.drawLine(middleX + 1, middleY + middleY / 4, middleX + 1, middleY
					+ (middleY / 4 + 2 * addY / 3));
			g.drawLine(middleX + 2, middleY + middleY / 4, middleX + 2, middleY
					+ (middleY / 4 + 2 * addY / 3));
			g.drawLine(middleX / 4, middleY + 1, middleX / 4 + 2 * addX / 3, middleY + 1);
			g.drawLine(middleX / 4, middleY + 2, middleX / 4 + 2 * addX / 3, middleY + 2);
			g.drawLine(middleX + middleX / 4, middleY + 1, middleX + middleX
					/ 4 + 2 * addX / 3, middleY + 1);
			g.drawLine(middleX + middleX / 4, middleY + 2, middleX + middleX
					/ 4 + 2 * addX / 3, middleY + 2);

		}

		for (int i = 0; i < 9; i++) {
			g.fillOval(black[i].getX(), black[i].getY(), radius, radius);
			g.drawOval(white[i].getX(), white[i].getY(), radius, radius);
		}
		
		sF.places(mFra);

		if(winner != null) {
			g.drawString(winner.name() + " won! Congratulations!", (width/2) - 50 , height - 50);
			
		} else if(mill)	{
			g.drawString("Mill found! Take a stone!", (width/2) - 50 , height - 50);
			
		} else {
			if(color != null) {
				g.drawString("I am " + color.name() + "!", (width/2) - 50 , 50);
			}
			g.drawString("Someone's turn! Make a move.", (width/2) - 50 , height - 50);
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
					((Client)game).disconnect();
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
						((Client)game).conceed(true);
					}
					else if(n2 == 1)
					{
						((Client)game).disconnect();
						((Client)game).conceed(false);
						System.exit(0);
					}
				}
			}
		});
	}

	public void moveS(MouseEvent e) {
		log.entry(e);
		
		Stone[] st;
		boolean stoneSelected = true;
		int x = e.getX() - (radius/2);
		int y = e.getY() - (radius/2);
		int xMax = x + (radius/2);
		int xMin = x - (radius/2);
		int yMax = y + (radius/2);
		int yMin = y - (radius/2);
		
		if(debug) {
			mouseClick.setText(String.format("(%d,%d)", e.getX(), e.getY()));
			offsetMouseClick.setText(String.format("(%d,%d)", x, y));
		}
		
		log.trace(xMin + " <= posX <= " + xMax);
		log.trace(yMin + " <= posY <= " + yMax);
		log.trace("you clicked at (" + x + "," + y + ")");
		if (color == Player.WHITE) {
			st = white;
		} else {
			st = black;
		}
		
		for(Stone s : black) {
			if(!s.placed()) {
				donePlacing = false;
				log.trace("we're not done placing stones");
				break;
			}
		}

		if (!donePlacing) {
			for (Stone stone : st) {
				// This sets the stone to a corner
				if (stone.inPlacement() == true && stone.placed() == false) { // Did I choose a piece?
					//Is it already placed?
					for (int l = 0; l < 24; l++) {
						for (int j = 0; j < radius; j++) {
							for (int k = 0; k < radius; k++) {
								if (x == sF.placement[l][0] - radius + j
										&& y == sF.placement[l][1] - radius
										+ k && sF.placed[l] == false) {
									
									globalRetVal = game.moveStone(stone.getID(), l);
									
									if (globalRetVal > 0) {
										stone.inPlacement(false);
										stone.setX(sF.placement[l][0] - (radius/2));
										stone.setY(sF.placement[l][1] - (radius/2));
										sF.placed[l] = true;
										stone.placedAt(l);
										stoneSelected = true;
										if (globalRetVal == 2)
											mill = true;
										break;
									}
								}
							}
						}
					}//for l
					log.trace("stone is at (" + stone.getX() + "," + stone.getY() + ")");
				} //Stone is placed
				//Still in placing Stones for i loop

				// This just wants to know if one stone has been selected yet
				for (Stone s : st) {
					if (s.inPlacement() == true) {
						stoneSelected = false;
						break;
					}
				}

				// Selects the Stone
				if (!stone.inPlacement() && stoneSelected == true && !stone.placed()) {
					int stoneX = stone.getX();
					int stoneY = stone.getY();
					if (stoneX > xMin && stoneX < xMax) {
						if (stoneY >= yMin && stoneY <= yMax && !stone.inPlacement()) { // else this function is not deterministic
							stone.inPlacement(true);
							stone.mark();
						}
					}
				}// if
				//Now we have everything done: Stone can be selected and moved
			}// forI
		} else {

			for(Stone stone : st) {
				if(stone.inPlacement() == true)
				{  //Did I choose a piece?            Is it already placed?
					for(int l = 0; l < 24; l++)
					{
						for(int j = 0; j < radius; j++)
						{
							for(int k = 0; k < radius; k++)
							{
								if(x == sF.placement[l][0] - radius + j && y == sF.placement[l][1] -radius + k)
								{

									if(color == Player.WHITE) {
										globalRetVal = game.moveStone(stone.getID(), l);
									} else {
										globalRetVal = game.moveStone(stone.getID()+9, l);
									}

									if (globalRetVal > 0) {
										stone.setX(sF.placement[l][0] - radius);
										stone.setY(sF.placement[l][1] - radius);
										sF.placed[l] = true;
										stone.placedAt(l);
										stoneSelected = true;
										if (globalRetVal == 2)
											mill = true;
										repaint();
									} else {
										stone.inPlacement(false);
										stone.unMark();
										log.error("ERROR!");
									}
									stone.inPlacement(false);
									log.trace("stone.inPlacement() => " + stone.inPlacement());
								}
							}
						}
					}
					break;
				}
				// This just wants to know if one stone has been selected yet
				for (Stone s : st) {
					if (s.inPlacement() == true) {
						stoneSelected = false;
						break;
					}
				}

				// Selects the Stone
				if (stone.inPlacement() == false && stoneSelected == true) {
					for (int j = 0; j < radius; j++) {
						for (int k = 0; k < radius; k++) {
							if (x == stone.getX() + j) {
								if (y == stone.getY() + k
										&& stone.inPlacement() == false) {
									//else this function would trigger set(5) more often
									stone.inPlacement(true);
									stone.mark();
								}
							}
						}
					}// forJ
				}// if

			}// forI
		} //else
		repaint();
	}

	public void removeStone(MouseEvent e) {
		log.entry();
		
		
		int x = e.getX() - (radius/2);
		int y = e.getY() - (radius/2);
		
		if(debug) {
			mouseClick.setText(String.format("(%d,%d)", e.getX(), e.getY()));
			offsetMouseClick.setText(String.format("(%d,%d)", x, y));
		}

		Stone[] st;
		if (color == Player.BLACK) {
			st = white;
		} else
			st = black;

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < radius; j++) {
				for (int k = 0; k < radius; k++) {
					if (x == st[i].getX() + j && y == st[i].getY() + k) {
						int m2;
						if(color == Player.WHITE)
							m2 = game.removeStone(i+9);
						else m2 = game.removeStone(i);
						if (m2 == 1) {
							sF.placed[st[i].placedAt()] = false;
							st[i].hide();
							mill = false;
						}
					}
				}
			}
		}
		repaint();
	}

	public synchronized void moveStone(int stone, int goal)
	{
		if (color == Player.BLACK) {
			white[stone].setX(sF.placement[goal][0]- (radius/2));
			white[stone].setY(sF.placement[goal][1]- (radius/2));
			white[stone].placedAt(goal);
		} else {
			black[stone-9].setX(sF.placement[goal][0] - (radius/2));
			black[stone-9].setY(sF.placement[goal][1] - (radius/2));
			black[stone-9].placedAt(goal);
		}
		sF.placed[goal] = true;

		repaint();
	}

	public synchronized void removeStone(int stone)	{
		log.trace("removing stone " + stone);
		if (color == Player.WHITE) {
			white[stone].hide();
			sF.placed[white[stone].placedAt()] = false;
		} else {
			black[stone-9].hide();
			sF.placed[black[stone-9].placedAt()] = false;
		}
		repaint();
	}

	public synchronized boolean newGame(boolean win){
		if(win = false) {
			if(color == Player.WHITE) {
				winner = Player.BLACK;
			} else {
				winner = Player.WHITE;
			}
		} else { 
			winner = color;
		}
		
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
		if(winner != null)
		{
			repaint();
		}
		else if (mill) {
			removeStone(e);
			repaint();
		} else {
			moveS(e);
			repaint();
			this.paint(mFra.getGraphics());
			if( globalRetVal == 1 && !mill) { globalRetVal = 0; }
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColour(Player colour) {
		this.color = colour;
		repaint();
	}


}
