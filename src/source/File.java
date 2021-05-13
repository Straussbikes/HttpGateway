package source;

import java.util.LinkedList;
import java.util.Queue;

public class File {
    private int numSequence = 0;
    private boolean isComplete = false;
    private Queue<Chunk> chunks = new LinkedList();

    public File() {
    }

    public void addChunkQueue(Chunk chunk) {
        if (!this.isComplete) {
            this.chunks.add(chunk);
            ++this.numSequence;
            if (chunk.isLast()) {
                this.isComplete = true;
            }
        }

    }

    public Chunk remChunkQueue() {
        Chunk c = null;
        if (!this.chunks.isEmpty()) {
            c = (Chunk)this.chunks.poll();
        }

        return c;
    }

    public int getNumSequence() {
        return this.getNumSequence();
    }
}
