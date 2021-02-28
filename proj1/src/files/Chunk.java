package files;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public abstract class Chunk implements Serializable {
    protected String fileId;
    protected int chunkNo;
    protected int replicationDegree;

    public Chunk(String fileId, int chunkNo) {
        this.fileId = fileId;
        this.chunkNo = chunkNo;
        this.replicationDegree = 0;
    }

    public Chunk(String fileId, int chunkNo, int replicationDegree) {
        this.fileId = fileId;
        this.chunkNo = chunkNo;
        this.replicationDegree = replicationDegree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chunk chunk = (Chunk) o;
        return chunkNo == chunk.chunkNo && Objects.equals(fileId, chunk.fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, chunkNo);
    }

    public int getChunkNo() {
        return chunkNo;
    }

    public String getFileId() {
        return fileId;
    }

    public String getChunkId() {
        return fileId + "_" + chunkNo;
    }

    public int getReplicationDegree() {
        return replicationDegree;
    }
}
