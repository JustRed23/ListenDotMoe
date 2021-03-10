package Utils;

import static Utils.LogLevel.*;

public class Logger {

    public static boolean disableLogging = false;
    public static boolean debug = false;

    public static void log(LogLevel level, String message) {
        if (!disableLogging) System.out.printf("[%s] [%s] %s%n", "ListenDotMoe", level.name(), message);
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
