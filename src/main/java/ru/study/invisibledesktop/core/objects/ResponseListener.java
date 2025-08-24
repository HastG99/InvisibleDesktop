package ru.study.invisibledesktop.core.objects;

import ru.study.invisibledesktop.core.packets.SResponsePacket;

public interface ResponseListener {

    void onReponse(SResponsePacket packet);

}
