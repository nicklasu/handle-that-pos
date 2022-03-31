package view;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.controlsfx.control.Notifications;

import java.io.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class HotkeyFileHandler extends MainView {

    ResourceBundle bundle;

    public HotkeyFileHandler(ResourceBundle bundle) {
        this.bundle = bundle;
    }

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
            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("fileNotFoundString"), ButtonType.CLOSE);
            alert.showAndWait();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("fileSavingErrorString"), ButtonType.CLOSE);
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
            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("fileLoadingErrorString"), ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    public void fileNotFoundNotification() {
        Notifications.create()
                .title(bundle.getString("notificationString"))
                .text(bundle.getString("fileNotFoundOnLoadString"))
                .position(Pos.CENTER)
                .show();
    }
}
