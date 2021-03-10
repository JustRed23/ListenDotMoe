package Endpoint;

import com.google.gson.JsonObject;
import jakarta.websocket.*;

import java.io.IOException;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import static Utils.Logger.*;

@ClientEndpoint
public class LDMEndpoint {

    private final String LDM_WEBSOCKET_ADDRESS;

    private Session session;
    private MessageHandler messageHandler;

    private Timer heartbeatInterval;

    private int reconnectAttempts = 0;

    public LDMEndpoint(String address) {
        this.LDM_WEBSOCKET_ADDRESS = address;
        connect();
    }

    @OnOpen
    public void open(Session session) {
        this.session = session;
        info("Session " + session.getId() + " started");
        reconnectAttempts = 0;
    }

    @OnMessage
    public void processMessage(String message) {
        if (messageHandler != null)
            messageHandler.handleMessage(message);
    }

    @OnClose
    public void close(Session session, CloseReason closeReason) {
        if (!closeReason.getReasonPhrase().isEmpty())
            debug("Connection closed with message: " + closeReason.getReasonPhrase());

        stopHeartbeatInterval();

        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        error("An error occurred: " + thr.getMessage());

        if (reconnectAttempts > 3) {
            error("Could not reconnect to WebSocket! Shutting down");
            close(session, new CloseReason(CloseReason.CloseCodes.TRY_AGAIN_LATER, "Failed to connect, try again later"));
            reconnectAttempts = 0;
        }

        info("Attempting to reconnect... attempt " + ++reconnectAttempts);
        connect();
    }

    public void addMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void setHeartbeatInterval(long interval) {
        heartbeatInterval = new Timer();
        heartbeatInterval.schedule(new TimerTask() {
            public void run() {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("op", 9);
                sendMessage(jsonObject.toString());
            }
        }, 0, interval);
    }

    public void stopHeartbeatInterval() {
        if (heartbeatInterval != null) {
            heartbeatInterval.cancel();
            heartbeatInterval = null;
        }
    }

    public void sendMessage(String message) {
        debug("Sent message: " + message + " to websocket");
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, URI.create(LDM_WEBSOCKET_ADDRESS));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Session getSession() {
        return session;
    }
}