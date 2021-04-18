package source;

import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPCon {
    private DatagramSocket datagramSocket;

    public UDPCon() throws SocketException {
        this.datagramSocket = new DatagramSocket(Constantes.UDPPort);
    }

    public DatagramSocket getDatagramSocket() {
        return this.datagramSocket;
    }
}
