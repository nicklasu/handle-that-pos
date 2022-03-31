package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import model.classes.Privilege;
import model.classes.User;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    private Button btn5;
    @FXML
    private Button btn6;
    @FXML
    private Button returnBtn;
    @FXML
    private Button helpBtn;
    @FXML
    Pane wrapperPane = new Pane();
    private FXMLLoader loader;

    public void loadMainView(ActionEvent event) throws IOException {
        this.loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        new ViewLoader(transactionAnchorPane, this.loader.load());
        ((MainView) this.loader.getController()).setMainApp(this.mainApp);
    }

    public void setMainApp(MainApp mainApp) throws IOException {
        /** Change views. */
        btn1.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            Pane newLoadedPane0 = null;
            try {
                this.loader = new FXMLLoader();
                this.loader.setLocation(getClass().getResource("users-view.fxml"));
                this.loader.setResources(this.mainApp.getBundle());
                newLoadedPane0 = this.loader.load();
                UsersView view = this.loader.getController();
                view.setMainApp(mainApp);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            wrapperPane.getChildren().add(newLoadedPane0);
        });
        btn2.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            Pane newLoadedPane2 = null;
            try {
                this.loader = new FXMLLoader();
                this.loader.setLocation(getClass().getResource("products-view.fxml"));
                this.loader.setResources(this.mainApp.getBundle());
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
                this.loader.setLocation(getClass().getResource("user-management-view.fxml"));
                this.loader.setResources(this.mainApp.getBundle());
                newLoadedPane3 = this.loader.load();
                UserManagementView view = this.loader.getController();
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
                ProductSearchView view = this.loader.getController();
                view.setMainApp(mainApp);
                view.setWrapperPane(wrapperPane); // jotta voidaan siirtyä suoraan edit ikkunaan
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            wrapperPane.getChildren().add(newLoadedPane);
        });
        btn5.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            Pane newLoadedPane = null;
            try {
                this.loader = new FXMLLoader();
                this.loader.setLocation(getClass().getResource("bonus-customer-management-view.fxml"));
                newLoadedPane = this.loader.load();
                BonusCustomerManagementView view = this.loader.getController();
                view.setMainApp(mainApp);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            wrapperPane.getChildren().add(newLoadedPane);
        });
        btn6.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            Pane newLoadedPane = null;
            try {
                this.loader = new FXMLLoader();
                this.loader.setLocation(getClass().getResource("edit-firm-info.fxml"));
                newLoadedPane = this.loader.load();
                EditFirmInfoView view = this.loader.getController();
                view.setMainApp(mainApp);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            wrapperPane.getChildren().add(newLoadedPane);
        });

        this.mainApp = mainApp;
        List<Integer> privilegesOfUser = this.mainApp.getEngine().getVerifiedPrivileges();


        if (privilegesOfUser.isEmpty() || Collections.max(privilegesOfUser) < 1) {
            btn1.setDisable(true);
            btn2.setDisable(true);
            btn3.setDisable(true);
            btn4.setDisable(true);
        }
        User user = this.mainApp.getEngine().getUser();
        fName.setText(user.getfName());
        lName.setText(user.getlName());
        returnBtn.requestFocus();
       /* if (this.mainApp.getEngine().getTransaction() != null) {
        }*/
    }

    @FXML
    public void showHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Valitse tarvittava toiminto listasta vasemmalla." +
                "\n\nTuotteiden lisääminen: Valitse 'Tuotteiden hallinta' välilehti. Klikkaa ”Lisää tuote” -painiketta ja syötä tuotteen tiedot. Varmista, että tuotteen viivakoodi on ainutlaatuinen. Napsauttaa ”Tallenna”, kun olet valmis." +
                "\n\nTuotteen poistaminen: Valitse 'Tuotteiden hallinta', sitten 'Poista tuote'. Syötä poistettavan tuotteen tunnus ja napsauta 'Poista'. " +
                "\n\nTuotteen muokkaus: Valitse joko 'Tuotteiden haku', sitten valitse muokattava tuote kaksoisklikkaamalla sitä. Syötä tuotteen uudet tiedot ja klikkaa ”Muokkaa”. Tai avaa 'Tuotteiden hallinta' välilehti. Valitse ”Muokkaa tuote”. Syötä muokattavan tuotteen viivakoodi ja napsauta ”Hae”. Syötä tuotteen uudet tiedot ja klikkaa ”Muokkaa”.", ButtonType.CLOSE);
        alert.setTitle("Ohje");
        alert.setHeaderText("Ohje");
        alert.showAndWait();
    }
}

