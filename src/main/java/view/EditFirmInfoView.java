package view;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditFirmInfoView {
    private MainApp mainApp;
    @FXML
    private TextField firmNameTF;
    @FXML
    private TextField phoneNumberTF;
    @FXML
    private TextField addressTF;
    @FXML
    private Button saveBtn;
    @FXML
    private ChoiceBox<String> currencyBox;
    @FXML
    private Label currencyLabel;

    private ObservableList<String> currencies = FXCollections.observableArrayList("$", "£", "€", "CHF", "kr","ƒ", "₼", "Br", "BZ$", "$b", "KM", "Lek", "P", "лв","؋", "R$", "៛", "¥", "₡", "kn", "₱", "Kč",  "RD$","¢", "Q", "L", "Ft", "₺", "Rp", "﷼", "₪", "J$", "₩", "₭", "ден", "RM", "₨", "₮", "MT", "C$", "₦", "B/.", "Gs", "S/.", "zł", "lei", "₽", "Дин.", "S", "R",  "NT$", "฿", "TT$", "₴", "$U", "Bs", "₫", "Z$");

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
    }

    @FXML
    private void editInfo() {
        String name = firmNameTF.getText();
        String phonenumber = phoneNumberTF.getText();
        String address = addressTF.getText();
    }

    @FXML
    private void updateInfo() {
        firmNameTF.clear();
        phoneNumberTF.clear();
        addressTF.clear();
    }
}
