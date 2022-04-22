package view;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.controlsfx.control.Notifications;

import java.io.*;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Handles the hotkey file (hotkey.properties).
 * Saves and loads hotkey information from the file.
 * Called from MainViewController.
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class HotkeyFileHandler extends MainViewController {

    ResourceBundle bundle;

    public HotkeyFileHandler(final ResourceBundle bundle) {
        this.bundle = bundle;
    }

    /**
     * Saves hotkey preferences to hotkey.properties file
     */
    void saveHotkeys(final String[] hotkeyProductIds, final String[] hotkeyProductNames) {
        try {
            final File configFile = new File("src/main/resources/hotkey.properties");
            final Properties props = new Properties();
            for (int i = 0; i < hotkeyProductIds.length; i++) {
                props.setProperty("hotkeyId" + i, String.valueOf(hotkeyProductIds[i]));
                props.setProperty("hotkeyName" + i, String.valueOf(hotkeyProductNames[i]));
            }
            try (final FileWriter writer = new FileWriter(configFile)) {
                props.store(writer, "Hotkey settings");
            }

        } catch (final FileNotFoundException ex) {
            final Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("fileNotFoundString"),
                    ButtonType.CLOSE);
            alert.showAndWait();
        } catch (final IOException ex) {
            final Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("fileSavingErrorString"),
                    ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    /**
     * Loads hotkey preferences from hotkey.properties file
     */
    void loadHotkeys(final String[] hotkeyProductIds, final String[] hotkeyProductNames) {
        final File configFile = new File("src/main/resources/hotkey.properties");
        try (final FileReader reader = new FileReader(configFile)) {
            final Properties props = new Properties();
            props.load(reader);
            try {
                for (int i = 0; i < hotkeyProductIds.length; i++) {
                    hotkeyProductIds[i] = props.getProperty("hotkeyId" + i);
                    hotkeyProductNames[i] = props.getProperty("hotkeyName" + i);
                }
            } catch (final Exception b) {
                b.printStackTrace();
            }
        } catch (final FileNotFoundException ex) {
            fileNotFoundNotification();

        } catch (final IOException ex) {
            final Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("fileLoadingErrorString"),
                    ButtonType.CLOSE);
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
