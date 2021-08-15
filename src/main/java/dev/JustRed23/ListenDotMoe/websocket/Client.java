package dev.JustRed23.ListenDotMoe.websocket;

import com.google.gson.JsonObject;
import dev.JustRed23.ListenDotMoe.ListenDotMoe;
import jakarta.websocket.*;
import org.glassfish.tyrus.core.CloseReasons;
import org.glassfish.tyrus.core.HandshakeException;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.channels.UnresolvedAddressException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
public class Client {

    private static final Logger LOGGER = ListenDotMoe.getLogger();

    private ReconnectHandler reconnectHandler;
    private Session session;

    private Timer heartbeatInterval;

    private MessageHandler messageHandler;

    public CountDownLatch closeLatch = new CountDownLatch(1);

    public Client(ReconnectHandler reconnectHandler) {
        this.reconnectHandler = reconnectHandler;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        reconnectHandler.resetReconnectAttempts();
        info("Connection established with session id: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message) {
        info("Got message: " + message);
        if (messageHandler != null)
            messageHandler.handleMessage(message);
    }

    @OnClose
    public void onConnectionClosed(CloseReason reason) {
        warn("Connection closed: " + reason);

        stopHeartBeat();

        try {
            if (session != null && session.isOpen())
                session.close(reason);
        } catch (IOException e) {
            e.printStackTrace();
        }

        closeLatch.countDown();
    }

    @OnError
    public void onError(Throwable t) {
        error("An error occurred", t);

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

        CloseReason reason = new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, closePhrase);
        onConnectionClosed(reason);
    }

    public void sendMessage(String message) {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(message);
                info("Sent message: " + message);
            } else warn("Could not send message");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            session.close(CloseReasons.NORMAL_CLOSURE.getCloseReason());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                sendMessage(jsonObject.toString());
            }
        }, 0, interval);
    }

    public void stopHeartBeat() {
        if (heartbeatInterval != null) {
            heartbeatInterval.cancel();
            heartbeatInterval = null;
        }
    }

    //LOGGING
    private void info(String msg) {
        if (ListenDotMoe.isLoggingEnabled())
            LOGGER.info(msg);
    }

    private void warn(String msg) {
        LOGGER.warn(msg);
    }

    private void error(String msg, Throwable t) {
        LOGGER.error(msg, t);
    }
}
