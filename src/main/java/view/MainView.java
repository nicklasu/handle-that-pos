package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainView {

    private MainApp mainApp;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Label usernameLabel;

    @FXML
    private ListView<String> scanListView;

    @FXML
    private TextField barcodeTextField;

    private ObservableList<String> items = FXCollections.observableArrayList();

    //Could try to remove (ActionEvent event) from this function and see if it still works.
    public void loadTransactionView(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("transaction-view.fxml"));
        new ViewLoader(mainAnchorPane, fxmlLoader.load());
        ((TransactionView) fxmlLoader.getController()).setMainApp(this.mainApp);
        //new ViewLoader(mainAnchorPane, FXMLLoader.load(getClass().getResource("transaction-view.fxml")));
    }

    @FXML
    private void readBarcode() {
        items.add(barcodeTextField.getText());
        barcodeTextField.clear();
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
        scanListView.setItems(items);

        //Pressing enter runs readBarcode()
        barcodeTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                readBarcode();
        });
    }
}