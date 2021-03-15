package dev.JustRed23.ListenDotMoe.Utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import static org.apache.logging.log4j.Level.*;

public class Logger {

    public static boolean debug;
    public static boolean disableLogging;

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(Logger.class.getName());

    public static void log(Level logLevel, Object message) {
        if (disableLogging) return;
        LOGGER.log(logLevel, message);
    }

    private static void log(Level logLevel, Object message, Throwable t) {
        log(logLevel, message);
        logStackTrace(t);
    }

    public static void logStackTrace(Throwable t) {
        if (t == null)
            return;
        t.printStackTrace();
    }

    public static void info(Object message) {
        log(INFO, message);
    }

    public static void warn(Object message) {
        log(WARN, message);
    }

    public static void error(Object message) {
        error(message, null);
    }

    public static void error(Object message, Throwable t) {
        log(ERROR, message, t);
    }

    public static void fatal(Object message) {
        fatal(message, null);
    }

    public static void fatal(Object message, Throwable t) {
        log(FATAL, message, t);
    }

    public static void debug(Object message) {
        if (debug) log(DEBUG, message);
    }
}
