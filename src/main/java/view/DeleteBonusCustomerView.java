package view;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;

import java.io.IOException;

public class DeleteBonusCustomerView {
    private MainApp mainApp;

    @FXML
    private TextField bonusCustomerId;

    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        bonusCustomerId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                bonusCustomerId.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    private void deleteBonusCustomer() {
        try {
            String text = bonusCustomerId.getText();
            this.mainApp.getEngine().customerDAO().deleteCustomer(this.mainApp.getEngine().customerDAO().getCustomer(Integer.parseInt(text)));
            Notifications.create()
                    .owner(bonusCustomerId.getScene().getWindow())
                    .title("")
                    .text("Bonusasiakas poistettu.")
                    .position(Pos.TOP_RIGHT)
                    .showConfirm();
        } catch (Exception e) {
            Notifications.create()
                    .owner(bonusCustomerId.getScene().getWindow())
                    .title("Virhe")
                    .text("Bonusasiakasta ei löydy!")
                    .position(Pos.TOP_RIGHT)
                    .showError();
        }
    }
}
