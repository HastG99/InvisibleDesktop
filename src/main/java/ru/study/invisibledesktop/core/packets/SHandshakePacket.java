package ru.study.invisibledesktop.core.packets;

public class SHandshakePacket extends AServerPacket {

    private final int version;
    private final long id;

    public SHandshakePacket(byte[] data) {
        super(data);

        version = getInt();
        id = getLong();
    }

    public int getVersion() {
        return version;
    }

    public long getId() {
        return id;
    }

}
