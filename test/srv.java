import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TestServer {
    public static void main(String args[]) {
        System.out.println("starting server on port 6112\n");
        try {
            @SuppressWarnings("resource")
			ServerSocket listener = new ServerSocket(6112);
            byte[] msgArray = new byte[3];
            boolean connected = false;
            while(true) {
                Socket client = listener.accept();
                connected = true;
                System.out.println("==========================================\n"
                        + "connection from " + client.getInetAddress().getHostAddress());
                System.out.println("the time is: " + new Date());
                
                DataInputStream din =
                		new DataInputStream(new BufferedInputStream(client.getInputStream()));
                OutputStream out = client.getOutputStream();
                
                while(connected)  {
                	try {
						din.readFully(msgArray);

						System.out.println("got operator: " + msgArray[0]);
						System.out.println("got first operand: " + msgArray[1]);
						System.out.println("got second operand: " + msgArray[2]);
						
						System.out.println("the time is: " + new Date());
						
						System.out.println("responding");
						out.write(82);
						out.write(69);
						out.write(83);
						out.write(33);
						out.write(10);
					} catch (EOFException e) {
						// client disconnected
						connected = false;
					}
                }
                
                System.out.println("client disconnected");
                System.out.println("the time is: " + new Date());
                System.out.println("==========================================");
                //listener.close();
	        }
        } catch (Exception e) {
        	// should never happen, but hey...
        	System.err.println("caught exception: " + e);
        }
    }
}
