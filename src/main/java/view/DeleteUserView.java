package view;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;

/**
 * Represents the hardware running the software
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class DeleteUserView {
    private MainApp mainApp;
    @FXML
    private Button saveBtn;
    @FXML
    private TextField userName;

    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
        final BooleanBinding booleanBind = userName.textProperty().isEmpty();
        saveBtn.disableProperty().bind(booleanBind);
    }

    @FXML
    private void deleteUser() {
        try {
            final String username = userName.getText();
            if (username.equals(this.mainApp.getEngine().getUser().getUsername())) {
                Notifications.create()
                        .owner(userName.getScene().getWindow())
                        .title(this.mainApp.getBundle().getString("errorString"))
                        .text(this.mainApp.getBundle().getString("deleteUserError2"))
                        .position(Pos.TOP_RIGHT)
                        .showError();
            } else {
                this.mainApp.getEngine().userDAO().deleteUser(mainApp.getEngine().userDAO().getUser(username));
                Notifications.create()
                        .owner(userName.getScene().getWindow())
                        .title(this.mainApp.getBundle().getString("success"))
                        .text(this.mainApp.getBundle().getString("deleteUserSuccess"))
                        .position(Pos.TOP_RIGHT)
                        .show();
            }
        } catch (final Exception e) {
            Notifications.create()
                    .owner(userName.getScene().getWindow())
                    .title(this.mainApp.getBundle().getString("errorString"))
                    .text(this.mainApp.getBundle().getString("deleteUserError"))
                    .position(Pos.TOP_RIGHT)
                    .showError();
        }
    }
}
