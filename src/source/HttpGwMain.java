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
    static final int MAX_T = 10;
  public   HTTPGw main_server = new HTTPGw();
   public DatagramSocket dserver;
    public static void main (String[] args) throws IOException  {
        HTTPGw main_server = new HTTPGw();
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
         //   DatagramSocket dsocket = new DatagramSocket(5000);

            // Main Server
            main_server.setIp(ip);

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
                 //   main_server.setDataSocket(dsocket);

                    // Start do server
                    main_server.run();

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
                System.out.println("Fast file server ");

                    try {
                        // cria thread ffs e adiciona a pool de servers disponiveis pool nao ta a funcionar
                            FastFileSrv ffs = new FastFileSrv(args[1]);
                            Queue<FastFileSrv> aux = main_server.getPoolServer();
                            aux.add((FastFileSrv) ffs);
                            main_server.setPoolServer(aux);
                            System.out.println(main_server.getPoolServer().size());

                                ffs.start();


                    } catch (IOException e) {
                        System.err.println("cannot start ffs");
                    }
                             }



            } else System.out.println("Parametros Invalidos!");
        }

    }

