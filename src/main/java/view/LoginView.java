package view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class LoginView {

    private MainApp mainApp;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordPasswordField;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        passwordPasswordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                handleLoginButton();
        });
        usernameTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                handleLoginButton();
        });
    }

    @FXML
    private void handleLoginButton() {
        if (this.mainApp.getEngine().login(usernameTextField.getText(), passwordPasswordField.getText())) {
            this.mainApp.showMainView();
        } else {
            // alert, wrong username or password
            Alert alert = new Alert(Alert.AlertType.ERROR, "Väärä käyttäjänimi tai salasana!", ButtonType.OK);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
    }

}



