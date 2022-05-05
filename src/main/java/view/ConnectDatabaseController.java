package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import model.classes.HibernateUtil;
import model.classes.POSEngine;

/**
 * Controller for connect-database-view.fxml.
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class ConnectDatabaseController {

    private MainApp mainApp;

    @FXML
    private Label connectingToDatabaseLabel;

    @FXML
    private Label failedToConnectToDatabaseLabel;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Button retryButton;

    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
        tryToConnect();
    }

    /**
     * Tries to connect to database, and displays "retry" -button if it fails after trying for a while.
     */
    @FXML
    private void tryToConnect() {
        retryButton.setVisible(false);
        failedToConnectToDatabaseLabel.setVisible(false);
        connectingToDatabaseLabel.setVisible(true);
        progressIndicator.setVisible(true);

        final Thread t1 = new Thread(() -> {
            if (sessionFactoryCreated()) {
                mainApp.setEngine(new POSEngine());
                Platform.runLater(mainApp::showLoginView);
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
            HibernateUtil.getSessionFactory("hibernate.cfg.xml");
        } catch (final Exception e) {
            return false;
        }
        return true;
    }
}
