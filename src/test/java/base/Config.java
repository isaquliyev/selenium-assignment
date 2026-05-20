package base;

import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final Properties PROPS = load();

    private Config() {}

    private static Properties load() {
        Properties p = new Properties();
        try (InputStream in = Config.class.getClassLoader()
                .getResourceAsStream("test.properties")) {
            if (in != null) p.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load test.properties", e);
        }
        return p;
    }

    public static String seleniumHubUrl() { return PROPS.getProperty("selenium.hub.url"); }
    public static String baseUrl()        { return PROPS.getProperty("base.url"); }
}
