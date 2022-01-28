package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TransactionView {

    @FXML
    private AnchorPane transactionAnchorPane;

    public void loadMainView(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("main-view.fxml"));
        transactionAnchorPane.getChildren().setAll(pane);
        pane.prefWidthProperty().bind(transactionAnchorPane.widthProperty());
        pane.prefHeightProperty().bind(transactionAnchorPane.heightProperty());
    }
}
