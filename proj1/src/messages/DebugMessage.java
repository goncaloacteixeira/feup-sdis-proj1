package messages;

import peer.Peer;
import tasks.DebugTask;
import tasks.Task;

public class DebugMessage extends Message {
    public DebugMessage(String protocolVersion, String senderId, String fileId, int chunkNo, int replicationDegree, byte[] body) {
        super(protocolVersion, "DEBUG", senderId, fileId, chunkNo, replicationDegree, body);
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
        return new DebugTask(this, peer);
    }
}