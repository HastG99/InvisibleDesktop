package ru.study.invisibledesktop.core.packets;

public class CPingPacket extends AClientPacket {

    public CPingPacket(int version) {
        putInt(version);
    }
}
