package util;
import java.io.*;
import java.util.Properties;


public class ConfigReader {
    private static final String CONFIG_FILE = "config.properties";
    private static Properties props = new Properties();

    static {
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(CONFIG_FILE))) {
            props.load(reader);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
