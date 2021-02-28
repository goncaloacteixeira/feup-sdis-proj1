package messages;

import peer.Peer;
import tasks.ChunkTask;
import tasks.Task;

public class ChunkMessage extends Message {
    public ChunkMessage(String protocolVersion, String senderId, String fileId, int chunkNo, int replicationDegree, byte[] body) {
        super(protocolVersion, "CHUNK", senderId, fileId, chunkNo, replicationDegree, body);
    }

    @Override
    public byte[] encodeToSend() {
        byte[] header = super.encodeToSend();

        byte[] toSend = new byte[header.length + this.body.length];
        System.arraycopy(header, 0, toSend, 0, header.length);
        System.arraycopy(this.body, 0, toSend, header.length, body.length);
        return toSend;
    }

    @Override
    public Task createTask(Peer peer) {
        return new ChunkTask(this, peer);
    }
}
