package view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.util.Properties;

public class HotkeyFileHandler extends MainView {
    /**
     * Saves hotkey preferences to hotkey.properties file
     */
    void saveHotkeys(String[] hotkeyProductIds, String[] hotkeyProductNames) {
        try {
            File configFile = new File("hotkey.properties");
            Properties props = new Properties();
            for (int i = 0; i < hotkeyProductIds.length; i++) {
                props.setProperty("hotkeyId" + i, String.valueOf(hotkeyProductIds[i]));
                props.setProperty("hotkeyName" + i, String.valueOf(hotkeyProductNames[i]));
            }
            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "Hotkey settings");
            writer.close();
        } catch (FileNotFoundException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Pikanäppäimen tallennustiedostoa ei löytynyt!", ButtonType.CLOSE);
            alert.showAndWait();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Pikanäppäintallennus epäonnistui!", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    /**
     * Loads hotkey preferences from hotkey.properties file
     */
    void loadHotkeys(String[] hotkeyProductIds, String[] hotkeyProductNames) {
        try {
            File configFile = new File("hotkey.properties");
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            try {
                for (int i = 0; i < hotkeyProductIds.length; i++) {
                    hotkeyProductIds[i] = props.getProperty("hotkeyId" + i);
                    hotkeyProductNames[i] = props.getProperty("hotkeyName" + i);
                }
            } catch (Exception ignored) {
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            fileNotFoundNotification();

        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Pikanäppäinasetusten lataamisvirhe! Kokeile käynnistää sovellus uudelleen. Jos tämä ei auta, aseta pikanäppäimet uudelleen.", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }
}
