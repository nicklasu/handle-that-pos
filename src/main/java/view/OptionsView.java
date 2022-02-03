package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import model.classes.Product;

import java.io.IOException;
import java.util.ArrayList;

public class OptionsView {
    private MainApp mainApp;
    @FXML
    private AnchorPane transactionAnchorPane;
    public void loadMainView(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        new ViewLoader(transactionAnchorPane, fxmlLoader.load());
        ((MainView) fxmlLoader.getController()).setMainApp(this.mainApp);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        if (this.mainApp.getEngine().getTransaction() != null) {
        }
    }
}
