package dev.JustRed23.ListenDotMoe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.JustRed23.ListenDotMoe.Music.Song;
import dev.JustRed23.ListenDotMoe.Music.SongUpdateEvent;
import dev.JustRed23.ListenDotMoe.websocket.Client;
import dev.JustRed23.ListenDotMoe.websocket.ReconnectHandler;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class ListenDotMoe implements Runnable {

    public static final double VERSION = 3.0;

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenDotMoe.class);

    private static ClientManager clientManager;
    private static Client client;

    private static SongUpdateEvent songUpdateEvent;

    public static final String LDM_ALBUM_ENDPOINT = "https://cdn.listen.moe/covers/";
    public static final String LDM_ARTISTS_ENDPOINT = "https://cdn.listen.moe/artists/";

    private boolean running = false;

    private static boolean loggingEnabled = true;

    public ListenDotMoe() {
        clientManager = ClientManager.createClient();

        ReconnectHandler reconnectHandler = new ReconnectHandler(this);
        client = new Client(reconnectHandler);

        clientManager.getProperties().put(ClientProperties.RECONNECT_HANDLER, reconnectHandler);
    }

    public void start() {
        if (running) return;
        running = true;

        Thread thread = new Thread(this,"ListenDotMoe");
        thread.start();
    }

    public void stop() {
        if (!running) return;
        running = false;

        client.close();
    }

    public void run() {
        LOGGER.info("Starting ListenDotMoe V{}", VERSION);

        client.addMessageHandler(this::processMessage);

        try {
            clientManager.connectToServer(client, URI.create("wss://listen.moe/gateway_v2"));
            client.closeLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LOGGER.info("Stopping...");
    }

    private synchronized void processMessage(String message) {
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

    public void disableLogging() {
        loggingEnabled = false;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public boolean isRunning() {
        return running;
    }
}
