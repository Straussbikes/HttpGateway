package source;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

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
public ReentrantLock lock=new ReentrantLock();
    // Buffer
    private byte[] buf = new byte[65000];

    public FastFileSrv(String ip) throws UnknownHostException {
        this.ipAdress =InetAddress.getByName(ip);

    }

    @Override
    public void run() {

        int udpport = 80;
        // Tem socket ligada ao server
        this.running = true;
while(running) {
    lock.lock();
    DatagramSocket socket = null;
    boolean conexao = false;
    //connecta-se ao udpworker
    while(!conexao) {
        try {
            socket = new DatagramSocket(udpport);
            System.out.println("ola " + ipAdress);
            conexao = true;
        } catch (SocketException e) {
            udpport++;
        }
    }
    // packet to send

    byte[] bufip = socket.getLocalAddress().getHostName().getBytes(StandardCharsets.UTF_8);

    DatagramPacket send = new DatagramPacket(bufip, bufip.length);
    try {
        socket.send(send);
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Packet
    DatagramPacket packet = null;


    packet = new DatagramPacket(buf, buf.length);


    // Fica a escuta de pedidos primeiro packet contem info sobre ficheiro
    try {
        socket.receive(packet);
    } catch (IOException e) {
        e.printStackTrace();
    }
    String s = new String(packet.getData());
    System.out.println("fast file server a preparar " + s + "\n");
    InetAddress ip_add1 = packet.getAddress();
    int port = packet.getPort();
    // Processa informação do pedido
    // Pega no file
    FileSplit fs = new FileSplit();
    // Dá split do file
    String ret = "";
    try {
        System.out.println(s);

        int i = 0;
        for (i = 0; i < s.length(); i++) {
            if (Character.isLetter(s.charAt(i)) || s.charAt(i) == '.' || Character.isDigit(s.charAt(i))) {
                char tmp = s.charAt(i);
                ret = ret + tmp;
            } else break;
        }

        System.out.println("lol wtf :" + s.length());

        System.out.println("real size " + ret.length());
        //substituir test.mp4 por s
        fs.splitFile(new File("src\\source\\" + ret));

    } catch (IOException e) {
        e.printStackTrace();
    }                                                                                            //substituir test.mp4 por s
    List<File> lista = listOfFilesToMerge(new File("src\\source\\" + ret + ".001"));
    int i = 0;
    Integer info1 = lista.size();
    System.out.println("size lista: " + lista.size());

    //envia a informaçao com o numero de chunks do ficheiro...
    try {
        String ss = info1.toString();

        DatagramPacket infos = new DatagramPacket(ss.getBytes(), ss.getBytes().length, ip_add1, port);
        socket.send(infos);
    } catch (UnknownHostException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

    //cria um map com os chunks ordenados
    int tam = lista.size();
    Map<Integer, Chunk> envio = new TreeMap<Integer, Chunk>();
    for (File f : lista) {
        System.out.println(f.getName());
        Chunk adi = new Chunk(i, i, socket.getInetAddress(), islast(i, tam), readFile(f), f.getName());
        envio.put(adi.getSequenceNum(), adi);
        i++;
    }
    // Envia os respetivos chunks
//envia os chunks e espera respostas de chunks recebidos, continua a enviar ate o map ficar vazio...  -> simulaçao de tcp
    while (manda) {
        for (Map.Entry<Integer, Chunk> entry : envio.entrySet()) {
            try {
                DatagramPacket chunksz = new DatagramPacket(Objects.requireNonNull(Serializer.serialize(entry.getValue())), Objects.requireNonNull(Serializer.serialize(entry.getValue())).length, ip_add1, port);
                socket.send(chunksz);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] retirar = new byte[65407];
        //recebe packet com numero de sequencia do packet recebido do outro lado
        DatagramPacket retira = new DatagramPacket(retirar, retirar.length);
        try {
            socket.receive(retira);
            String msg = new String(retira.getData(), retira.getOffset(), retira.getLength());
            Integer retiraint = Integer.parseInt(msg);
            System.out.println("reposta do UDP: chegou " + retiraint);
            envio.remove(retiraint);
        } catch (IOException e) {
            e.printStackTrace();
        }
if(envio.size()==0) break;
    }
socket.close();
    lock.unlock();
}
       // socket.close();
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
