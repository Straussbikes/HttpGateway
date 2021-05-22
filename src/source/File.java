package source;

import java.util.LinkedList;
import java.util.Queue;

public class File {
    private String name;
    private boolean isComplete = false;
    private Queue<Chunk> chunks = new LinkedList();

    public File() {
        this.chunks = new LinkedList<>();
        this.isComplete = false;
    }

    public void addChunkQueue(Chunk chunk) {
        if (!this.isComplete && chunk != null) {
            this.chunks.add(chunk);
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
