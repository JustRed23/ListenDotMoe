package dev.JustRed23.ListenDotMoe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.JustRed23.ListenDotMoe.Endpoint.Client;
import dev.JustRed23.ListenDotMoe.Music.Song;
import dev.JustRed23.ListenDotMoe.Music.SongUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class ListenDotMoe {

    public static final double VERSION = 2.1;

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenDotMoe.class);

    private static Client client;
    private static SongUpdateEvent songUpdateEvent;

    public static final String LDM_ALBUM_ENDPOINT = "https://cdn.listen.moe/covers/";
    public static final String LDM_ARTISTS_ENDPOINT = "https://cdn.listen.moe/artists/";

    private boolean running = false;

    private static boolean loggingEnabled = true;

    public void start() {
        if (running) return;
        running = true;

        LOGGER.info("Starting ListenDoeMoe V" + VERSION);

        Thread thread = new Thread(() -> {
            client = new Client(URI.create("wss://listen.moe/gateway_v2"));
            client.addMessageHandler(ListenDotMoe::processMessage);

            try {
                client.connectBlocking();
                client.closeLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            LOGGER.info("Stopping...");
        }, "ListenDotMoe");
        thread.start();
    }

    public void stop() {
        if (!running) return;
        running = false;

        try {
            client.closeBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void processMessage(String message) {
        JsonObject json = (JsonObject) JsonParser.parseString(message);

        switch (json.get("op").getAsInt()) {
            case 0:
                client.setHeartbeat(json.get("d").getAsJsonObject().get("heartbeat").getAsLong());
                break;
            case 1:
                Song song = new Song(json);
                if (songUpdateEvent != null)
                    songUpdateEvent.onSongUpdate(song);
                break;
            case 10:
            default:
                break;
        }
    }

    public void addSongEventHandler(SongUpdateEvent handler) {
        songUpdateEvent = handler;
    }

    public static boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public void setLoggingEnabled(boolean loggingEnabled) {
        ListenDotMoe.loggingEnabled = loggingEnabled;
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
