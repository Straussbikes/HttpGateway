package source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPCon {
    private Socket socket;
    private InputStream in;
    private OutputStream out;

    public TCPCon(Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }

    public void setOut(OutputStream out) {
        this.out = out;
    }

    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }
//ip?

    public void close() throws IOException {
        if(!this.socket.isClosed()){
            this.in.close();
            this.out.close();
            this.socket.close();
        }
    }
}
