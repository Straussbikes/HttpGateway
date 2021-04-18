package source;

import java.io.IOException;

public class Chunk {
    private int id;
    private boolean isLastPacket;
    private int numSequence;
    private int destino;
    private byte[] data;

    /*Criação de um Packet Default Constructor*/
    public Chunk(int id, int numSequence, boolean isLastPacket, int destino, byte[] data) {
        this.id = id;
        this.isLastPacket = isLastPacket;
        this.numSequence = numSequence;
        this.destino = destino;
        this.data = data;
    }

    /*
    * Getters
    */
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

    /*
    * Setters
    */
    public void setId(int id) {
        this.id = id;
    }

    public void setData (byte[] data){
        this.data = data;
    }
}
