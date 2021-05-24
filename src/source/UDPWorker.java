package source;

import javax.xml.crypto.Data;
import java.io.*;
import java.io.File;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UDPWorker extends Thread implements Serializable{

   private  FileSplit fs = new FileSplit();
    private boolean running = false;
    private byte[] buf = new byte[65507];
    private byte[] chunkbuf = new byte[65506];
    public String ficheiro;
    public InetAddress ip;
    public Map<Integer,Chunk> conteudo;
    public byte[] sending=null;
 //   public  DatagramSocket socket;
    public Integer info2;
    public Boolean wait=true;
    public UDPWorker(String ficheiro,DatagramSocket socket){
        this.ficheiro=ficheiro;
//this.socket = socket;
    }


    public byte[] getSending() {
        return sending;
    }

    public void setSending(byte[] sending) {
        this.sending = sending;
    }

    public void run(){
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(5000);
        } catch (SocketException e) {
            e.printStackTrace();
        }


        conteudo = new TreeMap<Integer, Chunk>();
            running = true;
            Boolean informa = true;


                // Packet

                if (informa) {
                    DatagramPacket packet = null;

                    try {
                        packet = new DatagramPacket(ficheiro.getBytes(), 0,ficheiro.getBytes().length,InetAddress.getLocalHost(),Constantes.UDPPort);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }

                    try {
                        socket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Packet a transmitir -> INFO DO FILE A TRANSMITIR
                    DatagramPacket areceber = null;

                        byte[] areceive = new byte[65407];

                        areceber = new DatagramPacket(areceive, areceive.length);

                    try {
                        socket.receive(areceber);

                        String msg = new String(areceber.getData(), areceber.getOffset(), areceber.getLength());
                        System.out.println("devia ser int "+msg);
                        info2= Integer.parseInt(msg);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    informa = false;
                }
                DatagramPacket chunkpacket = null;

                    chunkpacket = new DatagramPacket(chunkbuf, chunkbuf.length);


        while (conteudo.size() != info2) {
                    try {
                        FileSplit fs = new FileSplit();
                        socket.receive(chunkpacket);
                        Chunk chunk = (Chunk) Serializer.deserialize(chunkpacket.getData());
                        if(chunk != null){
                            System.out.println("Chegou "+ chunk.getNome());
                         String rrs=String.valueOf(chunk.getSequenceNum());
                         DatagramPacket resposta = new DatagramPacket(rrs.getBytes(), 0,rrs.getBytes().length,InetAddress.getLocalHost(),Constantes.UDPPort);
                         socket.send(resposta);
                         conteudo.put(chunk.getSequenceNum(), chunk);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        wait=false;
System.out.println("chegaram estes chunks "+conteudo.size());
/*
                buf = null;
                String stop= "Stop";
        DatagramPacket stops = null;
        try {
            stops = new DatagramPacket(stop.getBytes(),stop.getBytes().length, InetAddress.getLocalHost(), Constantes.UDPPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
                    socket.send(stops);

                } catch (IOException e) {
                    e.printStackTrace();
                }
*/
                List<File> files = new ArrayList<File>(info2);
                for (Map.Entry<Integer, Chunk> entry : conteudo.entrySet()) {

                System.out.println("indice = "+ entry.getKey());
                    //files.add(entry.getKey(), (File) Serializer.deserialize(entry.getValue().getData()));
                    File file = new File("C:\\Users\\StraussBikes\\Desktop\\3ano2sem\\CC\\HttpGateway2\\src\\source\\download\\"+entry.getValue().getNome());
                    files.add(entry.getKey(),file);
                    BufferedOutputStream bos = null;
                    try {
                        bos = new BufferedOutputStream(new FileOutputStream(file));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        bos.write(entry.getValue().getData());
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                File merged = new File("C:\\Users\\StraussBikes\\Desktop\\3ano2sem\\CC\\HttpGateway2\\src\\source\\download\\" + ficheiro);
                try {


                        FileSplit.mergeFiles(files, merged);
                        Thread.sleep(1000);
                        sending = readFile(merged);

                    } catch(IOException | InterruptedException e){
                        e.printStackTrace();
                    }



socket.close();

        }

    private byte[] bigIntToByteArray( final int i ) {
        BigInteger bigInt = BigInteger.valueOf(i);
        return bigInt.toByteArray();
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

    public boolean getWait() {
       return this.wait;
    }
}
