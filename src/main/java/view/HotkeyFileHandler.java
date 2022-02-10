package view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.util.Properties;

public class HotkeyFileHandler extends MainView {
    /**
     * Saves hotkey preferences to hotkey.properties file
     *
     * @param hotkeyProductIds
     */
    void saveHotkeys(String hotkeyProductIds[]) {
        try {
            File configFile = new File("hotkey.properties");
            Properties props = new Properties();
            for (int i = 0; i < hotkeyProductIds.length; i++) {
                props.setProperty("hotkey" + i, String.valueOf(hotkeyProductIds[i]));
            }
            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "Hotkey settings");
            writer.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pikanäppäimen tallennus onnistui!", ButtonType.CLOSE);
            alert.showAndWait();
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
     *
     * @param hotkeyProductIds
     */
    void loadHotkeys(String hotkeyProductIds[]) {
        try {
            File configFile = new File("hotkey.properties");
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            try {
                for (int i = 0; i < hotkeyProductIds.length; i++) {
                    hotkeyProductIds[i] = props.getProperty("hotkey" + i);
                }
            } catch (Exception e) {

            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pikanäppäinasetusten tiedostoa ei löytynyt. Jos haluat asettaa pikanäppäimen, aloita skannaamalla haluamasi tuote. Tämän jälkeen pidä jotakin pikanäppäintä pohjassa yli 2 sekuntia.", ButtonType.CLOSE);
            alert.showAndWait();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Pikanäppäinasetusten lataamisvirhe! Kokeile käynnistää sovellus uudelleen. Jos tämä ei auta, aseta pikanäppäimet uudelleen.", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }
}
