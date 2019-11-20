import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

public class UDPPinger {
	DatagramSocket socket;
	
	public void sendPing(PingMessage ping) {
		byte[] payloadByte = ping.getPayload().getBytes();
		byte[] payload = new byte[512];
		System.arraycopy(payloadByte,0,payload,0,payloadByte.length);
		DatagramPacket packet = new DatagramPacket(payload,512,ping.getIP(),ping.getPort());
		try {
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PingMessage receivePing() throws SocketTimeoutException {
		byte[] receive = new byte[512];
		DatagramPacket packet = new DatagramPacket(receive,512);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			System.out.println(e);
			// TODO Auto-generated catch block
			return null;
		}
		String s = data(receive).toString();
		return new PingMessage(packet.getAddress(),packet.getPort(),s);
	}
    private static StringBuilder data(byte[] a) 
    { 
        if (a == null) 
            return null; 
        StringBuilder ret = new StringBuilder(); 
        int i = 0; 
        while (a[i] != 0) 
        { 
            ret.append((char) a[i]); 
            i++; 
        } 
        return ret; 
    } 
}
