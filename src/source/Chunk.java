package source;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class Chunk implements Serializable {
    // Id do chunk
    private int id;

    // Boolean para fim de chunks
    private boolean isLastPacket;

    // Num de sequencia
    private int numSequence;

    private String nome;
    // Payload
    private byte[] data;

    // Construtor
    public Chunk(int id, int numSequence, boolean isLastPacket, byte[] data,String nome) {
        this.id = id;
        this.isLastPacket = isLastPacket;
        this.numSequence = numSequence;
        this.data = data;
        this.nome=nome;
    }

    public int getId() {
        return this.id;
    }

    public boolean isLast() {
        return this.isLastPacket;
    }

    public int getSequenceNum() {
        return this.numSequence;
    }

    public boolean isLastPacket() {
        return isLastPacket;
    }

    public void setLastPacket(boolean lastPacket) {
        isLastPacket = lastPacket;
    }

    public int getNumSequence() {
        return numSequence;
    }

    public void setNumSequence(int numSequence) {
        this.numSequence = numSequence;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        oos.flush();
        return bos.toByteArray();
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "id=" + id +
                ", isLastPacket=" + isLastPacket +
                ", numSequence=" + numSequence +
                ", data=" + Arrays.toString(data) +
                '}';
    }

}