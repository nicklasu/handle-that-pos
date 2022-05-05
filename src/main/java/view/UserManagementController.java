package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Controller for user-management-view.fxml.
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class UserManagementController {
    private MainApp mainApp;
    private FXMLLoader loader;

    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    Pane wrapperPaneUsers = new Pane();

    /**
     * Switch to add-product-view.fxml
     */
    public void addUserPane() throws IOException {
        System.out.println("add user button was pressed");
        wrapperPaneUsers.getChildren().clear();
        Pane newLoadedPane = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setLocation(getClass().getResource("add-user-view.fxml"));
            this.loader.setResources(this.mainApp.getBundle());
            newLoadedPane = this.loader.load();
            final AddUserController view = this.loader.getController();
            view.setMainApp(mainApp);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneUsers.getChildren().add(newLoadedPane);
    }

    /**
     * Switch to delete-user-view.fxml
     */
    public void deleteUserPane() throws IOException {
        System.out.println("delete user button was pressed");
        wrapperPaneUsers.getChildren().clear();
        Pane newLoadedPane2 = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setLocation(getClass().getResource("delete-user-view.fxml"));
            this.loader.setResources(this.mainApp.getBundle());
            newLoadedPane2 = this.loader.load();
            final DeleteUserController view = this.loader.getController();
            view.setMainApp(mainApp);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneUsers.getChildren().add(newLoadedPane2);
    }

    /**
     * Switch to edit-user-view.fxml
     */
    public void editUserPane() throws IOException {
        System.out.println("edit user button was pressed");
        wrapperPaneUsers.getChildren().clear();
        Pane newLoadedPane3 = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setLocation(getClass().getResource("edit-user-view.fxml"));
            this.loader.setResources(this.mainApp.getBundle());
            newLoadedPane3 = this.loader.load();
            final EditUserController view = this.loader.getController();
            view.setMainApp(mainApp);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneUsers.getChildren().add(newLoadedPane3);
    }
}
