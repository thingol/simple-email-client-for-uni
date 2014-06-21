package de.uni_jena.min.in0043.nine_mens_morris.gui;

import java.awt.*;
import java.awt.event.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.core.Logic;
import de.uni_jena.min.in0043.nine_mens_morris.core.Phase;
import de.uni_jena.min.in0043.nine_mens_morris.core.Player;

public class Head extends Panel implements MouseListener {

	private static final long serialVersionUID = -5704850734397028920L;
	private static Logger log = LogManager.getLogger();
	private Spielfeld sF;
	private Stone[] Black;
	private Stone[] White;
	private Frame mFra;
	private Logic nmm;
	private boolean mill;

	public Head() {
		mFra = new Frame("Nine Men's Morris - Retro Style");
		mFra.setSize(1024, 720);
		sF = new Spielfeld();
		Black = new Stone[9];
		White = new Stone[9];
		mill = false;
		nmm = new Logic();

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

	public Head(Logic nnn) {
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
				System.exit(0);
			}
		});
	}

	public void moveS(MouseEvent e) {
		Stone[] st = new Stone[9];
		boolean yep = true;
		int r = mFra.getSize().width * mFra.getSize().height / 30000 / 2;
		int x = e.getX();
		int y = e.getY();
		// Which Stone do we want to move?
		if (nmm.getActivePlayer() == Player.WHITE) {
			st = White;
		} else
			st = Black;

		if (nmm.getPhase() == Phase.PLACING_STONES) {
			for (int i = 0; i < 9; i++) {

				// This sets the stone to a corner
				if (st[i].inPlacement == true && st[i].placed == false) { // Did
																			// I
																			// choose
																			// a
																			// piece?
																			// Is
																			// it
																			// already
																			// placed?
					for (int l = 0; l < 24; l++) {
						for (int j = 0; j < 2 * r; j++) {
							for (int k = 0; k < 2 * r; k++) {
								if (e.getX() == sF.placement[l][0] - r + j
										&& e.getY() == sF.placement[l][1] - r
												+ k && sF.placed[l] == false) {
									int s;
									if (nmm.getActivePlayer() == Player.WHITE)
										s = i;
									else
										s = i + 9;
									int z = nmm.moveStone(s, l);
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
					}
				}
				// This just wants to know if one stone has been selected yet
				for (int j = 0; j < 9; j++) {
					if (st[j].inPlacement == true)
						yep = false;
				}

				// Selects the Stone
				if (st[i].inPlacement == false && yep == true
						&& st[i].placed == false) {
					// >> I didn't choose a piece yet, doesn't work tho...
					for (int j = 0; j < 2 * r; j++) {
						for (int k = 0; k < 2 * r; k++) {
							if (x == st[i].posX + j) {
								if (y == st[i].posY + k
										&& st[i].inPlacement == false) { // else
																			// this
																			// function
																			// is
																			// not
																			// deterministic
									st[i].inPlacement = true;
									st[i].set(5);
								}
							}
						}
					}// forJ
				}// if

			}// forI
		} else {

			for (int i = 0; i < 9; i++) {
				if (st[i].inPlacement == true) { // Did I choose a piece? Is it
													// already placed?
					for (int l = 0; l < 24; l++) {
						for (int j = 0; j < 2 * r; j++) {
							for (int k = 0; k < 2 * r; k++) {
								if (e.getX() == sF.placement[l][0] - r + j
										&& e.getY() == sF.placement[l][1] - r
												+ k) {
									int s;
									if (nmm.getActivePlayer() == Player.WHITE)
										s = i;
									else
										s = i + 9;
									int z = nmm.moveStone(s, l);
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
									} else {
										st[i].inPlacement = false;
										st[i].set(-5);
										log.error("ERROR!");
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
										&& st[i].inPlacement == false) { // else
																			// this
																			// function
																			// is
																			// not
																			// deterministic
									st[i].inPlacement = true;
									st[i].set(5);
								}
							}
						}
					}// forJ
				}// if

			}// forI
		}
	}

	public void delete(MouseEvent e) {
		Stone[] st = new Stone[9];
		if (nmm.getActivePlayer() == Player.BLACK) {
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
						if (nmm.getActivePlayer() == Player.BLACK) {
							m2 = nmm.removeStone(i);
						} else {
							m2 = nmm.removeStone(i + 9);
						}
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
		repaint();
	}

	public void mouseClicked(MouseEvent e) {
		
		log.trace("did we get a mill:" + mill);
		
		if(mill)
		{	delete(e);}
		else{
			moveS(e);
		}
	}

	public void mousePressed(MouseEvent e) {
		// this.setBackground(new Color((int) (Math.random()*255),(int)
		// (Math.random()*255),(int) (Math.random()*255)));
		// repaint();

	}

	public void mouseReleased(MouseEvent e) {
		repaint();
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}
