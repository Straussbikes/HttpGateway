package source;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Chunk {
    // Id do chunk
    private int id;

    // Boolean para fim de chunks
    private boolean isLastPacket;

    // Num de sequencia
    private int numSequence;

    // Payload
    private byte[] data;

    // Construtor
    public Chunk(int id, int numSequence, boolean isLastPacket, byte[] data) {
        this.id = id;
        this.isLastPacket = isLastPacket;
        this.numSequence = numSequence;
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
