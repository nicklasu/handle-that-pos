package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
 * Represents the hardware running the software
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
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

    @Override
    public void init() {
        final File appConfigPath = new File("src/main/resources/HandleThatPos.properties");
        final Properties properties = new Properties();
        try (final FileReader reader = new FileReader(appConfigPath)) {
            properties.load(reader);
            language = properties.getProperty("language");
            country = properties.getProperty("country");
        } catch (final IOException e) {
            try (final FileWriter writer = new FileWriter(appConfigPath)) {
                properties.setProperty("language", "");
                properties.setProperty("country", "");
                properties.store(writer, "HandleThatPos settings");
            } catch (final Exception b) {
                b.printStackTrace();
            }
        }
        this.locale = new Locale(language, country);
        Locale.setDefault(locale);
        this.bundle = ResourceBundle.getBundle("TextResources", locale);
    }

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
            final ConnectDatabaseView connectDatabaseView = fxmlLoader.getController();
            connectDatabaseView.setMainApp(this);
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
            final MainView mainView = fxmlLoader.getController();
            mainView.setMainApp(this);
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
            final LoginView loginView = fxmlLoader.getController();
            loginView.setMainApp(this);
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
            final OptionsView optionsView = fxmlLoader.getController();
            optionsView.setMainApp(this);
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
            final TransactionView transactionView = fxmlLoader.getController();
            transactionView.setMainApp(this);
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