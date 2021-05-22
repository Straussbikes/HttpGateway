package source;

import java.io.*;
import java.io.File;
import java.net.Socket;
import java.util.StringTokenizer;

public class Tcplistener extends Thread {
    public Socket socket;
    static String docroot = ".";
    public TCPCon tcp;
    public String ficheiro;
    public byte[] sending = new byte[100000];
    public Tcplistener(Socket s, TCPCon tcpc) {
        this.socket = s;
        this.tcp = tcpc;
    }

    public byte[] getSending() {
        return sending;
    }

    public void setSending(byte[] sending) {
        this.sending = sending;
    }

    public void run() {
        try {

            PrintStream os = new PrintStream(tcp.getOut());
            BufferedReader is = new BufferedReader(new InputStreamReader(tcp.getIn()));
            String get = is.readLine();

            StringTokenizer st = new StringTokenizer(get);
            String method = st.nextToken();
            if (!method.equals("GET")) {
                System.err.println("Only HTTP \"GET\" implemented");
            } else {
                while((get = is.readLine()) != null && !get.trim().equals("")) {
                }

                String file = st.nextToken();
                setFicheiro(file.substring(1));


                if (file.charAt(0) != '/') {
                    System.err.println("Exiting: Request filename must start with \"/\"");
                } else if (file.indexOf("../") != -1) {
                    System.err.println("Exiting: \"../\" in filename not allowed");
                } else {

                    this.sendFile(os, sending);
                    System.out.println(sending.length);
                    this.socket.close();
                }
            }
        } catch (IOException var7) {
            System.err.println("Cannot accept connection");
        }

    }

    void sendFile(PrintStream os, byte[] theData) {
       // System.err.println("Request for file \"" + docroot + file + "\"");

        try {
            System.out.println(theData.length);
            System.out.println(ficheiro);
            os.write("HTTP/1.1 200 OK\r\n".getBytes());
            os.write("Accept-Ranges: bytes\r\n".getBytes());
            os.write(("Content-Length: " + theData.length + "\r\n").getBytes());
            //content type variation need !!!
            os.write("Content-Type: text/plain; charset=utf-8\r\n".getBytes());
            os.write(("Content-Disposition: attachment; filename=" + ficheiro).getBytes());

            os.write(("Content-Length:" + theData.length + "\r\n").getBytes());
            os.write("\r\n".getBytes());
            os.write("\r\n".getBytes());
            os.write(theData);
            os.flush();
            os.close();
        } catch (IOException var5) {
            os.print("HTTP/1.0 404 Not Found\r\n");
            os.print("Server: Tp2CC/1.0\r\n");
            os.print("Content-type: text/html\r\n");
            os.print("\r\n");
            os.print("<h1>Not found</h1>\n");
            os.print("<p>File \"" + ficheiro + "\" not found</p>\n");
            os.print("<p><em>TP2CC/1.0</em></p>\n");
        }

    }



    public TCPCon getTcp() {
        return tcp;
    }

    public void setTcp(TCPCon tcp) {
        this.tcp = tcp;
    }

    public String getFicheiro() {
        return ficheiro;
    }

    public void setFicheiro(String ficheiro) {
        this.ficheiro = ficheiro;
    }
}
