package source;

import java.io.IOException;
import java.net.*;

// TODO falta definir conexão UDP com o HTTPGW

public class FastFileSrv extends Thread {
    // Target IP
    private InetAddress ipAdress;

    // Target port
    private DatagramSocket socket;

    // Server status
    private boolean running = false;

    // Buffer
    private byte[] buf = new byte[1024*1024];

    public FastFileSrv(String ip, String port) throws SocketException, UnknownHostException {
        this.ipAdress = InetAddress.getByName(ip);
        this.socket = new DatagramSocket(Integer.parseInt(port), ipAdress);
    }

    @Override
    public void run() {
        // Tem socket ligada ao server
        this.running = true;

        while(running) {
            // Packet
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            // Fica a escuta de pedidos
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Processa informação do pedido
            // Pega no file
            // Dá split do file

            // Envia os respetivos chunks
                // Pseudo-codigo
            while (lista de chunks != null){
                if (Lista de chunks numero == chunk.getSequenceNum()){
                    try {
                        socket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void close(){
        socket.close();
    }
}
