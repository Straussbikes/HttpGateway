package source;

import java.io.*;
import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UDPWorker extends Thread{
    private DatagramSocket socket;
   private  FileSplit fs = new FileSplit();
    private boolean running = false;
    private byte[] buf = new byte[1024*1024];
    private byte[] chunkbuf = new byte[1024*1024];
    public String ficheiro;
    public InetAddress ip;
    public Map<Integer,Chunk> conteudo;
    public byte[] sending= new byte[100000];

    public UDPWorker(String ficheiro,InetAddress ip,DatagramSocket socket){
        this.ficheiro=ficheiro;
        this.ip = ip;
        this.socket=socket;
    }


    public byte[] getSending() {
        return sending;
    }

    public void setSending(byte[] sending) {
        this.sending = sending;
    }

    public void run(){


        conteudo = new TreeMap<Integer,Chunk>();
        running = true;
        Boolean informa= true;

        while(running) {
            // Packet
            Integer info2 = null;
            if (informa) {
                DatagramPacket packet = new DatagramPacket(ficheiro.getBytes(), ficheiro.getBytes().length);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Packet a transmitir -> INFO DO FILE A TRANSMITIR
                DatagramPacket areceber = new DatagramPacket(buf, buf.length);
                try {
                    socket.receive(areceber);
                    String s = new String(buf, StandardCharsets.UTF_8);
                    info2 = Integer.parseInt(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                informa = false;
            }
            DatagramPacket chunkpacket = new DatagramPacket(chunkbuf, chunkbuf.length);
            while (conteudo.size() != info2) {
                try {
                    FileSplit fs = new FileSplit();
                    socket.receive(chunkpacket);
                    Chunk chunk = (Chunk) fs.deserialize(chunkbuf);
                    conteudo.put(chunk.getSequenceNum(), chunk);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
            buf=null;
            DatagramPacket stop = new DatagramPacket(buf,buf.length);
            String s="Stop";
            buf = s.getBytes(StandardCharsets.UTF_8);
            try {
                socket.send(stop);

            } catch (IOException e) {
                e.printStackTrace();
            }
            List<File> files = new ArrayList<>();
            for (Map.Entry<Integer,Chunk> entry : conteudo.entrySet()) {
                try {
                    files.set(entry.getKey(), (File) fs.deserialize(entry.getValue().getData()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
            File merged = new File("C:\\Users\\StraussBikes\\Desktop\\3ano2sem\\CC\\HttpGateway\\src\\source\\download\\"+ficheiro);
            try {
                fs.mergeFiles(files,merged);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sending=readFile(merged);
        }

        socket.close();
        }

    private static byte[] readFile(File file) {
        FileInputStream fis = null;
        byte[] bArray = new byte[(int)file.length()];

        try {
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();
        } catch (IOException var12) {
            var12.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException var11) {
                    var11.printStackTrace();
                }
            }

        }

        return bArray;
    }

}
