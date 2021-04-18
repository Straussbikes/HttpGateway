package source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.Socket;


public class HTTPGWClient {


    public static void main( String[] args ) {
        String path= " ";

        try {
            Socket socket = new Socket("localhost", Constantes.TCPPort);
            PrintStream out = new PrintStream( socket.getOutputStream() );
            BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );

            out.println( "GET " + path + " HTTP/1.0" );
            out.println();




            in.close();
            out.close();
            socket.close();

    }catch(IOException e){
        e.printStackTrace();
    }
    }

}