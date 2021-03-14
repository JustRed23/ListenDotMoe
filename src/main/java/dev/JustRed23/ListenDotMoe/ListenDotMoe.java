package dev.JustRed23.ListenDotMoe;

import com.google.gson.*;
import dev.JustRed23.ListenDotMoe.Endpoint.LDMEndpoint;
import dev.JustRed23.ListenDotMoe.Music.Song;
import dev.JustRed23.ListenDotMoe.Music.SongUpdateEvent;
import jakarta.websocket.CloseReason;
import org.fusesource.jansi.AnsiConsole;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static dev.JustRed23.ListenDotMoe.Utils.Logger.*;

public class ListenDotMoe implements Runnable {

    private static LDMEndpoint endpoint;
    private static String message = "";
    private static final Gson gson = new Gson();

    private static final String LDM_ALBUM_ENDPOINT = "https://cdn.listen.moe/covers/";
    private static final String LDM_ARTISTS_ENDPOINT = "https://cdn.listen.moe/artists/";

    private int heartbeats = 0;

    public void start(String[] args) {
        AnsiConsole.systemInstall();

        init(Arrays.asList(args).contains("debug"));

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
        info("Starting ListenDotMoe");
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
                break;
            case 10:
                debug("Received heartbeat");
                heartbeats++;
                if (heartbeats >= 3) stop();
                break;
            default:
                debug("Received invalid OP code! Ignoring");
                break;
        }
    }

    public void disableLogger(boolean disable) {
        disableLogging = disable;
    }
}
