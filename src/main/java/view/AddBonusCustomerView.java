package view;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.classes.Customer;

import java.io.IOException;

public class AddBonusCustomerView {
    private MainApp mainApp;

    @FXML
    private TextField bonusCustomerId;
    @FXML
    private Button addBonusBtn;

    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        BooleanBinding booleanBind = bonusCustomerId.textProperty().isEmpty();
        addBonusBtn.disableProperty().bind(booleanBind);
        bonusCustomerId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    bonusCustomerId.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    private void addBonusCustomer() {
        System.out.println("adding bonus customer");
        int customerId = Integer.parseInt(bonusCustomerId.getText());
        Customer c = new Customer(1);
        c.setId(customerId);
        this.mainApp.getEngine().customerDAO().addCustomer(c);
    }
}
