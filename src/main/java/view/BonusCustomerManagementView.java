package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Represents the hardware running the software
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class BonusCustomerManagementView {
    private MainApp mainApp;
    private FXMLLoader loader;

    public void setMainApp(final MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
    }

    @FXML
    Pane wrapperPaneBonusCustomers = new Pane();

    /**
     * Switch to add-bonus-customer-view.fxml
     */
    public void addBonusCustomerPane() {
        wrapperPaneBonusCustomers.getChildren().clear();
        Pane newLoadedPane = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setLocation(getClass().getResource("add-bonus-customer-view.fxml"));
            this.loader.setResources(this.mainApp.getBundle());
            newLoadedPane = this.loader.load();
            final AddBonusCustomerView view = this.loader.getController();
            view.setMainApp(mainApp);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneBonusCustomers.getChildren().add(newLoadedPane);
    }

    public void deleteBonusCustomerPane() {
        wrapperPaneBonusCustomers.getChildren().clear();
        Pane newLoadedPane = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setLocation(getClass().getResource("delete-bonus-customer-view.fxml"));
            this.loader.setResources(this.mainApp.getBundle());
            newLoadedPane = this.loader.load();
            final DeleteBonusCustomerView view = this.loader.getController();
            view.setMainApp(mainApp);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneBonusCustomers.getChildren().add(newLoadedPane);
    }
}
