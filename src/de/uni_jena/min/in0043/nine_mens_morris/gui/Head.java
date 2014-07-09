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
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.core.Game;
import de.uni_jena.min.in0043.nine_mens_morris.core.TestLogIn;
import de.uni_jena.min.in0043.nine_mens_morris.core.Logic;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;
import de.uni_jena.min.in0043.nine_mens_morris.net.LogInClient;
import de.uni_jena.min.in0043.nine_mens_morris.test.ToyBoard;

public class Head extends Panel implements MouseListener, TestLogIn {

	private static final long serialVersionUID = -5704850734397028920L;
	private static Logger log = LogManager.getLogger();
	private Spielfeld sF;
	private Stone[] black;
	private Stone[] white;
	private Frame mFra;
	
	private Game game;
	private boolean mill;
	private boolean donePlacing = false;
	private Player colour;
	private Player winner;
	private int globalRetVal = 0;
	private boolean sessionOver = false;
	
	private boolean debug = true;
	private JFrame debugFrame;
	private JPanel mousePanel,mainPanel;
	private JLabel mouseClick, offsetMouseClick;
	private ToyBoard board;
	
	private int width = 1024;
	private int height = 720;
	private int radius = width * height / 30000;

	public Head() {
		this(new Logic());
		this.colour = Player.WHITE;
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
			
			mainPanel = new JPanel();
			mousePanel = new JPanel();
			board = new ToyBoard();
			
			
			mousePanel.setLayout(new GridLayout(3,2));
			mousePanel.add(new JLabel("ID:"));
			mousePanel.add(new JLabel(""+ts));
			mousePanel.add(new JLabel("mouse clicked at:"));
			mousePanel.add(mouseClick);
			mousePanel.add(new JLabel("offset click:"));
			mousePanel.add(offsetMouseClick);
			
			mainPanel.add(mousePanel);
			mainPanel.add(board);
			
			debugFrame = new JFrame("Debug frame, " + ts);
			debugFrame.setSize(300, 200);
			debugFrame.add(mainPanel);
			debugFrame.setVisible(true);
		}

		setUpStones();
	}
	
	private void setUpStones() {
		log.entry();
		log.debug("setting up stones, radius: " + radius + ", width: " + width + ",height: " + height);
		for (int i = 0; i < 9; i++) {
			black[i] = new Stone(i+9,40 + i * radius, height - 50);
			white[i] = new Stone(i,width - 80 - i * radius, height - 50);
		}
		log.exit();
	}

	public synchronized void reset() {
		log.entry();
		width = mFra.getSize().width;
		height = mFra.getSize().height;
		radius =  width * height / 30000;
		log.debug("width: "+ width + ",height: " + height + ",radius: " + radius);
		sF.reset();
		mill = false;
		colour = null;
		winner = null;
		
		setUpStones();
		repaint();
		log.exit();
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
		for(int i = 0; i < sF.placed.length; i++) {
			board.p[i].setText(""+sF.placed[i]);
		}
			
		
		if(winner != null) {
			String victoryInfo;
			if(winner == colour) {
				victoryInfo = "You won! ";
				if(sessionOver) {
					victoryInfo += "The loser has left. You may now quit ;)";
				} else {
					victoryInfo += "Perhaps the loser want another go.";
				}
			} else {
				victoryInfo = "Dang! You lost :(";
			}
			
			g.drawString(victoryInfo, (width/2) - 50 , height - 50);
			
		} else if(mill)	{
			g.drawString("Mill found! Take a stone!", (width/2) - 50 , height - 50);
			
		} else {
			if(colour != null) {
				g.drawString("I am " + colour.name() + "!", (width/2) - 50 , 50);
			}
			//g.drawString("Someone's turn! Make a move.", (width/2) - 50 , height - 50);
		}
	}

	public void init() {

		mFra.add(this);
		mFra.setForeground(new Color(255, 134, 13));
		mFra.setBackground(new Color(94, 47, 0));
		sF.places(mFra);
		
		/*if(debug) {
			for(int i = 0; i < 24; i++) {
				board.p[i].setText("(" + sF.placement[i][0] + "," + sF.placement[i][1] + ")");
			}
		}*/

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
					log.debug("Disconnecting from server and exiting");
					((LogInClient)game).disconnect();
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
						log.debug("conceding game and requesting new");
						((LogInClient)game).conceed(true);
					}
					else if(n2 == 1)
					{
						log.debug("conceding game and exiting");
						((LogInClient)game).conceed(false);
						System.exit(0);
					}
				}
			}
		});
	}

	public void moveS(MouseEvent e) {
		log.entry();
		
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
		
		if (colour == Player.WHITE) {
			st = white;
		} else {
			st = black;
		}

		if (!donePlacing) {
			boolean gotNone = true;
			for(Stone s : black) {
				if(!s.used()) {
					donePlacing = false;
					log.debug("we're not done placing stones");
					gotNone = false;
					break;
				}
			}
			
			if(gotNone) {
				log.debug("done placing stones");
				donePlacing = true;
			}
			
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
										stone.used(true);
										stoneSelected = true;
										if (globalRetVal == 2)
											mill = true;
										break;
									}
								}
							}
						}
					}//for l
					log.debug("stone is at (" + stone.getX() + "," + stone.getY() + ")");
				} //Stone is placed

				log.debug("checking for selected stones");
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
					if (stoneX >= xMin && stoneX <= xMax) {
						if (stoneY >= yMin && stoneY <= yMax && !stone.inPlacement()) { // else this function is not deterministic
							stone.inPlacement(true);
							stone.mark();
							log.debug("stone " + stone.getID() + " selected");
						}
					}
				}// if
				//Now we have everything done: Stone can be selected and moved
			}// forI
		} else {

			for(Stone stone : st) {
				if(stone.inPlacement() == true)
				{  //Did I choose a piece?            Is it already placed?
					log.debug(stone.getID() + " might be suitable"); 
					for(int l = 0; l < 24; l++)
					{
						int placeX = sF.placement[l][0];
						int placeY = sF.placement[l][1];
						if(placeX >= xMin  && placeX <= xMax && placeY >= yMin  && placeY <= yMax) {
							log.debug("calling game.moveStone(stone, place)");
							globalRetVal = game.moveStone(stone.getID(), l);
							if (globalRetVal > 0) {
								stone.setX(sF.placement[l][0] - (radius/2));
								stone.setY(sF.placement[l][1] - (radius/2));
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
							log.debug("stone.inPlacement() => " + stone.inPlacement());
						}
					}

				}
				// This just wants to know if one stone has been selected yet
				for (Stone s : st) {
					if (s.inPlacement() == true) {
						stoneSelected = false;
						break;
					}
				}

				// Selects the Stone
				if (!stone.inPlacement() && stoneSelected == true) {
					int stoneX = stone.getX();
					int stoneY = stone.getY();
					if (stoneX >= xMin && stoneX <= xMax && stoneY >= yMin && stoneY <= yMax
							&& !stone.inPlacement()) { // else this function is not deterministic
							stone.inPlacement(true);
							stone.mark();
							log.debug("stone " + stone.getID() + " selected");
					}
				}
			}// forI
		} //else
		repaint();
	}

	public void removeS(MouseEvent e) {
		log.entry(e);
		
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

		Stone[] st;
		if (colour == Player.BLACK) {
			st = white;
		} else
			st = black;

		for (Stone stone : st) {
			int stoneX = stone.getX();
			int stoneY = stone.getY();
			if (stoneX >= xMin && stoneX <= xMax && stoneY >= yMin && stoneY <= yMax) {
				log.debug("stone " + stone.getID() + " fits the bill");
				int retVal = game.removeStone(stone.getID()); 
				log.debug("game.removeStone(stone.getID()) => " + retVal);
				if (retVal == 1) {
					sF.placed[stone.placedAt()] = false;
					stone.hide();
					mill = false;
				}
			}

		}
		repaint();
	}

	public synchronized void moveStone(int stone, int goal)
	{
		if (colour == Player.BLACK) {
			white[stone].setX(sF.placement[goal][0]- (radius/2));
			white[stone].setY(sF.placement[goal][1]- (radius/2));
			white[stone].placedAt(goal);
			white[stone].used(true);
		} else {
			black[stone-9].setX(sF.placement[goal][0] - (radius/2));
			black[stone-9].setY(sF.placement[goal][1] - (radius/2));
			black[stone-9].placedAt(goal);
			black[stone-9].used(true);
		}
		sF.placed[goal] = true;

		repaint();
	}

	public synchronized void removeStone(int stone)	{
		log.entry(stone);
		log.debug("white.length: " + white.length);
		log.debug("black.length: " + black.length);
		if (colour == Player.WHITE) {
			log.debug("removing a white stone");
			sF.placed[white[stone].placedAt()] = false;
			white[stone].hide();
		} else {
			log.debug("removing a black stone");
			sF.placed[black[stone-9].placedAt()] = false;
			black[stone-9].hide();
		}
		repaint();
	}

	public synchronized boolean newGame(boolean win){
		log.entry();
		log.debug("did I win? " + win);
		int n = 0;
		Object[] options = {"Yes, please",
				"No",};
		
		if(colour == Player.WHITE) {
			winner = Player.BLACK;
		} else {
			winner = Player.WHITE;
		}
		
		if(win = false) {
			

		} else { 
			winner = colour;
			
			log.debug("asking player about new game");
			n = JOptionPane.showOptionDialog(mFra,
					"Do you to start a new Game?",
					"New Game Dialog",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[1]);
		}
		
		repaint();
		
		if(n == 0) {
			log.debug("player says 'yes'");
			return true;
		} else {
			log.debug("player says 'no'");
			return false;
		}
	}

	public synchronized void noMore() {
		log.entry();
		JOptionPane.showMessageDialog(mFra,
				"User denied. Disconnecting...",
				"No more!",
				JOptionPane.PLAIN_MESSAGE);
		System.exit(0);
	}

	public void mouseClicked(MouseEvent e) {
		log.entry();
		if (mill) {
			removeS(e);
		} else {
			moveS(e);
			this.paint(mFra.getGraphics());
			if( globalRetVal == 1 && !mill) { globalRetVal = 0; }
		}
		repaint();
		log.exit();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void setColour(Player colour) {
		log.entry();
		this.colour = colour;
		log.debug("new colour is " + this.colour);
		repaint();
		log.exit();
	}

	@Override
	public void youWon(boolean loserGone) {
		this.winner = colour;
		this.sessionOver = loserGone;
		repaint();
		
	}


}
