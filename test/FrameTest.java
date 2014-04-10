import java.awt.*;

public class FrameTest {
    static Frame testFrame;

    public static void main(String args[]) {
        testFrame = new Frame("A simple Frame");
        testFrame.setLayout(new FlowLayout());
        testFrame.add(new Button("Java"));
        testFrame.pack();
        testFrame.setVisible(true);
    }

    public void paint(Graphics g) {
        int dia = Math.min(getSize().width,getSize().height);
        g.fillOval((this.getSize().width-dia)/2,(this.getSize().height-dia)/2, dia, dia);
    }

    /*
       // Drawing squares
       drawRect(int x, int y, int width, int height)
       fillRect(int x, int y, int width, int height)
       drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
       fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
       draw3DRect(int x, int y, int width, int height, boolean raised)
       fill3DRect(int x, int y, int width, int height, boolean raised)

       // Ellipsis
       drawOval(int x, int y, int width, int height)
       fillOval(int x, int y, int width, int height)

       // Arcs
       drawArc(int x, int y, int width, int height, int startAngle, int arcAngle)
       fillArc(int x, int y, int width, int height, int startAngle, int arcAngle)

       // Polygons
       drawPolygon(int[] xPoints, int[] yPoints, int n)
       fillPolygon(int[] xPoints, int[] yPoints, int n)

       // Polyline
       drawPolyline(int[] xPoints, int[] yPoints, int n)

       // Text as a "drawing"
       drawString(String str, int x, int y)
       
       // Usage
       public void paint(Graphics g) {
           g.drawRect(10, 10, 80, 120);
           g.fillRect(110, 10, 80, 120);
           g.drawRoundRect(210, 10, 80, 120, 40, 40);
           g.fill3DRect(310, 10, 80, 120, false);
       }

       // Colour
       Color myColor = new Color(0, 0, 255);
       myColor = Color.BLUE;
       g.setColor(Color.RED); Color c = g.getColor();


       

   */

}
