package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainGUIController {
    @FXML
    private Button payButton;
    @FXML
    private AnchorPane mainAnchorPane;

    public void loadTransactionView(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("transaction-view.fxml"));
        mainAnchorPane.getChildren().setAll(pane);
        pane.prefWidthProperty().bind(mainAnchorPane.widthProperty());
        pane.prefHeightProperty().bind(mainAnchorPane.heightProperty());
    }
}