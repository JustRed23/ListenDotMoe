package dev.JustRed23.ListenDotMoe.Endpoint;

import com.google.gson.JsonObject;
import dev.JustRed23.ListenDotMoe.ListenDotMoe;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Client extends WebSocketClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private boolean onError;

    private Timer heartbeatInterval;

    private MessageHandler messageHandler;
    public CountDownLatch closeLatch;

    private String closeReason;

    private static int reconnectAttempts = 0;
    private static final int MAX_RECONNECT_ATTEMPTS = 3;

    public Client(URI serverUri, Draft draft) {
        super(serverUri, draft);
        closeLatch = new CountDownLatch(1);
    }

    public Client(URI serverURI) {
        super(serverURI);
        closeLatch = new CountDownLatch(1);
    }

    public Client(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
        closeLatch = new CountDownLatch(1);
    }

    public void onOpen(ServerHandshake handshakeData) {
        info("Opened connection with status code " + handshakeData.getHttpStatus() + " (" + handshakeData.getHttpStatusMessage() + ")");
        reconnectAttempts = 0;
        onError = false;
    }

    public void onMessage(String message) {
        info("Got message: " + message);
        if (messageHandler != null)
            messageHandler.handleMessage(message);
    }

    public void onClose(int code, String reason, boolean remote) {
        closeReason = reason;
        warn("Closing connection with code " + code + " (" + reason + ")");
        stopHeartBeat();
        closeLatch.countDown();
    }

    public void onError(Exception ex) {
        onError = true;
        error("An error occurred", ex);
    }

    public void addMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void setHeartbeat(long interval) {
        heartbeatInterval = new Timer("API-KeepAlive");
        heartbeatInterval.schedule(new TimerTask() {
            public void run() {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("op", 9);
                send(jsonObject.toString());
            }
        }, 0, interval);
    }

    public void stopHeartBeat() {
        if (heartbeatInterval != null) {
            heartbeatInterval.cancel();
            heartbeatInterval = null;
        }
    }

    public String getCloseReason() {
        return closeReason;
    }

    //LOGGING
    private void info(String msg) {
        if (ListenDotMoe.isLoggingEnabled())
            LOGGER.info(msg);
    }

    private void warn(String msg) {
        if (ListenDotMoe.isLoggingEnabled())
            LOGGER.warn(msg);
    }

    private void error(String msg, Throwable t) {
        if (ListenDotMoe.isLoggingEnabled())
            LOGGER.error(msg, t);
    }
}
