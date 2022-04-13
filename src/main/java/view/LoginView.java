package view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import model.classes.LoginStatus;

/**
 * Represents the hardware running the software
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class LoginView {
    // Prefills the username field and password field with the manager's username
    // and password
    private final boolean DEV_MODE = true;

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

    public void setMainApp(final MainApp mainApp) {
        devLabel.setVisible(DEV_MODE);
        // Prefills the username field and password field with the manager's username
        // and password
        if (DEV_MODE) {
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
        loginButton.textProperty()
                .bind(Bindings.when(progressIndicator.visibleProperty())
                        .then(this.mainApp.getBundle().getString("logging_in"))
                        .otherwise(this.mainApp.getBundle().getString("login")));
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "default error", ButtonType.OK);
        alert.setTitle(this.mainApp.getBundle().getString("errorString"));
        alert.setHeaderText(this.mainApp.getBundle().getString("loginError"));
        loginHandlerThread(alert);

    }

    private void loginHandlerThread(final Alert alert) {
        final Thread thread = new Thread(() -> {
            try {
                final LoginStatus result = this.mainApp.getEngine().login(usernameTextField.getText(),
                        passwordPasswordField.getText());
                Platform.runLater(() -> {

                    switch (result) {
                        case SUCCESS:
                            this.mainApp.showMainView();
                            break;
                        case WRONG_CREDENTIALS:
                            alertMsg(alert, "loginError1");
                            break;
                        case NO_PRIVILEGES:
                            alertMsg(alert, "loginError2");
                            break;
                        default:
                            alertMsg(alert, "loginError3");
                            break;
                    }
                    progressIndicator.setVisible(false);
                    loginButton.setDisable(false);
                    loginButton.textProperty().unbind();

                });
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void alertMsg(final Alert alert, String msg) {
        alert.setContentText(this.mainApp.getBundle().getString(msg));
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

}
