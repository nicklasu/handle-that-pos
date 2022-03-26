package view;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.classes.Customer;

import java.io.IOException;

public class AddBonusCustomerView {
    private MainApp mainApp;

    @FXML
    private TextField bonusCustomerId;
    @FXML
    private Button addBonusBtn;
    @FXML
    private Label bonusCustomerLabel;

    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        bonusCustomerId.setVisible(false);
        bonusCustomerLabel.setVisible(false);
        bonusCustomerId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                bonusCustomerId.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    private void addBonusCustomer() {
        //int customerId = Integer.parseInt(bonusCustomerId.getText());
        Customer c = new Customer(1);
        // c.setId(customerId);
        this.mainApp.getEngine().customerDAO().addCustomer(c);
        bonusCustomerId.setText(String.valueOf(this.mainApp.getEngine().customerDAO().getCustomer(c.getId()).getId()));
        bonusCustomerId.setVisible(true);
        bonusCustomerLabel.setVisible(true);
        addBonusBtn.disableProperty();

    }
}
