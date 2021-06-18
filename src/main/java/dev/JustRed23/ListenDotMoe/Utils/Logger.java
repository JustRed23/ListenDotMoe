package dev.JustRed23.ListenDotMoe.Utils;

import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import static org.slf4j.event.Level.*;

public class Logger {

    public static boolean debug;
    public static boolean disableLogging;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Logger.class);

    public static void info(String message) {
        if (!disableLogging)
            LOGGER.info(message);
    }

    public static void warn(String message) {
        if (!disableLogging)
            LOGGER.warn(message);
    }

    public static void error(String message) {
        if (!disableLogging)
            LOGGER.error(message);
    }

    public static void error(String message, Throwable t) {
        if (!disableLogging)
            LOGGER.error(message, t);
    }

    public static void debug(String message) {
        if (debug && !disableLogging)
            LOGGER.debug(message);
    }
}
