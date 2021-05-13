package source;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Chunk {
    private int id;
    private boolean isLastPacket;
    private int numSequence;
    private int destino;
    private byte[] data;

    public Chunk(int id, int numSequence, boolean isLastPacket, int destino, byte[] data) {
        this.id = id;
        this.isLastPacket = isLastPacket;
        this.numSequence = numSequence;
        this.destino = destino;
        this.data = data;
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

    public int getDestination() {
        return this.destino;
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
}
