package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import model.classes.Privilege;
import model.classes.User;

import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class OptionsView {
    private MainApp mainApp;
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
    private ChoiceBox<String> languageBox;

    private ObservableList<String> languages = FXCollections.observableArrayList("fi", "en");
    @FXML
    Pane wrapperPane = new Pane();
    private FXMLLoader loader;
    private ResourceBundle bundle;

    public OptionsView() {
    }

    public void loadMainView(ActionEvent event) throws IOException {
        this.loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        this.loader.setResources(mainApp.getBundle());
        new ViewLoader(transactionAnchorPane, this.loader.load());
        ((MainView) this.loader.getController()).setMainApp(this.mainApp);
    }

    public void setMainApp(MainApp mainApp) throws IOException {
        File appConfigPath = new File("src/main/resources/HandleThatPos.properties");
        Properties properties = new Properties();
        try {
            FileReader reader = new FileReader(appConfigPath);
            properties.load(reader);
            String language = properties.getProperty("language");
            languageBox.setValue(language);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        languageBox.setItems(languages);
        languageBox.setOnAction(event -> {
            String lang = switch (languageBox.getValue()) {
                case "fi" -> "fi_FI";
                case "en" -> "en_US";
                default -> throw new IllegalStateException("Unexpected value: " + languageBox.getValue());
            };
            try {
                FileWriter writer = new FileWriter(appConfigPath);
                properties.setProperty("language", lang.split("_")[0]);
                properties.setProperty("country", lang.split("_")[1]);
                properties.store(writer, "HandleThatPos settings");
                writer.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(this.mainApp.getBundle().getString("lang_alert_title"));
                alert.setHeaderText(this.mainApp.getBundle().getString("language_changed_header"));
                alert.setContentText(this.mainApp.getBundle().getString("language_changed") + " " + languageBox.getValue() + ". " + this.mainApp.getBundle().getString("restart_to_apply"));
                alert.show();
            } catch (Exception ignored) {
            }
        });
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
                this.loader.setResources(this.mainApp.getBundle());
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
                this.loader.setResources(this.mainApp.getBundle());
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
                this.loader.setResources(this.mainApp.getBundle());
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


        if (privilegesOfUser.isEmpty() || Collections.max(privilegesOfUser) < 2) {
            btn1.setDisable(true);
            btn2.setDisable(true);
            btn3.setDisable(true);
            btn4.setDisable(true);
        }
        User user = this.mainApp.getEngine().getUser();
        returnBtn.requestFocus();
       /* if (this.mainApp.getEngine().getTransaction() != null) {
        }*/
    }

    @FXML
    public void showHelp() {
        bundle = mainApp.getBundle();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, bundle.getString("optionsViewHelpString"), ButtonType.CLOSE);
        alert.setTitle(bundle.getString("helpString"));
        alert.setHeaderText(bundle.getString("helpString"));
        alert.showAndWait();
    }
}

