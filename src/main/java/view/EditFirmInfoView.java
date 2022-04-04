package view;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;

public class EditFirmInfoView {
    private MainApp mainApp;
    @FXML
    private TextField firmNameTF;
    @FXML
    private TextField phoneNumberTF;
    @FXML
    private TextField businessIdTF;
    @FXML
    private TextField addressTF;
    @FXML
    private TextField postalCodeTF;
    @FXML
    private TextField cityTF;
    @FXML
    private Button saveBtn;
    @FXML
    private ChoiceBox<String> currencyBox;
    @FXML
    private Label currencyLabel;

    private ObservableList<String> currencies = FXCollections.observableArrayList("$", "£", "€", "CHF", "kr", "ƒ", "₼", "Br", "BZ$", "$b", "KM", "Lek", "P", "лв", "؋", "R$", "៛", "¥", "₡", "kn", "₱", "Kč", "RD$", "¢", "Q", "L", "Ft", "₺", "Rp", "﷼", "₪", "J$", "₩", "₭", "ден", "RM", "₨", "₮", "MT", "C$", "₦", "B/.", "Gs", "S/.", "zł", "lei", "₽", "Дин.", "S", "R", "NT$", "฿", "TT$", "₴", "$U", "Bs", "₫", "Z$");

    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        currencyBox.setItems(currencies);
        currencyBox.setValue("$");
        currencyLabel.setText("$");
        currencyBox.setOnAction((currency -> {
            int index = currencyBox.getSelectionModel().getSelectedIndex();
            String selectedCurrency = currencies.get(index);
            currencyLabel.setText(selectedCurrency);
        }));

        File appConfigPath = new File("src/main/resources/HandleThatPos.properties");
        Properties properties = new Properties();
        try {
            FileReader reader = new FileReader(appConfigPath, StandardCharsets.UTF_8);
            properties.load(reader);
            firmNameTF.setText(properties.getProperty("firmName"));
            phoneNumberTF.setText(properties.getProperty("phoneNumber"));
            businessIdTF.setText(properties.getProperty("businessId"));
            addressTF.setText(properties.getProperty("address"));
            postalCodeTF.setText(properties.getProperty("postalCode"));
            cityTF.setText(properties.getProperty("city"));
            currencyBox.setValue(properties.getProperty("currency"));
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //postalCodeTF accepts only numbers
        postalCodeTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                postalCodeTF.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

    }

    @FXML
    private void editInfo() {
        String name = firmNameTF.getText();
        String phonenumber = phoneNumberTF.getText();
        String address = addressTF.getText();
    }

    @FXML
    private void updateInfo() {
        String firmName = firmNameTF.getText();

        String phoneNumber = phoneNumberTF.getText();

        String businessId = businessIdTF.getText();

        String address = addressTF.getText();

        String postalCode = postalCodeTF.getText();

        String city = cityTF.getText();

        String currency = currencyBox.getValue();



        try {
            File appConfigPath = new File("src/main/resources/HandleThatPos.properties");
            Properties properties = new Properties();
            properties.load(new FileReader(appConfigPath));
            FileWriter writer = new FileWriter(appConfigPath, StandardCharsets.UTF_8);
            properties.setProperty("firmName", firmName);
            properties.setProperty("phoneNumber", phoneNumber);
            properties.setProperty("businessId", businessId);
            properties.setProperty("address", address);
            properties.setProperty("postalCode", postalCode);
            properties.setProperty("city", city);
            properties.setProperty("currency", currency);
            properties.store(writer, "HandleThatPos settings");
            writer.close();
            Notifications.create().owner(this.firmNameTF.getScene().getWindow()).position(Pos.TOP_RIGHT).title(this.mainApp.getBundle().getString("success")).text(this.mainApp.getBundle().getString("firmInfoUpdated")).showInformation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
