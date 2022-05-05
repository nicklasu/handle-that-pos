package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.interfaces.IPOSEngine;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * The JavaFX main.
 * Has methods, that get called when the desired view is needed.
 * If I wanted to load main-view.fxml, I would call showMainView for example.
 * Views call the controllers they want to use (for example MainViewController).
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 * Samu Luoma
 */
public class MainApp extends Application {
    public static final String APP_TITLE = "Handle that POS";
    public static final String MAIN_VIEW_CSS = "main-view.css";
    public static final String MAIN_VIEW_DARK_CSS = "main-view-dark.css";
    private Stage stage;
    private IPOSEngine engine;
    private final String[] hotkeyProductNames = new String[9];
    private String language = "";
    private String country = "";
    private Locale locale;
    private ResourceBundle bundle;
    private boolean darkMode;

    public MainApp() {

    }

    /**
     * Init gets called before start and in this app deals with getting localization settings from the .properties file.
     */
    @Override
    public void init() {
        final File appConfigPath = new File("src/main/resources/HandleThatPos.properties");
        final Properties properties = new Properties();
        try (final FileReader reader = new FileReader(appConfigPath)) {
            properties.load(reader);
            if (properties.isEmpty()) {
                final FileWriter writer = new FileWriter(appConfigPath);
                properties.setProperty("language", "");
                properties.setProperty("country", "");
                properties.store(writer, "HandleThatPos settings");
                writer.close();
            }
            language = properties.getProperty("language");
            country = properties.getProperty("country");
        } catch (final IOException e) {
            this.locale = new Locale("", "");
            this.bundle = ResourceBundle.getBundle("TextResources", locale);
            final Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("cantloadhandlethatpos"));
            alert.showAndWait();
        }
        this.locale = new Locale(language, country);
        Locale.setDefault(locale);
        this.bundle = ResourceBundle.getBundle("TextResources", locale);
    }

    /**
     * Sets the size of the window according to the resolution of the system.
     * Shows the databaseConnection view after setting the other parts.
     * @param stage Takes the stage.
     */
    @Override
    public void start(final Stage stage) {
        this.stage = stage;
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        this.stage.setHeight(size.getHeight());
        this.stage.setWidth(size.getWidth());
        this.stage.setMaximized(true);
        stage.setTitle(APP_TITLE);
        this.stage.getIcons().add(new Image("file:src/main/resources/images/pos.png"));
        showConnectToDatabaseView();
    }

    public String[] getHotkeyButtonNames() {
        return hotkeyProductNames;
    }

    public void setHotkeyButtonName(final String hotkeyButtonName, final int buttonId) {
        hotkeyProductNames[buttonId] = hotkeyButtonName;
    }

    private void showConnectToDatabaseView() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("connect-database-view.fxml"));
            fxmlLoader.setResources(this.bundle);
            final Scene scene = new Scene(fxmlLoader.load());
            this.stage.setScene(scene);
            final ConnectDatabaseController connectDatabaseController = fxmlLoader.getController();
            connectDatabaseController.setMainApp(this);
            this.stage.show();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainView() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main-view.fxml"));
            fxmlLoader.setResources(this.bundle);
            final Scene scene = new Scene(fxmlLoader.load());
            this.stage.setScene(scene);
            final MainViewController mainViewController = fxmlLoader.getController();
            mainViewController.setMainApp(this);
            /* Set CSS stylesheet */
            final File appConfigPath = new File("src/main/resources/HandleThatPos.properties");
            final Properties properties = new Properties();
            try (final FileReader reader = new FileReader(appConfigPath, StandardCharsets.UTF_8)) {
                properties.load(reader);
                this.darkMode = Boolean.parseBoolean(properties.getProperty("mode"));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            if (this.darkMode) {
                scene.getStylesheets().add(getClass().getResource(MAIN_VIEW_CSS).toExternalForm()); // Set light css style
            } else {
                scene.getStylesheets().add(getClass().getResource(MAIN_VIEW_DARK_CSS).toExternalForm()); // Set dark css style
            }
            this.stage.show();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void showLoginView() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("login-view.fxml"));
            fxmlLoader.setResources(this.bundle);
            final Scene scene = new Scene(fxmlLoader.load());
            this.stage.setScene(scene);
            final LoginController loginController = fxmlLoader.getController();
            loginController.setMainApp(this);
            this.stage.show();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void showOptionsView() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("options-view.fxml"));
            fxmlLoader.setResources(this.bundle);
            final Scene scene = new Scene(fxmlLoader.load());
            this.stage.setScene(scene);
            final OptionsController optionsController = fxmlLoader.getController();
            optionsController.setMainApp(this);
            /* Set CSS stylesheet */
            if (darkMode) {
                scene.getStylesheets().add(getClass().getResource(MAIN_VIEW_CSS).toExternalForm()); // Set light css style
            } else {
                scene.getStylesheets().add(getClass().getResource(MAIN_VIEW_DARK_CSS).toExternalForm()); // Set dark css style
            }
            this.stage.show();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void showTransactionView() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("transaction-view.fxml"));
            fxmlLoader.setResources(this.bundle);
            final Scene scene = new Scene(fxmlLoader.load());
            this.stage.setScene(scene);
            final TransactionController transactionController = fxmlLoader.getController();
            transactionController.setMainApp(this);
            /* Set CSS stylesheet */
            if (darkMode) {
                scene.getStylesheets().add(getClass().getResource(MAIN_VIEW_CSS).toExternalForm()); // Set light css style
            } else {
                scene.getStylesheets().add(getClass().getResource(MAIN_VIEW_DARK_CSS).toExternalForm()); // Set dark css style
            }
            this.stage.show();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public IPOSEngine getEngine() {
        return this.engine;
    }

    public void setEngine(final IPOSEngine engine) {
        this.engine = engine;
    }

    public Stage getStage() {
        return this.stage;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(final ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public static void main(final String[] args) {
        launch();
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public boolean isDarkMode() {
        return darkMode;
    }
}