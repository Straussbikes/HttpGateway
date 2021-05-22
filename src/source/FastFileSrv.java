package source;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import static source.FileSplit.listOfFilesToMerge;

// TODO falta definir conexão UDP com o HTTPGW

public class FastFileSrv extends Thread {
    // Target IP
    private InetAddress ipAdress;

    private  Boolean manda = true;
    // Target port
   public DatagramSocket socket;

    // Server status
    private boolean running = false;

    public InetAddress getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(InetAddress ipAdress) {
        this.ipAdress = ipAdress;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public byte[] getBuf() {
        return buf;
    }

    public void setBuf(byte[] buf) {
        this.buf = buf;
    }

    // Buffer
    private byte[] buf = new byte[1024*1024];

    public FastFileSrv(String ip, String port) throws SocketException, UnknownHostException {
        this.ipAdress =InetAddress.getByName(ip);

    }

    @Override
    public void run() {
        // Tem socket ligada ao server
        this.running = true;
        DatagramSocket socket = null;
        try {
            System.out.println(ipAdress);
            socket = new DatagramSocket(Constantes.UDPPort);

        } catch (SocketException e) {
            e.printStackTrace();
        }
        while(running) {
            // Packet
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            // Fica a escuta de pedidos
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String s = new String(buf, StandardCharsets.UTF_8);
            System.out.println("fast file server a preparar " +s+"\n");
            // Processa informação do pedido
            // Pega no file
            java.io.File files = new java.io.File("C:\\Users\\StraussBikes\\Desktop\\3ano2sem\\CC\\HttpGateway\\src\\source\\" + s);
            FileSplit fs = new FileSplit();
            // Dá split do file
            try {
                fs.splitFile(files);
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<File> lista = listOfFilesToMerge(new File ("C:\\Users\\StraussBikes\\Desktop\\3ano2sem\\CC\\HttpGateway\\src\\source\\"+s+".001"));
            int i=0;
            Integer info1= lista.size();
            DatagramPacket info = new DatagramPacket(String.valueOf(info1).getBytes(),String.valueOf(info1).getBytes().length);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int tam= lista.size();
            Map<Integer,Chunk> envio= new TreeMap<Integer,Chunk>();
            for(File f: lista){
                    Chunk adi = new Chunk(i+1,i+1,islast(i+1,tam),readFile(f));
                    envio.put(adi.getSequenceNum(),adi);
            }
            // Envia os respetivos chunks

            while(manda) {
                for (Map.Entry<Integer, Chunk> entry : envio.entrySet()) {
                    try {
                        packet.setData(entry.getValue().toBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                buf=null;
             DatagramPacket packet2= new DatagramPacket(buf,buf.length);
                try {
                    socket.receive(packet2);
                    String rec= buf.toString();
                    if(rec.equals("Stop"));
                    manda=false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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

    public Boolean islast(Integer index,Integer fim){
        if(index==fim){
            return true;
        }else return false;
    }



}
