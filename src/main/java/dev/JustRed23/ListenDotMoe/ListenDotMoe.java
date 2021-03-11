package dev.JustRed23.ListenDotMoe;

import dev.JustRed23.ListenDotMoe.Endpoint.LDMEndpoint;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.websocket.CloseReason;

import static dev.JustRed23.ListenDotMoe.Utils.Logger.*;

public class ListenDotMoe implements Runnable {

    private static LDMEndpoint endpoint;
    private static String message = "";
    private static final Gson gson = new Gson();

    private static final String LDM_ALBUM_ENDPOINT = "https://cdn.listen.moe/covers/";
    private static final String LDM_ARTISTS_ENDPOINT = "https://cdn.listen.moe/artists/";

    public void start() {
        endpoint = new LDMEndpoint("wss://listen.moe/gateway_v2");

        ListenDotMoe listenDotMoe = new ListenDotMoe();

        Thread thread = new Thread(listenDotMoe);
        thread.start();
    }

    public void stop() {
        info("Closing client");
        endpoint.close(endpoint.getSession(), new CloseReason(CloseReason.CloseCodes.NO_STATUS_CODE, "Closing client"));
    }

    public void run() {
        info("Starting dev.JustRed23.ListenDotMoe.ListenDotMoe");
        endpoint.addMessageHandler(message -> ListenDotMoe.message = message);

        while (endpoint.getSession() != null && endpoint.getSession().isOpen()) {
            if (!message.equals("")) {
                JsonObject json = (JsonObject) JsonParser.parseString(message);

                switch (json.get("op").getAsInt()) {
                    case 0:
                        debug("Received welcome message");
                        endpoint.setHeartbeatInterval(json.get("d").getAsJsonObject().get("heartbeat").getAsLong());
                        break;
                    case 1:
                        debug("Received song information");
                        debug(json.toString());

                        break;
                    case 10:
                        debug("Received heartbeat");
                        break;
                    default:
                        debug("Received invalid OP code! Ignoring");
                        break;
                }

                message = "";
            }
        }

        info("Closed client");
    }

    public void disableLogger(boolean disable) {
        disableLogging = disable;
    }
}
