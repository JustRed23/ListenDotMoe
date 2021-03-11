package dev.JustRed23.ListenDotMoe.Utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import static org.apache.logging.log4j.Level.*;

public class Logger {

    public static boolean disableLogging = false;
    public static boolean debug = false;

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger("dev.JustRed23.ListenDotMoe.ListenDotMoe");

    public static void log(Level level, String message) {
        LOGGER.log(level, message);
    }

    public static void info(String message) {
        log(INFO, message);
    }

    public static void warn(String message) {
        log(WARN, message);
    }

    public static void error(String message) {
        log(ERROR, message);
    }

    public static void debug(String message) {
        if (debug) log(DEBUG, message);
    }
}
