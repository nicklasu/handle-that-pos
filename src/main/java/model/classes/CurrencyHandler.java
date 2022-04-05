package model.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class CurrencyHandler {

    public static String getCurrency() {
        File file = new File("src/main/resources/HandleThatPos.properties");
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream(file);
            properties.load(new InputStreamReader(input, Charset.forName("UTF-8")));
            String c = properties.getProperty("currency");
            //if c is null fall back to euro
            if(c == null) {
                c = "€";
            }
            return c;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "€";
    }
}
