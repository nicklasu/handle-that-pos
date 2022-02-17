package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.classes.POSEngine;
import model.classes.ProductDAO;
import model.interfaces.IPOSEngine;

import java.io.IOException;

public class MainApp extends Application {

    private Stage stage;
    private IPOSEngine engine;
    private String hotkeyProductNames[] = new String[2];
    private boolean hotkeyDatabaseSearch = false;
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

    public String[] getHotkeyButtonNames(String hotkeyProductIds[]) {
        if (!hotkeyDatabaseSearch) {
            ProductDAO productDAO = new ProductDAO();
            for (int i = 0; i < hotkeyProductIds.length; i++) {
                //Necessary try catch. Otherwise program crash and burn.
                try {
                    hotkeyProductNames[i] = productDAO.getProduct(hotkeyProductIds[i]).getName();
                } catch (Exception ignored) {

                }
            }
            hotkeyDatabaseSearch = true;
        }
        return hotkeyProductNames;
    }

    public void setHotkeyButtonNames(String hotkeyProductIds[]){
        ProductDAO productDAO = new ProductDAO();
        for(int i = 0; i < hotkeyProductIds.length; i++){
            try {
                hotkeyProductNames[i] = productDAO.getProduct(hotkeyProductIds[i]).getName();
            } catch (Exception ignored) {

            }
        }
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

    public void showTransactionView() {
    }

    public IPOSEngine getEngine() {
        return this.engine;
    }

    public static void main(String[] args) {
        launch();

    }
}