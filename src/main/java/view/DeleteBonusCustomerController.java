package view;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;

import java.util.ResourceBundle;

/**
 * Controller for delete-bonus-customer-view.fxml
 *
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 * Samu Luoma
 */
public class DeleteBonusCustomerController {
    private MainApp mainApp;
    @FXML
    private TextField bonusCustomerId;

    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
        bonusCustomerId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                bonusCustomerId.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Tries to delete BonusCustomer from database.
     * If it can't find the user, display error notification.
     */
    @FXML
    private void deleteBonusCustomer() {
        ResourceBundle bundle;
        bundle = mainApp.getBundle();
        try {
            final String text = bonusCustomerId.getText();
            this.mainApp.getEngine().customerDAO()
                    .deleteCustomer(this.mainApp.getEngine().customerDAO().getCustomer(Integer.parseInt(text)));
            Notifications.create()
                    .owner(bonusCustomerId.getScene().getWindow())
                    .title("")
                    .text(bundle.getString("bonusCustomerDeleted"))
                    .position(Pos.TOP_RIGHT)
                    .showConfirm();
        } catch (final Exception e) {
            Notifications.create()
                    .owner(bonusCustomerId.getScene().getWindow())
                    .title(bundle.getString("errorString"))
                    .text(bundle.getString("bonusCustomerError"))
                    .position(Pos.TOP_RIGHT)
                    .showError();
        }
    }
}
