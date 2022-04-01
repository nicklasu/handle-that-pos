package view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

public class LoginView {
    //Prefills the username field and password field with the manager's username and password
    private boolean DEV_MODE = true;

    private MainApp mainApp;

    @FXML
    private TextField usernameTextField;
    @FXML
    private Label devLabel;
    @FXML
    private PasswordField passwordPasswordField;

    public void setMainApp(MainApp mainApp) {
        devLabel.setVisible(DEV_MODE);
        //Prefills the username field and password field with the manager's username and password
        if(DEV_MODE) {
            usernameTextField.setText("testuser");
            passwordPasswordField.setText("123");
        }

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
        if (this.mainApp.getEngine().login(usernameTextField.getText(), passwordPasswordField.getText()) == 1) {
            this.mainApp.showMainView();
        } else if (this.mainApp.getEngine().login(usernameTextField.getText(), passwordPasswordField.getText()) == 0) {
            // alert, wrong username or password
            Alert alert = new Alert(Alert.AlertType.ERROR, "Väärä käyttäjänimi tai salasana!", ButtonType.OK);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }

        } else if (this.mainApp.getEngine().login(usernameTextField.getText(), passwordPasswordField.getText()) == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ei käyttöoikeuksia! Ota yhteyttä myymäläpäällikköön.", ButtonType.OK);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        } else {
            System.out.println("Muu virhe");
        }
    }

}



