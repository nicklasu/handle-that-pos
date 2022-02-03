package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainGUIController {
    @FXML
    private AnchorPane mainAnchorPane;
    public void loadTransactionView(ActionEvent event) throws IOException {
        new ViewLoader(mainAnchorPane, FXMLLoader.load(getClass().getResource("transaction-view.fxml")));
    }
    public void loadOptionsView(ActionEvent event) throws IOException {
        new ViewLoader(mainAnchorPane, FXMLLoader.load(getClass().getResource("options-view.fxml")));
    }
}