package view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Button loginButton;

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
        progressIndicator.setVisible(true);
        progressIndicator.setViewOrder(2);
        loginButton.setDisable(true);
        loginButton.textProperty().bind(Bindings.when(progressIndicator.visibleProperty()).then(this.mainApp.getBundle().getString("logging_in")).otherwise(this.mainApp.getBundle().getString("login")));
        Thread thread = new Thread(() -> {
            try {
                int result = this.mainApp.getEngine().login(usernameTextField.getText(), passwordPasswordField.getText());
                Platform.runLater(() -> {
                    if (result == 1) {
                        this.mainApp.showMainView();
                    } else if (result == 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, this.mainApp.getBundle().getString("loginError1"), ButtonType.OK);
                        alert.showAndWait();

                        if (alert.getResult() == ButtonType.OK) {
                            alert.close();
                        }
                    } else if (result == 2) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, this.mainApp.getBundle().getString("loginError2"), ButtonType.OK);
                        alert.showAndWait();

                        if (alert.getResult() == ButtonType.OK) {
                            alert.close();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, this.mainApp.getBundle().getString("loginError3"), ButtonType.OK);
                        alert.showAndWait();

                        if (alert.getResult() == ButtonType.OK) {
                            alert.close();
                        }
                    }
                    progressIndicator.setVisible(false);
                    loginButton.setDisable(false);
                    loginButton.textProperty().unbind();

                });
            } catch(Exception e){
                    e.printStackTrace();
                }
            });
        thread.start();

    }

}



