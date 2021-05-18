package source;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

// TODO falta definir conexão UDP com o HTTPGW

public class FastFileSrv implements Runnable {
    // Target IP
    private InetAddress ipAdress;

    // Target port
    private DatagramSocket socket;

    // Buffer
    private byte[] buf;

    public FastFileSrv(String ip) throws SocketException {
        this.ipAdress = ;
        this.socket = new DatagramSocket();
    }

    @Override
    public void run() {

        DatagramPacket packet = new DatagramPacket(, address, 4445);

        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        packet = new DatagramPacket();

        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String received;

        return received;
    }

    public void close(){
        socket.close();
    }
}
