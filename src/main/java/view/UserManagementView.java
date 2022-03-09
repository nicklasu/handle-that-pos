package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class UserManagementView {
    private MainApp mainApp;
    private FXMLLoader loader;

    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
    }

    @FXML
    Pane wrapperPaneUsers = new Pane();

    /**
     * Switch to add-product-view.fxml
     */
    public void addUserPane(ActionEvent event) throws IOException {

        System.out.println("add user button was pressed");
        wrapperPaneUsers.getChildren().clear();
        Pane newLoadedPane = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setLocation(getClass().getResource("add-user-view.fxml"));
            newLoadedPane = this.loader.load();
            AddUserView view = this.loader.getController();
            view.setMainApp(mainApp);
            //newLoadedPane3 = FXMLLoader.load(getClass().getResource("add-product-view.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneUsers.getChildren().add(newLoadedPane);
    }
//        /**
//         * Switch to delete-user-view.fxml
//         */
//        public void deleteProductPane(ActionEvent event) throws IOException {
//
//            System.out.println("delete user button was pressed");
//            wrapperPaneUsers.getChildren().clear();
//            Pane newLoadedPane3 = null;
//            try {
//                this.loader = new FXMLLoader();
//                this.loader.setLocation(getClass().getResource("delete-user-view.fxml"));
//                newLoadedPane3 = this.loader.load();
//                DeleteProductView view = this.loader.getController();
//                view.setMainApp(mainApp);
//                //newLoadedPane3 = FXMLLoader.load(getClass().getResource("delete-user-view.fxml"));
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            wrapperPaneUsers.getChildren().add(newLoadedPane3);
//        }

//        /**
//         * Switch to edit-user-view.fxml
//         */
//        public void editProductPane(ActionEvent event) throws IOException {
//
//            System.out.println("edit product button was pressed");
//            wrapperPaneProducts.getChildren().clear();
//            Pane newLoadedPane3 = null;
//            try {
//                this.loader = new FXMLLoader();
//                this.loader.setLocation(getClass().getResource("edit-user-view.fxml"));
//                newLoadedPane3 = this.loader.load();
//                EditProductView view = this.loader.getController();
//                view.setMainApp(mainApp);
//                //newLoadedPane3 = FXMLLoader.load(getClass().getResource("edit-user-view.fxml"));
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            wrapperPaneProducts.getChildren().add(newLoadedPane3);
//        }
}
