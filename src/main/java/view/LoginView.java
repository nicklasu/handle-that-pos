package view;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginView {

    private MainApp mainApp;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordPasswordField;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLoginButton() {
        if (this.mainApp.getEngine().login(usernameTextField.getText(), passwordPasswordField.getText())) {
            this.mainApp.showMainView();
        } else {
            // alert, wrong username or password
        }
    }

}
