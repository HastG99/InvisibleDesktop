package ru.study.invisibledesktop.core.packets;

import java.util.ArrayList;
import java.util.List;

public class SResponsePacket extends AServerPacket {

    private int version;
    private List<Long> people = new ArrayList<>();

    public SResponsePacket(byte[] data) {
        super(data);

        version = getInt();

        int online = getInt();

        for (int i = 0; i < getInt(); i++) {
            people.add(getLong());
        }
    }

    public int getVersion() {
        return version;
    }

    public List<Long> getPeople() {
        return people;
    }
}
