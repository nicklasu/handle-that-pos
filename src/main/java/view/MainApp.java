package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.classes.POSEngine;
import model.interfaces.IPOSEngine;

import java.io.IOException;

public class MainApp extends Application {

    public static String APP_TITLE = "Handle that POS";

    private Stage stage;
    private IPOSEngine engine;
    private final String[] hotkeyProductNames = new String[9];

    public MainApp() {
        //this.engine = new POSEngine();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.stage.setMinHeight(750);
        this.stage.setMinWidth(1070);
        stage.setTitle(APP_TITLE);
        this.stage.getIcons().add(new Image("file:src/main/resources/images/pos.png"));

        //showLoginView();
        showConnectToDatabaseView();
    }

    public String[] getHotkeyButtonNames() {
        return hotkeyProductNames;
    }

    public void setHotkeyButtonName(String hotkeyButtonName, int buttonId) {
        hotkeyProductNames[buttonId] = hotkeyButtonName;
    }

    private void showConnectToDatabaseView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("connect-database-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            this.stage.setScene(scene);
            ConnectDatabaseView connectDatabaseView = fxmlLoader.getController();
            connectDatabaseView.setMainApp(this);
            this.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            this.stage.setScene(scene);
            MainView mainView = fxmlLoader.getController();
            mainView.setMainApp(this);
            this.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLoginView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            this.stage.setScene(scene);
            LoginView loginView = fxmlLoader.getController();
            loginView.setMainApp(this);
            this.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showOptionsView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("options-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            this.stage.setScene(scene);
            OptionsView optionsView = fxmlLoader.getController();
            optionsView.setMainApp(this);
            this.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTransactionView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("transaction-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            this.stage.setScene(scene);
            TransactionView transactionView = fxmlLoader.getController();
            transactionView.setMainApp(this);
            this.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IPOSEngine getEngine() {
        return this.engine;
    }

    public void setEngine(IPOSEngine engine) {
        this.engine = engine;
    }

    public Stage getStage() { return this.stage; }

    public static void main(String[] args) {
        launch();
    }
}