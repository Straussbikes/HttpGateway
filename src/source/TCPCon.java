package source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPCon {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    public String ficheiro;

    public String getFicheiro() {
        return ficheiro;
    }

    public void setFicheiro(String ficheiro) {
        this.ficheiro = ficheiro;
    }

    public TCPCon(Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    public InputStream getIn() {
        return this.in;
    }

    public OutputStream getOut() {
        return this.out;
    }

    public void close() throws IOException {
        if (!this.socket.isClosed()) {
            this.in.close();
            this.out.close();
            this.socket.close();
        }

    }
}
