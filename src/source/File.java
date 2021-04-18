package source;

import java.util.LinkedList;
import java.util.Queue;

public class File {
    // Numero de sequência
    private int numSequence;

    // Flag que indica se o file está completo
    private boolean isComplete;

    // Componentes do file
    private Queue<Chunk> chunks;

    // Construtor default
    public File(){
        this.numSequence = 0;
        this.isComplete = false;
        this.chunks = new LinkedList<>();
    }

    // Função que adiciona chunks a queue -> Download
    public void addChunkQueue(Chunk chunk){
        if(!this.isComplete){
            // Adição do chunk à queue
            this.chunks.add(chunk);

            // Incrementa número de sequência
            this.numSequence++;

            // Se for o ultimo chunk do file ativa a flag
            if(chunk.isLast())
                this.isComplete = true;
        }
    }

    // Função que remove chunks da queue -> Upload
    public Chunk remChunkQueue(){
        // Criação do chunk temporário
        Chunk c = null;

        // Se a Queue não estiver vazia retira-se o primeiro na queue.
        if(!this.chunks.isEmpty()){
            c = this.chunks.poll();
        }

        return c;
    }

    public int getNumSequence(){
        return this.getNumSequence();
    }
}
