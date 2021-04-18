package source;

import java.io.*;
import java.net.*;

public class HTTPGw {

    public static void main (String[] args) throws IOException {

        DatagramSocket socket = new DatagramSocket(80);
        ServerSocket serverSocket = new ServerSocket(80);
        boolean running;
        byte[] buf = new byte[256];


        while (true) {
            Socket clientSocket = serverSocket.accept();

            Thread actionHandling = new Thread() {

                DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                DataOutputStream out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

                public void run(){

                    new Thread(() -> {

                    }).start();
                }
            };
        }
    }
}
