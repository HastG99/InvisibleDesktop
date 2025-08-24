package ru.study.invisibledesktop.core.packets;

public class SMessagePacket extends AServerPacket{

    private final long sender;
    private final long time;
    private final byte[] message;

    public SMessagePacket(byte[] data) {
        super(data);

        this.sender = getLong();
        this.time = getLong();
        this.message = getData();
    }

    public long getSender() {
        return sender;
    }

    public long getTime() {
        return time;
    }

    public byte[] getMessage() {
        return message;
    }
}
