package de.uni_jena.min.in0043.nine_mens_morris;

import java.awt.Graphics;
import java.awt.Panel;



public class Spielfeld extends Panel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void paint(Graphics g)
	{
		int height = getSize().height;
		int width = getSize().width;
		int middleX = width/2;
		int middleY = height/2;
		int addX = middleX-middleX/4;
		int addY = middleY-middleY/4;
		
		g.drawRect(middleX/4, middleY/4, (addX)*2, (addY)*2);
		
		g.drawRect(middleX/4+addX/3, middleY/4+addY/3, (middleX-(middleX/4+addX/3))*2, (middleY-(middleY/4+addY/3))*2);
		
		g.drawRect(middleX/4+2*addX/3, middleY/4+2*addY/3, (middleX-(middleX/4+2*addX/3))*2, (middleY-(middleY/4+2*addY/3))*2);
		
		g.drawLine(middleX, middleY, middleX, middleY);
		//Y Lines
		g.drawLine(middleX, middleY/4, middleX, middleY/4+2*addY/3);
		g.drawLine(middleX, middleY+middleY/4, middleX, middleY+(middleY/4+2*addY/3));
		//X Lines
		g.drawLine(middleX/4, middleY, middleX/4+2*addX/3, middleY);
		g.drawLine(middleX+middleX/4, middleY, middleX+middleX/4+2*addX/3, middleY);
		
		g.drawLine(100, 100, 100, 100);
		
		/*//Now, this is getting fat
		g.drawRect(middleX/4+1, middleY/4+1, (middleX-middleX/4)*2, (middleY-middleY/4)*2);
		g.drawRect(middleX/4+2, middleY/4+2, (middleX-middleX/4)*2, (middleY-middleY/4)*2);

		g.drawRect(middleX-100+1, middleY-100+1, middleX*2/3, middleY*2/3);
		g.drawRect(middleX-100+2, middleY-100+2, middleX*2/3, middleY*2/3);

		g.drawRect(middleX-50+1, middleY-50+1, middleX/3, middleY/3);
		g.drawRect(middleX-50+2, middleY-50+2, middleX/3, middleY/3);

		g.drawLine(middleX+2, middleY-150, middleX+2, middleY-50);
		g.drawLine(middleX+2, middleY+150, middleX+2, middleY+50);
		g.drawLine(middleX+3, middleY-150, middleX+3, middleY-50);
		g.drawLine(middleX+3, middleY+150, middleX+3, middleY+50);

		g.drawLine(middleX-150, middleY+2, middleX-50, middleY+2);
		g.drawLine(middleX+150, middleY+2, middleX+50, middleY+2);
		g.drawLine(middleX-150, middleY+3, middleX-50, middleY+3);
		g.drawLine(middleX+150, middleY+3, middleX+50, middleY+3);//*/
		
	}
}