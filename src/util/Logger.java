package util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = ConfigReader.get("log.file");
    private static final DateTimeFormatter fmt =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static synchronized void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(fmt);
        String entry = timestamp + " [" + level + "] " + message;
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Logger error: " + e.getMessage());
        }
    }
}
