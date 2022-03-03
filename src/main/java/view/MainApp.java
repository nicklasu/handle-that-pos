package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.classes.POSEngine;
import model.interfaces.IPOSEngine;

import java.io.IOException;

public class MainApp extends Application {

    private Stage stage;
    private IPOSEngine engine;
    private final String[] hotkeyProductNames = new String[5];
    /*
    @Override
    public void start(Stage stage) throws IOException {

        this.stage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setFullScreen(true);
        stage.setTitle("Kassajärjestelmä");
        stage.setScene(scene);
        stage.show();
    }*/

    public MainApp() {
        this.engine = new POSEngine();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("Kassajärjestelmä");
        showLoginView();
    }

    public String[] getHotkeyButtonNames() {
        return hotkeyProductNames;
    }

    public void setHotkeyButtonName(String hotkeyButtonName, int buttonId) {
        hotkeyProductNames[buttonId] = hotkeyButtonName;
    }

    public void showMainView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            this.stage.setScene(scene);
            //this.stage.setFullScreen(true);
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
            //this.stage.setFullScreen(true);
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
            //this.stage.setFullScreen(true);
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
            //this.stage.setFullScreen(true);
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

    public static void main(String[] args) {
        launch();
    }
}