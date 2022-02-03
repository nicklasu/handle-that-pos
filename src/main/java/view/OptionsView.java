package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class OptionsView {
    @FXML
    private AnchorPane transactionAnchorPane;
    public void loadMainView(ActionEvent event) throws IOException {
        new ViewLoader(transactionAnchorPane, FXMLLoader.load(getClass().getResource("main-view.fxml")));
    }
}
