package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.classes.Customer;

/**
 * Controller for add-bonus-customer-view.fxml.
 *
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 * Samu Luoma
 */
public class AddBonusCustomerController {
    private MainApp mainApp;
    @FXML
    private TextField bonusCustomerId;
    @FXML
    private Button addBonusBtn;
    @FXML
    private Label bonusCustomerLabel;

    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
        bonusCustomerId.setVisible(false);
        bonusCustomerLabel.setVisible(false);
        bonusCustomerId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                bonusCustomerId.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Adds bonus customer to database.
     */
    @FXML
    private void addBonusCustomer() {
        final Customer c = new Customer(1);
        this.mainApp.getEngine().customerDAO().addCustomer(c);
        bonusCustomerId.setText(String.valueOf(this.mainApp.getEngine().customerDAO().getCustomer(c.getId()).getId()));
        bonusCustomerId.setVisible(true);
        bonusCustomerLabel.setVisible(true);
        addBonusBtn.disableProperty();
    }
}
