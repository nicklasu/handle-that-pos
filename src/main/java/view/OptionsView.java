package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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
    @FXML
    private Button btn1;
    @FXML
    private Button btn2;
    @FXML
    private Button btn3;
    @FXML
    private Button btn4;
    @FXML
    Pane wrapperPane = new Pane();
    private FXMLLoader loader;

    public void loadMainView(ActionEvent event) throws IOException {
        this.loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        new ViewLoader(transactionAnchorPane, this.loader.load());
        ((MainView) this.loader.getController()).setMainApp(this.mainApp);
    }

    public void setMainApp(MainApp mainApp) throws IOException {

        /** Change views: */
        btn1.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            Pane newLoadedPane = null;
            try {
                this.loader = new FXMLLoader();
                this.loader.setLocation(getClass().getResource("users-view.fxml"));
                newLoadedPane = this.loader.load();
                UsersView view = this.loader.getController();
                view.setMainApp(mainApp);
                //newLoadedPane = FXMLLoader.load(getClass().getResource("users-view.fxml"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            wrapperPane.getChildren().add(newLoadedPane);
        });

        btn2.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            Pane newLoadedPane2 = null;
            try {
                this.loader = new FXMLLoader();
                this.loader.setLocation(getClass().getResource("products-view.fxml"));
                newLoadedPane2 = this.loader.load();
                ProductManagementView view = this.loader.getController();
                view.setMainApp(mainApp);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            wrapperPane.getChildren().add(newLoadedPane2);
        });

        btn3.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            Pane newLoadedPane3 = null;
            try {
                this.loader = new FXMLLoader();
                this.loader.setLocation(getClass().getResource("add-user-view.fxml"));
                newLoadedPane3 = this.loader.load();
                AddUserView view = this.loader.getController();
                view.setMainApp(mainApp);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            wrapperPane.getChildren().add(newLoadedPane3);
        });

        btn4.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            Pane newLoadedPane = null;
            try {
                this.loader = new FXMLLoader();
                this.loader.setLocation(getClass().getResource("products-search-view.fxml"));
                newLoadedPane = this.loader.load();
                UsersView view = this.loader.getController();
                view.setMainApp(mainApp);
                //newLoadedPane = FXMLLoader.load(getClass().getResource("users-view.fxml"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            wrapperPane.getChildren().add(newLoadedPane);
        });

        this.mainApp = mainApp;
        User user = this.mainApp.getEngine().getUser();
        fName.setText(user.getfName());
        lName.setText(user.getlName());

        if (this.mainApp.getEngine().getTransaction() != null) {
        }
    }
}
