package ru.study.invisibledesktop.core.objects;

public interface MessageListener {

    void onMessage(long senderId, String message);

    void onMessage(String message);

}
