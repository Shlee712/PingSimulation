import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;

class PingClient extends UDPPinger {
	public static void main(String args[]) {
		PingClient client = new PingClient();
		client.run();
	}
	public void run( ) {
		
		int portNum = 5525;
		int myPortNum = 5540;
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName("localhost");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		try {
			socket = new DatagramSocket(myPortNum);
			socket.setSoTimeout(1000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		String payload1 = "PING ";
		long packetTime;
		PingMessage msg;
		int[] pingTimeArr = new int[10];
		int receivePing = 0;
		PingMessage replyPing;
		Date time;
		for(int i = 0; i < 10; i++) {
			time = new Date();
			packetTime = time.getTime();
			msg = new PingMessage(addr,portNum,payload1 + i + " " + packetTime);
			sendPing(msg);
			if(receivePing < 10) {
				try {
					replyPing = receivePing();
					if(replyPing != null) {
						time = new Date();
						pingTimeArr[replyPing.getNumber()] = (int) (time.getTime() - replyPing.getTime());
						System.out.println("Received packet # "+replyPing.getNumber()+" from "+addr + " " +portNum+ " "+time.getTime());
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
				}
				receivePing++;
			}
		}
		
		try {
			socket.setSoTimeout(5000);
		}catch (SocketException e) {
			e.printStackTrace();
		}
		
		try {
			replyPing = receivePing();
			if(replyPing != null) {
				time = new Date();
				pingTimeArr[replyPing.getNumber()] = (int) (time.getTime() - replyPing.getTime());
				System.out.println("Received packet from "+addr + " " +portNum+ " "+time.getTime());
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		}
		
		int min = 1000;
		int max = 0;
		int avg = 0;
		boolean lost = false;
		for(int i = 0; i < 10;i++) {
			max = Math.max(max,pingTimeArr[i]);
			if(pingTimeArr[i] != 0) {
				min = Math.min(min,pingTimeArr[i]);
			}
			if(pingTimeArr[i] == 0) {
				System.out.println("PING " + i + ": false RTT: 1000");
				lost = true;
				avg += 1000;
			}
			else {
				System.out.println("PING " + i + ": true RTT: "+pingTimeArr[i]);
				avg += pingTimeArr[i];
			}
		}
		avg = avg/10;
		if(lost = true) {
			max = 1000;
		}
		System.out.println("Min = "+min+" Max = "+max+" Avg = "+avg);
		
		
	}
}
