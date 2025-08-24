package ru.study.invisibledesktop.core.packets;

public class CHandshakePacket extends AClientPacket {

    public CHandshakePacket(int version, long id) {
        putInt(version);
        putLong(id);
    }
}
