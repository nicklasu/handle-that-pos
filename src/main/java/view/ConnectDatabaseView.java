package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import model.classes.HibernateUtil;
import model.classes.POSEngine;

public class ConnectDatabaseView {

    private MainApp mainApp;

    @FXML
    private Label connectingToDatabaseLabel;

    @FXML
    private Label failedToConnectToDatabaseLabel;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Button retryButton;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        tryToConnect();
    }

    @FXML
    private void tryToConnect() {
        retryButton.setVisible(false);
        failedToConnectToDatabaseLabel.setVisible(false);
        connectingToDatabaseLabel.setVisible(true);
        progressIndicator.setVisible(true);

        Thread t1 = new Thread(() -> {
            if (sessionFactoryCreated()) {
                mainApp.setEngine(new POSEngine());
                Platform.runLater(() -> {
                    mainApp.showLoginView();
                });
            } else {
                Platform.runLater(() -> {
                    connectingToDatabaseLabel.setVisible(false);
                    failedToConnectToDatabaseLabel.setVisible(true);
                    progressIndicator.setVisible(false);
                    retryButton.setVisible(true);
                });
            }
        });
        t1.start();
    }

    private boolean sessionFactoryCreated() {
        try {
            HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            System.out.println("virhe istuntotehtaan luomisessa");
            return false;
        }
        return true;
    }

}
