package source;


import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class HTTPGw extends Thread {
    InetAddress ip;
    DatagramSocket dataSocket;
    Socket serverSocket;
    boolean running;
    TCPCon tcpc;
    int servers_conectados;

    public HTTPGw(InetAddress ip) throws IOException {
        this.ip = ip;
        this.dataSocket = new DatagramSocket(Constantes.UDPPort);
        this.running = true;
        this.servers_conectados = 0;
    }

    public InetAddress getIp() {
        return this.ip;
    }

    public DatagramSocket getDatagramSocket() {
        return this.dataSocket;
    }

    public Socket getServerSocket() {
        return this.serverSocket;
    }

    public int getServers_conectados() {
        return this.servers_conectados;
    }

    public boolean isRunning() {
        return this.running;
    }

    //SETS
    public void setServerSocket(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setTCP(TCPCon tcp){
        this.tcpc=tcp;
    }

    public void setDataSocket(DatagramSocket ds){
        this.dataSocket = ds;
    }

    // Incrementa Servidores Conectados
    public void incServers() {
        ++this.servers_conectados;
    }

    // Desliga servidor
    public void ServerShutdown() {
        this.running = false;
    }

    public void run() {
        try {
            Tcplistener tcp = new Tcplistener(this.serverSocket, tcpc);
            tcp.start();

            UDPWorker udp = new UDPWorker(this.dataSocket);
            udp.start();

            System.out.println("dar upload a 2: "+ tcp.getFicheiro());

        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }
    public String getf(){
        return tcpc.getFicheiro();
    }
}
