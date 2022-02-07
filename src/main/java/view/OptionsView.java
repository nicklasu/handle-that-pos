package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.classes.Product;
import model.classes.User;

import java.io.IOException;
import java.util.ArrayList;

public class OptionsView {
    private MainApp mainApp;
    @FXML
    private Text fName;
    @FXML
    private Text lName;
    @FXML
    private AnchorPane transactionAnchorPane;
    public void loadMainView(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        new ViewLoader(transactionAnchorPane, fxmlLoader.load());
        ((MainView) fxmlLoader.getController()).setMainApp(this.mainApp);
    }

    public void setMainApp(MainApp mainApp) {

        this.mainApp = mainApp;
        User user = this.mainApp.getEngine().getUser();
        fName.setText(user.getfName());
        lName.setText(user.getlName());

        if (this.mainApp.getEngine().getTransaction() != null) {
        }
    }
}
