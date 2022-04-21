package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.classes.User;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Represents the hardware running the software
 *
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 * Samu Luoma
 */
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
    private Button btn7;
    @FXML
    private Button returnBtn;
    @FXML
    private Button darkmode;
    @FXML
    private ChoiceBox<String> languageBox;
    @FXML
    private Button aboutButton;

    private boolean darkMode;

    private final ObservableList<String> languages = FXCollections.observableArrayList("fi", "en");
    @FXML
    Pane wrapperPane = new Pane();
    private FXMLLoader loader;
    private ResourceBundle bundle;

    public OptionsView() {
    }

    public void loadMainView() throws IOException {
        this.loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        this.loader.setResources(mainApp.getBundle());
        new ViewLoader(transactionAnchorPane, this.loader.load());
        ((MainView) this.loader.getController()).setMainApp(this.mainApp);
    }

    public void setMainApp(final MainApp mainApp) throws IOException {
        final File appConfigPath = new File("src/main/resources/HandleThatPos.properties");
        final Properties properties = new Properties();
        try (final FileReader reader = new FileReader(appConfigPath, StandardCharsets.UTF_8)) {
            properties.load(reader);
            final String language = properties.getProperty("language");
            languageBox.setValue(language);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        languageBox.setItems(languages);
        languageBox.setOnAction(event -> {
            final String lang = switch (languageBox.getValue()) {
                case "fi" -> "fi_FI";
                case "en" -> "en_US";
                default -> throw new IllegalStateException("Unexpected value: " + languageBox.getValue());
            };
            try (final FileWriter writer = new FileWriter(appConfigPath, StandardCharsets.UTF_8)) {
                properties.setProperty("language", lang.split("_")[0]);
                properties.setProperty("country", lang.split("_")[1]);
                properties.store(writer, "HandleThatPos settings");
                final Locale locale = new Locale(lang.split("_")[0], lang.split("_")[1]);
                Locale.setDefault(locale);
                this.mainApp.setBundle(ResourceBundle.getBundle("TextResources", locale));
                this.mainApp.showOptionsView();

            } catch (final Exception g) {
                g.printStackTrace();
            }
        });

        darkmode.setOnAction(event -> {
            darkMode = !mainApp.isDarkMode();
            mainApp.setDarkMode(darkMode);

            if (mainApp.isDarkMode()) {
                darkmode.setText("Light mode");
            } else {
                darkmode.setText("Dark mode");
            }

            try (final FileWriter writer = new FileWriter(appConfigPath, StandardCharsets.UTF_8)) {
                properties.setProperty("mode", String.valueOf(darkMode));
                properties.store(writer, "HandleThatPos settings");
                this.mainApp.showOptionsView();

            } catch (final Exception b) {
                b.printStackTrace();
            }
        });

        /* Change views. */
        btn1.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            Pane newLoadedPane0 = null;
            try {
                this.loader = new FXMLLoader();
                this.loader.setLocation(getClass().getResource("users-view.fxml"));
                this.loader.setResources(this.mainApp.getBundle());
                newLoadedPane0 = this.loader.load();
                final UsersView view = this.loader.getController();
                view.setMainApp(mainApp);
            } catch (final IOException ex) {
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
                final ProductManagementView view = this.loader.getController();
                view.setMainApp(mainApp);
            } catch (final IOException ex) {
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
                final UserManagementView view = this.loader.getController();
                view.setMainApp(mainApp);
            } catch (final IOException ex) {
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
                final ProductSearchView view = this.loader.getController();
                view.setMainApp(mainApp);
                view.setWrapperPane(wrapperPane); // jotta voidaan siirtyä suoraan edit ikkunaan
            } catch (final IOException ex) {
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
                final BonusCustomerManagementView view = this.loader.getController();
                view.setMainApp(mainApp);
            } catch (final IOException ex) {
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
                final EditFirmInfoView view = this.loader.getController();
                view.setMainApp(mainApp);
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
            wrapperPane.getChildren().add(newLoadedPane);
        });

        btn7.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            Pane newLoadedPane = null;
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            try {
                this.loader = new FXMLLoader();
                this.loader.setLocation(getClass().getResource("stats-view.fxml"));
                this.loader.setResources(this.mainApp.getBundle());
                newLoadedPane = this.loader.load();
                newLoadedPane.setMaxSize(size.getWidth() * 0.7, size.getHeight() * 0.8);
                final StatsView view = this.loader.getController();
                view.setMainApp(mainApp);
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
            wrapperPane.getChildren().add(newLoadedPane);
        });

        this.mainApp = mainApp;
        final List<Integer> privilegesOfUser = this.mainApp.getEngine().getVerifiedPrivileges();

        if (privilegesOfUser.isEmpty() || Collections.max(privilegesOfUser) < 2) {
            btn2.setVisible(false);
            btn3.setVisible(false);
            btn6.setVisible(false);
            btn7.setVisible(false);
        }
        returnBtn.requestFocus();
        this.aboutButton.setOnAction(e -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about-dialog-view.fxml"));
            Parent parent = null;
            try {
                fxmlLoader.setResources(this.mainApp.getBundle());
                parent = fxmlLoader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        });
    }

    @FXML
    public void showHelp() {
        bundle = mainApp.getBundle();
        final Alert alert = new Alert(Alert.AlertType.INFORMATION, bundle.getString("optionsViewHelpString"), ButtonType.CLOSE);
        alert.setTitle(bundle.getString("helpString"));
        alert.setHeaderText(bundle.getString("helpString"));
        alert.showAndWait();
    }
}
