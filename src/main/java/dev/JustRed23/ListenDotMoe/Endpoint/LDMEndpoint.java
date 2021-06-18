package dev.JustRed23.ListenDotMoe.Endpoint;

import com.google.gson.JsonObject;
import jakarta.websocket.*;
import org.glassfish.tyrus.core.HandshakeException;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.UnresolvedAddressException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@ClientEndpoint
public class LDMEndpoint {

    private final String LDM_WEBSOCKET_ADDRESS;

    private Session session;
    private MessageHandler messageHandler;
    public CountDownLatch closeLatch;

    private CloseReason close;

    private boolean onError;

    private Timer heartbeatInterval;

    private static int reconnectAttempts = 0;
    private static final int MAX_RECONNECT_ATTEMPTS = 3;

    public LDMEndpoint(String address) {
        this.LDM_WEBSOCKET_ADDRESS = address;
        closeLatch = new CountDownLatch(1);
        connect();
    }

    @OnOpen
    public void open(Session session) {
        this.session = session;
        reconnectAttempts = 0;
        onError = false;
    }

    @OnMessage
    public void processMessage(String message) {
        assert messageHandler != null;
        messageHandler.handleMessage(message);
    }

    @OnClose
    public void close(CloseReason closeReason) {
        this.close = closeReason;

        Session session = getSession();

        stopHeartbeatInterval();

        try {
            if (session != null && session.isOpen())
                session.close(closeReason);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (onError) {
            reconnectAttempts++;
            if (reconnectAttempts > MAX_RECONNECT_ATTEMPTS) {
                reconnectAttempts = 0;
                closeLatch.countDown();
            } else {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                connect();
            }
        } else
            closeLatch.countDown();
    }

    @OnError
    public void onError(Throwable t) {
        onError = true;

        String closePhrase;

        try {
            throw (t instanceof DeploymentException ? t.getCause() : t);
        } catch (UnresolvedAddressException unresolvedAddressException) {
            closePhrase = "Could not find websocket address. Make sure you are connected to the internet";
        } catch (HandshakeException handshakeException) {
            closePhrase = handshakeException.getMessage();
        } catch (Throwable e) {
            closePhrase = e.getMessage();
            e.printStackTrace();
        }

        CloseReason reason = new CloseReason(CloseReason.CloseCodes.NO_STATUS_CODE, closePhrase);
        close(reason);
    }

    public void addMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void setHeartbeatInterval(long interval) {
        heartbeatInterval = new Timer("API-KeepAlive");
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
        Session session = getSession();
        try {
            if (session != null && session.isOpen())
                session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, URI.create(LDM_WEBSOCKET_ADDRESS));
        } catch (Exception e) {
            onError(e);
        }
    }

    public Session getSession() {
        return session;
    }

    public CloseReason getCloseReason() {
        return close;
    }
}