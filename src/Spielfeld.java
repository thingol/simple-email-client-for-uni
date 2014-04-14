import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Spielfeld extends Panel {
	private static final long serialVersionUID = 1L;

	public void paint(Graphics g)
	{
		int height = getSize().height;
		int width = getSize().width;
		int middleX = width/2;
		int middleY = height/2;
		int addX = middleX-middleX/4;
		int addY = middleY-middleY/4;
		int facHW = width*height/(1366*766);
		int facW = width/1366;
		int facH = height/766;
		int r = width*height/30000;
		
		{//Spielfeld
		//Outer
		g.drawRect(middleX/4, middleY/4, (addX)*2, (addY)*2);
		//Middle
		g.drawRect(middleX/4+addX/3, middleY/4+addY/3, (middleX-(middleX/4+addX/3))*2, (middleY-(middleY/4+addY/3))*2);
		//Inner
		g.drawRect(middleX/4+2*addX/3, middleY/4+2*addY/3, (middleX-(middleX/4+2*addX/3))*2, (middleY-(middleY/4+2*addY/3))*2);
		
		//g.drawLine(middleX, middleY, middleX, middleY);
		//Y Lines
		g.drawLine(middleX, middleY/4, middleX, middleY/4+2*addY/3);
		g.drawLine(middleX, middleY+middleY/4, middleX, middleY+(middleY/4+2*addY/3));
		//X Lines
		g.drawLine(middleX/4, middleY, middleX/4+2*addX/3, middleY);
		g.drawLine(middleX+middleX/4, middleY, middleX+middleX/4+2*addX/3, middleY);
		
		//Now, this is getting fat
		g.drawRect(middleX/4 +1, middleY/4+1, (addX)*2, (addY)*2);
		g.drawRect(middleX/4 +2, middleY/4+2, (addX)*2, (addY)*2);
		g.drawRect(middleX/4+addX/3 +1, middleY/4+addY/3 +1, (middleX-(middleX/4+addX/3))*2, (middleY-(middleY/4+addY/3))*2);
		g.drawRect(middleX/4+addX/3 +2, middleY/4+addY/3 +2, (middleX-(middleX/4+addX/3))*2, (middleY-(middleY/4+addY/3))*2);
		g.drawRect(middleX/4+2*addX/3 +1, middleY/4+2*addY/3 +1, (middleX-(middleX/4+2*addX/3))*2, (middleY-(middleY/4+2*addY/3))*2);
		g.drawRect(middleX/4+2*addX/3 +2, middleY/4+2*addY/3 +2, (middleX-(middleX/4+2*addX/3))*2, (middleY-(middleY/4+2*addY/3))*2);
		g.drawLine(middleX+1, middleY/4, middleX+1, middleY/4+2*addY/3);
		g.drawLine(middleX+2, middleY/4, middleX+2, middleY/4+2*addY/3);
		g.drawLine(middleX+1, middleY+middleY/4, middleX+1, middleY+(middleY/4+2*addY/3));
		g.drawLine(middleX+2, middleY+middleY/4, middleX+2, middleY+(middleY/4+2*addY/3));
		g.drawLine(middleX/4, middleY+1, middleX/4+2*addX/3, middleY+1);
		g.drawLine(middleX/4, middleY+2, middleX/4+2*addX/3, middleY+2);
		g.drawLine(middleX+middleX/4, middleY+1, middleX+middleX/4+2*addX/3, middleY+1);
		g.drawLine(middleX+middleX/4, middleY+2, middleX+middleX/4+2*addX/3, middleY+2);
		}
		
		for(int i = 0; i <= 9; i++)
		{
			g.fillOval(60+i*r, height-60, r, r);
			g.drawOval(width-60-i*r, height-60, r, r);
		}
	}
	
	public static void main(String args[])
	{
		List myList = new List(3, false);
		myList.add("Java"); myList.add("Coffee");
		myList.add("Expresso"); myList.add("Cappuccino");
		Frame myFrame = new Frame();
		myFrame.add(new Spielfeld());
		myFrame.setSize(1024,720);
		myFrame.setVisible(true);
		myFrame.add("South", myList);
		
		myFrame.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent we){
			    System.exit(0);
			  }
			});
		
	}
}