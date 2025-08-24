package ru.study.invisibledesktop.core.packets;

import java.nio.charset.StandardCharsets;

public class CMessagePacket extends AClientPacket{

    public CMessagePacket(byte[] message) {
        putData(message);
    }
}
