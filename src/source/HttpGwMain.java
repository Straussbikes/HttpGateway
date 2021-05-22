package source;

import com.sun.net.httpserver.HttpServer;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;

public class HttpGwMain {

    public static void main (String[] args) throws IOException  {

        if (args.length == 0) {
           throw new IllegalArgumentException("insuficient arguments");
        }

        // Secção HTTPGW
        else if(args[0].equals("HTTPGw")) {
            // Ip da maquina
            InetAddress ip = InetAddress.getLocalHost();

            // Socket TCP
            ServerSocket server = new ServerSocket(Constantes.TCPPort);

            // Socket UDP
            DatagramSocket dServer = new DatagramSocket(Constantes.UDPPort);

            // Main Server
            HTTPGw main_server = new HTTPGw(ip);

            // Rodar server
            while(true) {
                try {
                    System.out.println("Waiting for connection on port " + Constantes.TCPPort);

                    //TCP
                    Socket sockets = server.accept();
                    TCPCon tcp = new TCPCon(sockets);
                    main_server.setServerSocket(sockets);
                    main_server.setTCP(tcp);

                    //UDP
                    main_server.setDataSocket(dServer);

                    // Start do server
                    main_server.start();

                    // Print de aviso
                    System.out.println("Ativo em " + main_server.getIp() + " porta " + Constantes.UDPPort);
                }
                catch(IOException e) {
                    System.err.println("Cannot accept connection");
                }
            }
        }

        // Secção FastFileServer
        else if(args[0].equals("FastFileSvr") && args.length < 4) {

            //Ver se args[1] e um IP e ver se args[2] é uma port valida
            if(args[1].matches(Constantes.IPV4Pattern) && args[2].matches(Constantes.PortPattern)) {
                FastFileSrv ffs = new FastFileSrv(args[1], args[2]);
                ffs.start();
            }
            else System.out.println("Parametros Invalidos!");
        }

    }
}
