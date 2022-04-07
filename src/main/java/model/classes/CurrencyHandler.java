package model.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class CurrencyHandler {
    private CurrencyHandler() {
    }

    public static String getCurrency() {
        final File file = new File("src/main/resources/HandleThatPos.properties");
        final Properties properties = new Properties();
        try (final FileInputStream input = new FileInputStream(file)) {
            properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            String c = properties.getProperty("currency");
            // if c is null fall back to euro
            if (c == null) {
                c = "€";
            }
            return c;

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return "€";
    }
}
