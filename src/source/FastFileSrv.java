package source;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

// TODO falta definir conexão UDP com o HTTPGW

public class FastFileSrv implements Runnable {
    // Target IP
    String target_ip;

    // Target port
    DatagramSocket port;

    public FastFileSrv(String ip, String port) throws SocketException {
        this.target_ip = ip;
        this.port = new DatagramSocket(Integer.parseInt(port));
    }

    @Override
    public void run() {

    }
}
