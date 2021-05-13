package source;


import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

public class HTTPGw extends Thread {
    String ip;
    DatagramSocket socket;
    Socket serverSocket;
    boolean running;
    TCPCon tcpc;
    int servers_conectados;

    public HTTPGw(String ip) throws IOException {
        this.ip = ip;
        this.socket = new DatagramSocket(80);
        this.running = true;
        this.servers_conectados = 0;
    }

    public String getIp() {
        return this.ip;
    }

    public DatagramSocket getSocket() {
        return this.socket;
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

    public void incServers() {
        ++this.servers_conectados;
    }
    public void setTCP(TCPCon tcp){
        this.tcpc=tcp;
    }

    public void setServerSocket(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void ServerShutdown() {
        this.running = false;
    }

    public void run() {
        try {
            Tcplistener tcp = new Tcplistener(this.serverSocket,tcpc);
            tcp.start();
            System.out.println("dar upload a 2: "+ tcp.getFicheiro());
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }
    public String getf(){
        return tcpc.getFicheiro();
    }
}
