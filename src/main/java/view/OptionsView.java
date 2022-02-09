package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import model.classes.User;

import java.io.IOException;

public class OptionsView {
    private MainApp mainApp;
    @FXML
    private Text fName;
    @FXML
    private Text lName;
    @FXML
    private AnchorPane transactionAnchorPane;
    //@FXML
    //private ListView listView;
    @FXML
    private Button btn1;
    @FXML
    private Button btn2;
    @FXML
    Pane wrapperPane = new Pane();

    //private ObservableList<String> optionsList = FXCollections.observableArrayList();

    public void loadMainView(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        new ViewLoader(transactionAnchorPane, fxmlLoader.load());
        ((MainView) fxmlLoader.getController()).setMainApp(this.mainApp);
    }

    public void setMainApp(MainApp mainApp) throws IOException {
        //optionsList.add("Tuotteet");
        //optionsList.add("Käyttäjät");
        //listView.setItems(optionsList);

        /* Change views: */
        btn1.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            Pane newLoadedPane = null;
            try {
                newLoadedPane = FXMLLoader.load(getClass().getResource("users-view.fxml"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            wrapperPane.getChildren().add(newLoadedPane);
        });

        btn2.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            Pane newLoadedPane2 = null;
            try {
                newLoadedPane2 = FXMLLoader.load(getClass().getResource("add-product-view.fxml"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            wrapperPane.getChildren().add(newLoadedPane2);
        });

        this.mainApp = mainApp;
        User user = this.mainApp.getEngine().getUser();
        fName.setText(user.getfName());
        lName.setText(user.getlName());

        if (this.mainApp.getEngine().getTransaction() != null) {}
    }
}
