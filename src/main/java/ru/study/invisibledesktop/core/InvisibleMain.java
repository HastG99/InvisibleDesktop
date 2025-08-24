package ru.study.invisibledesktop.core;

import ru.study.invisibledesktop.core.objects.Profile;
import ru.study.invisibledesktop.core.objects.Session;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class InvisibleMain {

    public static Profile profile;

    public static List<Long> online = new ArrayList<>();

    public static boolean init() {
        File config = new File("invisibledesktop.properties");

        if(!config.exists()) {
            try {
                InputStream is = InvisibleMain.class.getClassLoader().getResourceAsStream("invisibledesktop.properties");

                if(is == null)
                    throw new NullPointerException("InputStream cannot be null.");

                Files.copy(is, config.toPath());
            } catch (IOException e) {
                return false;
            }
        }

        try {
            Properties conf = new Properties();
            conf.load(new FileInputStream("invisibledesktop.properties"));

            String host = conf.getProperty("host");
            int port = Integer.parseInt(conf.getProperty("port"));

            if(conf.getProperty("id") == null) {
                conf.setProperty("id", String.valueOf(generateId()));
                conf.store(new FileWriter("invisibledesktop.properties"), "Settings");
            }

            profile = new Profile(host, port, Long.parseLong(conf.getProperty("id")));

            return true;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public static void pingServer() {
        Session pinger = new Session("pinger", "localhost", 1337, -1);

        pinger.setResponseListener(packet -> {
            InvisibleMain.online = packet.getPeople();
            pinger.setRunning(false);
        });
        pinger.sendPing();
    }

    public static long generateId() {
        Random random = new Random();
        return Math.abs(random.nextLong());
    }

    public static Session connect(String password) {
        Session session = new Session(password, profile.getHost(), profile.getPort(), profile.getId());

        new Thread(session).start();

        return session;
    }

}
