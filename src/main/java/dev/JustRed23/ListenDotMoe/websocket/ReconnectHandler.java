package dev.JustRed23.ListenDotMoe.websocket;

import dev.JustRed23.ListenDotMoe.ListenDotMoe;
import jakarta.websocket.CloseReason;
import org.glassfish.tyrus.client.ClientManager;

import java.util.concurrent.TimeUnit;

public class ReconnectHandler extends ClientManager.ReconnectHandler {

    private ListenDotMoe listenDotMoe;

    private static final int MAX_RECONNECT_ATTEMPTS = 3;
    private int counter = 0;

    public ReconnectHandler(ListenDotMoe listenDotMoe) {
        this.listenDotMoe = listenDotMoe;
    }

    public boolean onDisconnect(CloseReason closeReason) {
        if (!listenDotMoe.isRunning())
            return false;

        counter++;

        if (counter <= MAX_RECONNECT_ATTEMPTS) {
            System.out.println("Connection failed. Reconnecting... (Attempt " + counter + " of " + MAX_RECONNECT_ATTEMPTS + ")");
            return true;
        } else {
            listenDotMoe.stop();
            return false;
        }
    }

    public boolean onConnectFailure(Exception exception) {
        counter++;

        if (counter <= MAX_RECONNECT_ATTEMPTS) {
            System.out.println("Connection failed. Reconnecting... (Attempt " + counter + " of " + MAX_RECONNECT_ATTEMPTS + ")");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            listenDotMoe.stop();
            return false;
        }
    }

    public long getDelay() {
        return 1;
    }

    public void resetReconnectAttempts() {
        counter = 0;
    }
}
