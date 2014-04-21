import java.awt.*;
import java.awt.event.*;


public class Head extends Panel implements MouseListener {

	private static final long serialVersionUID = -5704850734397028920L;
	public Spielfeld sF;
	public Stone[] Black;
	public Stone[] White;
	public Frame mFra;

	public Head() {
		mFra = new Frame("Nine Men's Morris - Retro Style");
		mFra.setSize(1024, 720);
		sF = new Spielfeld();
		Black = new Stone[9];
		White = new Stone[9];

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

	public void paint(Graphics g) {
		int height = mFra.getSize().height;
		int width = mFra.getSize().width;
		int middleX = width / 2;
		int middleY = height / 2;
		int addX = middleX - middleX / 4;
		int addY = middleY - middleY / 4;
		int r = width * height / 30000;
		
		/*for (int i = 0; i < 9; i++) {
		Black[i].posX = 40 + i * r;
		Black[i].posY = mFra.getSize().height - 80;
		White[i].posX = mFra.getSize().width - 80 - i * r;
		White[i].posY = mFra.getSize().height - 80;
		}*/

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
			if(Black[i] != null && White[i] != null)
			{
				g.fillOval(Black[i].posX, Black[i].posY, r, r);
				g.drawOval(White[i].posX, White[i].posY, r, r);
			}
			else;
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

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
//				this.setBackground(new Color((int) (Math.random()*255),(int) (Math.random()*255),(int) (Math.random()*255)));
//				repaint();
				boolean yep = true;
				int height = mFra.getSize().height;
				int width = mFra.getSize().width;
				int r = width * height / 30000 /2;
				int x = e.getX();
				int y = e.getY();
				
				for(int i = 0; i < 9; i++)
				{
					//This sets the stone to a corner
					if(Black[i].inPlacement == true && Black[i].placed == false && Black[i].now == true)
					{  //Did I choose a piece?            Is it already placed?     Else the Stone moves instantly
						for(int l = 0; l < 24; l++)
						{
							for(int j = 0; j < 2*r; j++)
							{
								for(int k = 0; k < 2*r; k++)
								{
									if(e.getX() == sF.placement[l][0] - r + j && e.getY() == sF.placement[l][1] -r + k && sF.placed[l] == false)
									{
										Black[i].inPlacement = false;
										Black[i].posX = sF.placement[l][0] - r;
										Black[i].posY = sF.placement[l][1] - r;
										sF.placed[l] = true;
										Black[i].placed = true;
										yep = true;
										break;
									}
								}
							}
						}
					}
					//This just wants to know if one strone has been selected yet
					for(int j = 0; j < 9; j++)
					{
						if(Black[j].inPlacement == true)
							yep = false;
					}
					
					if(Black[i].inPlacement == false && yep == true && Black[i].placed == false) {
						// >> I didn't choose a piece yet, doesn't work tho...
					for(int j = 0; j < 2*r; j++)
					{
						for(int k = 0; k < 2*r; k++)
						{
							if(x == Black[i].posX + j)
							{
								if(y == Black[i].posY + k)
								{
									Black[i].inPlacement = true;
								}
							}
						}
					}//forJ
					}//if
					
				}//forI
				
				//Now the same stuff for the Whites
				for(int i = 0; i < 9; i++)
				{
					//This sets the stone to a corner
					if(White[i].inPlacement == true && White[i].placed == false && White[i].now == true)
					{  //Did I choose a piece?            Is it already placed?     Else the Stone moves instantly
						for(int l = 0; l < 24; l++)
						{
							for(int j = 0; j < 2*r; j++)
							{
								for(int k = 0; k < 2*r; k++)
								{
									if(e.getX() == sF.placement[l][0] - r + j && e.getY() == sF.placement[l][1] -r + k && sF.placed[l] == false)
									{
										White[i].inPlacement = false;
										White[i].posX = sF.placement[l][0] - r;
										White[i].posY = sF.placement[l][1] - r;
										sF.placed[l] = true;
										White[i].placed = true;
										yep = true;
										break;
									}
								}
							}
						}
					}
					//This just wants to know if one strone has been selected yet
					for(int j = 0; j < 9; j++)
					{
						if(White[j].inPlacement == true)
							yep = false;
					}
					
					if(White[i].inPlacement == false && yep == true && White[i].placed == false) {
						// >> I didn't choose a piece yet, doesn't work tho...
					for(int j = 0; j < 2*r; j++)
					{
						for(int k = 0; k < 2*r; k++)
						{
							if(x == White[i].posX + j)
							{
								if(y == White[i].posY + k)
								{
									White[i].inPlacement = true;
								}
							}
						}
					}//forJ
					}//if
					
				}//forI
				
				for(int i = 0; i < 9; i++)
				{System.out.println("Stone Black " + i + " inPlacement: " + Black[i].inPlacement);
				 System.out.println("Stone White " + i + " inPlacement: " + White[i].inPlacement);
				}
				System.out.println(yep);
	}

	public void mouseReleased(MouseEvent e) {
		repaint();
		for(int i = 0; i < 9; i++)
		{if(Black[i].inPlacement == true && Black[i].placed == false) Black[i].now = true;
		if(White[i].inPlacement == true && White[i].placed == false) White[i].now = true;
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}