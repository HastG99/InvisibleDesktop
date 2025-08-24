package ru.study.invisibledesktop.core.objects;

import ru.study.invisibledesktop.core.converters.Encryptor;
import ru.study.invisibledesktop.core.converters.TextConverter;
import ru.study.invisibledesktop.core.packets.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Session implements Runnable, Encryptor, TextConverter {

    public static int VERSION = 1;

    private long serverId;
    private int serverVersion;

    private MessageListener messageListener = new MessageListener() {
        @Override
        public void onMessage(long senderId, String message) {

        }

        @Override
        public void onMessage(String message) {

        }
    };

    private ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onReponse(SResponsePacket packet) {

        }
    };

    private final long id;
    private final String password;
    private final byte[] key;
    private final String host;
    private final int port;

    private Socket socket;
    private OutputStream os;
    private InputStream is;

    private boolean logged = false;
    public boolean running = true;

    public Session(String password, String host, int port, long id) {
        this.host = host;
        this.port = port;
        this.id = id;
        this.password = password;
        this.key = genKey(password);
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getKey() {
        return key;
    }

    public boolean isLogged() {
        return logged;
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public void setServerVersion(int serverVersion) {
        this.serverVersion = serverVersion;
    }

    public ResponseListener getResponseListener() {
        return responseListener;
    }

    public void setResponseListener(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public long getServerId() {
        return serverId;
    }

    public int getServerVersion() {
        return serverVersion;
    }

    public void handleData(byte packetId, byte[] bytes) {
        if(!logged) {
            if(packetId == 0x0) {
                SHandshakePacket packet = new SHandshakePacket(bytes);

                setServerId(packet.getId());
                setServerVersion(packet.getVersion());

                logged = true;

                System.out.printf("CONNECTED: [%s] host:%s:%d version:%d\n", formatID(serverId), host, port, serverVersion);
                System.out.printf("PROFILE: [%s] %d, version:%d\n", formatID(id), id, VERSION);

                return;
            }

            if(packetId == 0x3) {
                SResponsePacket packet = new SResponsePacket(bytes);
                responseListener.onReponse(packet);

                return;
            }

            return;
        }

        switch (packetId) {
            case 0x1: {
                sendPacket(bytes);
                return;
            }

            case 0x2: {
                SMessagePacket packet = new SMessagePacket(bytes);

                try {
                    byte[] decrypted = decrypt(packet.getMessage(), key);
                    String message = new String(decrypted, StandardCharsets.UTF_8);

                    System.out.printf("[%s] => %s\n", formatID(packet.getSender()), message);

                    messageListener.onMessage(packet.getSender(), message);
                } catch (Exception e) {

                }

                return;
            }

            case 0x3: {
                SResponsePacket packet = new SResponsePacket(bytes);
                responseListener.onReponse(packet);

                return;
            }

        }
    }

    public void sendMessage(String message) {
        try {
            if(message.isEmpty())
                return;

            byte[] encrypted = encrypt(message.getBytes(StandardCharsets.UTF_8), key);

            sendPacket(0x2, new CMessagePacket(encrypted));
        } catch (Exception e) {
        }
    }

    public void sendPacket(int packetId, AClientPacket packet) {
        try {
            DataOutputStream dos = new DataOutputStream(os);

            byte[] bytes = packet.getData();

            dos.writeByte(packetId);
            dos.writeInt(bytes.length);
            dos.write(bytes);
            dos.flush();
        } catch (Exception e) {
        }
    }

    public void sendPacket(byte[] bytes) {
        try {
            DataOutputStream dos = new DataOutputStream(os);
            dos.write(bytes);
            dos.flush();
        } catch (Exception e) {
        }
    }

    public void sendHandshake() {
        sendPacket(0x0, new CHandshakePacket(VERSION, id));
    }

    public void sendPing() {
        sendPacket(0x3, new CPingPacket(VERSION));
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host,port);

            is = socket.getInputStream();
            os = socket.getOutputStream();

            DataInputStream dis = new DataInputStream(is);

            if(id >= 0)
                sendHandshake();

            while(!socket.isClosed() && running) {
                if(is.available() > 0) {
                    byte packetId = dis.readByte();
                    int len = dis.readInt();

                    if (len < 1)
                        throw new IOException("len < 1");

                    byte[] bytes = new byte[len];

                    if (dis.read(bytes) == -1)
                        throw new IOException("read = -1");

                    handleData(packetId, bytes);
                }

                Thread.sleep(50);
            }


            is.close();
            os.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

}
