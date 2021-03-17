package tasks;

import files.SavedChunk;
import jobs.SendStoredChunk;
import messages.Message;
import messages.PutchunkMessage;
import messages.StoredMessage;
import peer.Peer;

import java.util.concurrent.TimeUnit;

public class PutchunkTask extends Task {
    public PutchunkTask(PutchunkMessage message, Peer peer) {
        super(message, peer);
    }

    @Override
    public void run() {
        SavedChunk chunk = new SavedChunk(message.getFileId(), message.getChunkNo(), message.getReplicationDegree(), message.getBody());

        Message reply = new StoredMessage(peer.getProtocolVersion(), peer.getPeerId(), message.getFileId(), message.getChunkNo());

        if (!this.peer.getInternalState().isAcceptingRequests()) {
            // System.out.println("[PUTCHUNK] Peer is not accepting requests as of now. It is probably reclaiming some space.");
            return;
        }

        if (chunk.isStored() && this.peer.getInternalState().getSavedChunksMap().containsKey(chunk.getChunkId())) {
            // This peer has this chunk but it will send a reply anyways cause it indicates that it has saved the chunk (UDP unreliability)
            chunk.setReceivedPutchunk(true);
            peer.getRequestsExecutor().schedule(new SendStoredChunk(chunk, peer, reply), this.getSleepTime(), TimeUnit.MILLISECONDS);
        } else if (!this.peer.getInternalState().getSentChunksMap().containsKey(chunk.getChunkId())) {
            if (chunk.getBody().length + this.peer.getInternalState().getOccupation() > this.peer.getInternalState().getCapacity()) {
                // I dont have the storage needed to backup that, i'm afraid
                System.out.printf("[PEER] Not enough space for %s\n", chunk.getChunkId());
                return;
            }
            peer.getInternalState().getSavedChunksMap().put(chunk.getChunkId(), chunk);
            peer.getRequestsExecutor().schedule(new SendStoredChunk(chunk, peer, reply), this.getSleepTime(), TimeUnit.MILLISECONDS);
        }
    }
}
