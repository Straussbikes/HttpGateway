package source;

import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPCon {
    private DatagramSocket datagramSocket = new DatagramSocket(80);

    public UDPCon() throws SocketException {
    }

    public DatagramSocket getDatagramSocket() {
        return this.datagramSocket;
    }
}
