package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class BonusCustomerManagementView {
    private MainApp mainApp;
    private FXMLLoader loader;

    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
    }

    @FXML
    Pane wrapperPaneBonusCustomers = new Pane();

    /**
     * Switch to add-bonus-customer-view.fxml
     */
    public void addBonusCustomerPane(ActionEvent event) throws IOException {

        System.out.println("add user button was pressed");
        wrapperPaneBonusCustomers.getChildren().clear();
        Pane newLoadedPane = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setLocation(getClass().getResource("add-bonus-customer-view.fxml"));
            newLoadedPane = this.loader.load();
            AddBonusCustomerView view = this.loader.getController();
            view.setMainApp(mainApp);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneBonusCustomers.getChildren().add(newLoadedPane);
    }
}
