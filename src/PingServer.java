import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Random;

public class PingServer {
	public static void main(String args[]) {
		int portNum = 5525;
		int average_delay = 100;
		double loss_rate = 0.3;
		
		Random random = new Random (new Date().getTime());
		byte[] buff = new byte[512];
		DatagramPacket inpacket = new DatagramPacket(buff,512);
		DatagramPacket outpacket = null;
		
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(portNum);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		System.out.println("Ping Server running...");
		while(true) {
			try {
				System.out.println("Waiting for packet...");
				socket.receive(inpacket);
				String s = new String(buff);
				System.out.println("Received from: " +inpacket.getAddress() + " " + inpacket.getPort() + " "+ s);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep( (int) (random.nextDouble() * 2 * average_delay) ) ;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(random.nextDouble() > loss_rate) {
				outpacket = new DatagramPacket(inpacket.getData(),512,inpacket.getAddress(),inpacket.getPort());
				try {
					socket.send(outpacket);
					System.out.println("Reply sent");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				System.out.println("Simulating packet loss, not sent");
			}
		}
		
	}
}
