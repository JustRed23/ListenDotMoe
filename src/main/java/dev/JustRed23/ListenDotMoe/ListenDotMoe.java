package dev.JustRed23.ListenDotMoe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.JustRed23.ListenDotMoe.Endpoint.LDMEndpoint;
import dev.JustRed23.ListenDotMoe.Music.Song;
import dev.JustRed23.ListenDotMoe.Music.SongUpdateEvent;
import jakarta.websocket.CloseReason;
import org.fusesource.jansi.AnsiConsole;

import static dev.JustRed23.ListenDotMoe.Utils.Logger.*;

public class ListenDotMoe {

    public static final double VERSION = 1.1;

    private static LDMEndpoint endpoint;
    private static SongUpdateEvent songUpdateEvent;

    public static final String LDM_ALBUM_ENDPOINT = "https://cdn.listen.moe/covers/";
    public static final String LDM_ARTISTS_ENDPOINT = "https://cdn.listen.moe/artists/";

    private boolean running = false;

    public void start() {
        if (running) return;
        running = true;
        AnsiConsole.systemInstall();

        Thread thread = new Thread(() -> {
            info("Starting ListenDotMoe V" + VERSION);
            endpoint = new LDMEndpoint("wss://listen.moe/gateway_v2");
            endpoint.addMessageHandler(ListenDotMoe::processMessage);

            try {
                endpoint.closeLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            info("Closed client");
        }, "ListenDotMoe");
        thread.start();
    }

    public void stop() {
        if (!running) return;
        running = false;
        info("Closing client");
        endpoint.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Closing client"));
        AnsiConsole.systemUninstall();
    }

    private static synchronized void processMessage(String message) {
        JsonObject json = (JsonObject) JsonParser.parseString(message);

        switch (json.get("op").getAsInt()) {
            case 0:
                debug("Received welcome message");
                endpoint.setHeartbeatInterval(json.get("d").getAsJsonObject().get("heartbeat").getAsLong());
                break;
            case 1:
                info("Received song information");
                Song song = new Song(json);
                if (songUpdateEvent != null)
                    songUpdateEvent.onSongUpdate(song);
                break;
            case 10:
                debug("Received heartbeat");
                break;
            default:
                debug("Received invalid OP code! Ignoring");
                break;
        }
    }

    public void addSongEventHandler(SongUpdateEvent handler) {
        songUpdateEvent = handler;
    }

    public void disableLogger(boolean disable) {
        disableLogging = disable;
    }

    public void enableDebug(boolean enable) {
        debug = enable;
    }
}
