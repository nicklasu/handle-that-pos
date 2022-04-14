package view;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
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

/**
 * Represents the hardware running the software
 *
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 * Samu Luoma
 */
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
    private TextField bonusAmountTF;
    @FXML
    private Button saveBtn;
    @FXML
    private ChoiceBox<String> currencyBox;
    @FXML
    private Label currencyLabel;

    private final ObservableList<String> currencies = FXCollections.observableArrayList("$", "£", "€", "CHF", "kr", "ƒ",
            "₼", "Br", "BZ$", "$b", "KM", "Lek", "P", "лв", "؋", "R$", "៛", "¥", "₡", "kn", "₱", "Kč", "RD$", "¢", "Q",
            "L", "Ft", "₺", "Rp", "﷼", "₪", "J$", "₩", "₭", "ден", "RM", "₨", "₮", "MT", "C$", "₦", "B/.", "Gs", "S/.",
            "zł", "lei", "₽", "Дин.", "S", "R", "NT$", "฿", "TT$", "₴", "$U", "Bs", "₫", "Z$");

    public void setMainApp(final MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        currencyBox.setItems(currencies);
        currencyBox.setValue("$");
        currencyLabel.setText("$");
        currencyBox.setOnAction((currency -> {
            final int index = currencyBox.getSelectionModel().getSelectedIndex();
            final String selectedCurrency = currencies.get(index);
            currencyLabel.setText(selectedCurrency);
        }));

        final File appConfigPath = new File("src/main/resources/HandleThatPos.properties");
        final Properties properties = new Properties();
        try (final FileReader reader = new FileReader(appConfigPath, StandardCharsets.UTF_8)) {
            properties.load(reader);
            firmNameTF.setText(properties.getProperty("firmName"));
            phoneNumberTF.setText(properties.getProperty("phoneNumber"));
            businessIdTF.setText(properties.getProperty("businessId"));
            addressTF.setText(properties.getProperty("address"));
            postalCodeTF.setText(properties.getProperty("postalCode"));
            cityTF.setText(properties.getProperty("city"));
            currencyBox.setValue(properties.getProperty("currency"));
            bonusAmountTF.setText(properties.getProperty("bonusAmount"));
        } catch (final IOException e) {
            e.printStackTrace();
        }

        // postalCodeTF accepts only numbers
        postalCodeTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                postalCodeTF.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        bonusAmountTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                bonusAmountTF.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    private void updateInfo() {
        final String firmName = firmNameTF.getText();

        final String phoneNumber = phoneNumberTF.getText();

        final String businessId = businessIdTF.getText();

        final String address = addressTF.getText();

        final String postalCode = postalCodeTF.getText();

        final String city = cityTF.getText();

        final String currency = currencyBox.getValue();

        final String bonusAmount = bonusAmountTF.getText();

        final File appConfigPath = new File("src/main/resources/HandleThatPos.properties");
        final Properties properties = new Properties();
        try (final FileReader reader = new FileReader(appConfigPath, StandardCharsets.UTF_8)) {
            properties.load(reader);
            try (final FileWriter writer = new FileWriter(appConfigPath, StandardCharsets.UTF_8)) {
                properties.setProperty("firmName", firmName);
                properties.setProperty("phoneNumber", phoneNumber);
                properties.setProperty("businessId", businessId);
                properties.setProperty("address", address);
                properties.setProperty("postalCode", postalCode);
                properties.setProperty("city", city);
                properties.setProperty("currency", currency);
                if (Objects.equals(bonusAmountTF.getText(), "")) {
                    properties.setProperty("bonusAmount", bonusAmount);
                } else {
                    properties.setProperty("bonusAmount", "0");
                }
                properties.store(writer, "HandleThatPos settings");
            }
            Notifications.create().owner(this.firmNameTF.getScene().getWindow()).position(Pos.TOP_RIGHT)
                    .title(this.mainApp.getBundle().getString("success"))
                    .text(this.mainApp.getBundle().getString("firmInfoUpdated")).showInformation();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
