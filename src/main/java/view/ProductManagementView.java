package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import model.classes.User;

import java.io.IOException;

public class ProductManagementView {
    private MainApp mainApp;
    private FXMLLoader loader;
    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
    }
        //@FXML
    //private Button addProductBtn;
    @FXML
    Pane wrapperPaneProducts = new Pane();

    /** Switch to add-product-view.fxml */
    public void addProductPane(ActionEvent event) throws IOException{

        System.out.println("add product button was pressed");
        wrapperPaneProducts.getChildren().clear();
        Pane newLoadedPane3 = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setLocation(getClass().getResource("add-product-view.fxml"));
            newLoadedPane3 = this.loader.load();
            AddProductView view = this.loader.getController();
            view.setMainApp(mainApp);
            //newLoadedPane3 = FXMLLoader.load(getClass().getResource("add-product-view.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneProducts.getChildren().add(newLoadedPane3);
    }

    /** Switch to delete-product-view.fxml */
    public void deleteProductPane(ActionEvent event) throws IOException{

        System.out.println("add product button was pressed");
        wrapperPaneProducts.getChildren().clear();
        Pane newLoadedPane3 = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setLocation(getClass().getResource("delete-product-view.fxml"));
            newLoadedPane3 = this.loader.load();
            DeleteProductView view = this.loader.getController();
            view.setMainApp(mainApp);
            //newLoadedPane3 = FXMLLoader.load(getClass().getResource("delete-product-view.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneProducts.getChildren().add(newLoadedPane3);
    }

    /** Switch to edit-product-view.fxml */
    public void editProductPane(ActionEvent event) throws IOException{

        System.out.println("add product button was pressed");
        wrapperPaneProducts.getChildren().clear();
        Pane newLoadedPane3 = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setLocation(getClass().getResource("edit-product-view.fxml"));
            newLoadedPane3 = this.loader.load();
            EditProductView view = this.loader.getController();
            view.setMainApp(mainApp);
            //newLoadedPane3 = FXMLLoader.load(getClass().getResource("edit-product-view.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneProducts.getChildren().add(newLoadedPane3);
    }
}