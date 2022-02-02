package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainView {

    private MainApp mainApp;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Label usernameLabel;

    public void loadTransactionView(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("transaction-view.fxml"));
        new ViewLoader(mainAnchorPane, fxmlLoader.load());
        ((TransactionView)fxmlLoader.getController()).setMainApp(this.mainApp);
        //new ViewLoader(mainAnchorPane, FXMLLoader.load(getClass().getResource("transaction-view.fxml")));
    }

    @FXML
    private void handleLogoutButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Haluatko varmasti kirjautua ulos?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            this.mainApp.getEngine().logout();
            this.mainApp.showLoginView();
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.usernameLabel.setText(mainApp.getEngine().getUser().getUsername());
    }
}