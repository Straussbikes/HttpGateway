package source;

import com.sun.net.httpserver.HttpServer;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpGwMain {
    public static void main(String[] args) throws IOException {
        HTTPGw main_server = new HTTPGw();
        if (args.length == 0) {
            throw new IllegalArgumentException("insuficient arguments");
        }

        // Secção HTTPGW
        else if (args[0].equals("HTTPGw")) {

            // Ip da maquina
            InetAddress ip = InetAddress.getLocalHost();

            // Socket TCP
            ServerSocket server = new ServerSocket(Constantes.TCPPort);

            // Socket UDP
            //   DatagramSocket dsocket = new DatagramSocket(5000);

            // Main Server
            main_server.setIp(ip);

            // Rodar server
            while (true) {


                try {
                    System.out.println("Waiting for connection on port " + Constantes.TCPPort);


                    //TCP
                    Socket sockets = server.accept();
                    TCPCon tcp = new TCPCon(sockets);
                    main_server.setServerSocket(sockets);
                    main_server.setTCP(tcp);

                    //UDP
                    //   main_server.setDataSocket(dsocket);

                    // Start do server
                    main_server.run();

                    // Print de aviso
                    System.out.println("Ativo em " + main_server.getIp() + " porta " + Constantes.UDPPort);


                } catch (IOException e) {
                    System.err.println("Cannot accept connection");
                }

            }


        } else System.out.println("Parametros Invalidos!");
    }
}

