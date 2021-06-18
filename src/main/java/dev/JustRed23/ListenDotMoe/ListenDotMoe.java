package dev.JustRed23.ListenDotMoe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.JustRed23.ListenDotMoe.Endpoint.LDMEndpoint;
import dev.JustRed23.ListenDotMoe.Music.Song;
import dev.JustRed23.ListenDotMoe.Music.SongUpdateEvent;
import jakarta.websocket.CloseReason;
import org.fusesource.jansi.AnsiConsole;

public class ListenDotMoe {

    public static final double VERSION = 1.4;

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
            endpoint = new LDMEndpoint("wss://listen.moe/gateway_v2");
            endpoint.addMessageHandler(ListenDotMoe::processMessage);

            try {
                endpoint.closeLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "ListenDotMoe");
        thread.start();
    }

    public void stop() {
        if (!running) return;
        running = false;
        endpoint.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Closing client"));
        AnsiConsole.systemUninstall();
    }

    private static synchronized void processMessage(String message) {
        JsonObject json = (JsonObject) JsonParser.parseString(message);

        switch (json.get("op").getAsInt()) {
            case 0:
                endpoint.setHeartbeatInterval(json.get("d").getAsJsonObject().get("heartbeat").getAsLong());
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
}
