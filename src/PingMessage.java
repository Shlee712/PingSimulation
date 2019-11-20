import java.net.InetAddress;

public class PingMessage {
	InetAddress addr;
	int port;
	String payload;
	public PingMessage(InetAddress addr, int port, String payload) {
		this.addr = addr;
		this.port = port;
		this.payload = payload;
	}
	public InetAddress getIP() {
		return addr;
	}
	public int getPort() {
		return port;
	}
	public String getPayload() {
		return payload;
	}
	public long getTime() {
		String s = payload.substring(7);
		return Long.parseLong(s);
	}
	public int getNumber() {
		char a = payload.charAt(5);
		return Character.getNumericValue(a);
	}
}
