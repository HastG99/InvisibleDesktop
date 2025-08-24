package ru.study.invisibledesktop.core.objects;

public class Profile {

    private final String host;
    private final int port;
    private final long id;

    public Profile(String host, int port, long id) {
        this.host = host;
        this.port = port;
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public long getId() {
        return id;
    }
}
