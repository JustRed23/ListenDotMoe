package dev.JustRed23.ListenDotMoe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.JustRed23.ListenDotMoe.Endpoint.LDMEndpoint;
import dev.JustRed23.ListenDotMoe.Music.Song;
import dev.JustRed23.ListenDotMoe.Music.SongUpdateEvent;
import jakarta.websocket.CloseReason;
import org.fusesource.jansi.AnsiConsole;

import java.util.Arrays;

import static dev.JustRed23.ListenDotMoe.Utils.Logger.*;

public class ListenDotMoe implements Runnable {

    public static final double VERSION = 1.0;

    private static LDMEndpoint endpoint;

    public static final String LDM_ALBUM_ENDPOINT = "https://cdn.listen.moe/covers/";
    public static final String LDM_ARTISTS_ENDPOINT = "https://cdn.listen.moe/artists/";

    public void start(String[] args) {
        AnsiConsole.systemInstall();

        ListenDotMoe listenDotMoe = new ListenDotMoe();

        Thread thread = new Thread(listenDotMoe, "ListenDotMoe");
        thread.start();
    }

    public void stop() {
        info("Closing client");
        endpoint.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Closing client"));
        AnsiConsole.systemUninstall();
    }

    public void run() {
        info("Starting ListenDotMoe V" + VERSION);
        endpoint = new LDMEndpoint("wss://listen.moe/gateway_v2");
        endpoint.addMessageHandler(this::processMessage);

        try {
            endpoint.closeLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        info("Closed client");
    }

    private synchronized void processMessage(String message) {
        JsonObject json = (JsonObject) JsonParser.parseString(message);

        switch (json.get("op").getAsInt()) {
            case 0:
                debug("Received welcome message");
                endpoint.setHeartbeatInterval(json.get("d").getAsJsonObject().get("heartbeat").getAsLong());
                break;
            case 1:
                info("Received song information");
                Song song = new Song(json);
                SongUpdateEvent.onSongUpdate(song);
                break;
            case 10:
                debug("Received heartbeat");
                break;
            default:
                debug("Received invalid OP code! Ignoring");
                break;
        }
    }

    public void disableLogger(boolean disable) {
        disableLogging = disable;
    }

    public void enableDebug(boolean enable) {
        debug = enable;
    }
}
