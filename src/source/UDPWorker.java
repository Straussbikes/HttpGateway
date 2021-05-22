package source;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPWorker extends Thread{
    private DatagramSocket ds;
    private boolean running = false;
    private byte[] buf = new byte[1024*1024];

    public UDPWorker(DatagramSocket socket){
        this.ds = socket;
    }

    public void run(){
        running = true;

        while(running){
            // Packet
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            // Packet a transmitir -> INFO DO FILE A TRANSMITIR
            try {
                ds.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Info do Packet
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);

            // RECEBEU FILE
            String received = new String(packet.getData(), 0, packet.getLength());

            if(received.equals("end")){
                running = false;
                continue;
            }

            // Mandar Packet
            try {
                ds.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ds.close();
    }
}
