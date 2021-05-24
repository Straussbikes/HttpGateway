package source;


import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class HTTPGw extends Thread {
    InetAddress ip;
    DatagramSocket dataSocket;
    Socket serverSocket;
    boolean running;
    TCPCon tcpc;
    int servers_conectados;
    public Queue<FastFileSrv> poolServer;
    public byte[] sending = new byte[65506];

    public HTTPGw() {
        this.running = true;
        this.servers_conectados = 0;
        this.poolServer = new LinkedList<>();
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }


    public HTTPGw(InetAddress ip, DatagramSocket data) throws IOException {
        this.ip = ip;
        this.dataSocket = data;
        this.running = true;
        this.servers_conectados = 0;
        this.poolServer = new LinkedList<>();
    }

    public Queue<FastFileSrv> getPoolServer() {
        return poolServer;
    }

    public void setPoolServer(Queue<FastFileSrv> poolServer) {
        this.poolServer = poolServer;
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
            //thread que trabalha com o client
            Tcplistener tcp = new Tcplistener(this.serverSocket, tcpc);
            tcp.start();
            while(tcp.getFicheiro()==null){
                Thread.sleep(1);
            }
            while(poolServer==null){
                Thread.sleep(1);
            }
            System.out.println("dar upload a 2: "+ tcp.getFicheiro());
            System.out.println(poolServer.size());

              // FastFileSrv ffs = poolServer.remove();
               incServers();
               //thread que comunica com ffs


            UDPWorker udp = new UDPWorker(tcp.getFicheiro(),dataSocket);
            udp.start();

            //espera pela receção e agrupamento
            while(udp.getSending()==null || udp.getWait()){
                System.out.println("sleep time");
               Thread.sleep(1);
            }
            //envia para a thread que comunica com o client
            tcp.setSending(udp.getSending());
            System.out.println("Sendint length "+tcp.getSending().length);
            PrintStream os = new PrintStream(tcpc.getOut());
            tcp.sendFile(os,tcp.getSending());


           this.serverSocket.close();
           tcp.setFicheiro(null);

        } catch (Exception var2) {
            var2.printStackTrace();
            ServerShutdown();
        }

    }
    public String getf(){
        return tcpc.getFicheiro();
    }
}
