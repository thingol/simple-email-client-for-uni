import java.awt.*;
import java.awt.event.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Spielfeld extends Panel implements MouseListener {
	private static final long serialVersionUID = 1L;

	public void paint(Graphics g)
	{
		int height = getSize().height;
		int width = getSize().width;
		int middleX = width/2;
		int middleY = height/2;
		int addX = middleX-middleX/4;
		int addY = middleY-middleY/4;
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
		{ //Adds the stones, still not final tho... looks kinda bad.
			g.fillOval(60+i*r, height-60, r, r);
			g.drawOval(width-60-i*r, height-60, r, r);
		}
	}
	
	public static void main(String args[])
	{
		new Spielfeld().BuildUp();
	}
	
	public void BuildUp()
	{
		Frame mFra = new Frame(" Nine Men's Morris - Retro Style");
		mFra.setSize(1024,720);
		mFra.setForeground(Color.yellow);
		mFra.setBackground(Color.blue);
		mFra.add(this);
		
		mFra.add(this);
		this.addMouseListener(this);
		mFra.setVisible(true);
		
		mFra.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent we){
			    System.exit(0);
			  }
			});
	}
	
	public void mouseClicked(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			System.out.println("Pressed at x:"+e.getX()+" y:"+e.getY());
		}
		public void mouseReleased(MouseEvent e) {
			System.out.println("Released at x:"+e.getX()+" y:"+e.getY());
		}
		public void mouseEntered(MouseEvent e)
		{}
		public void mouseExited(MouseEvent e) {}
}