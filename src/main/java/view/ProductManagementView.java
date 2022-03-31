package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import model.classes.Product;

import java.io.IOException;

public class ProductManagementView {
    private MainApp mainApp;
    private FXMLLoader loader;

    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
    }

    @FXML
    Pane wrapperPaneProducts = new Pane();

    /**
     * Switch to add-product-view.fxml
     */
    public void addProductPane(ActionEvent event) throws IOException {
        System.out.println("add product button was pressed");
        wrapperPaneProducts.getChildren().clear();
        Pane newLoadedPane3 = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setLocation(getClass().getResource("add-product-view.fxml"));
            this.loader.setResources(this.mainApp.getBundle());
            newLoadedPane3 = this.loader.load();
            AddProductView view = this.loader.getController();
            view.setMainApp(mainApp);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneProducts.getChildren().add(newLoadedPane3);
    }

    /**
     * Switch to delete-product-view.fxml
     */
    public void deleteProductPane(ActionEvent event) throws IOException {
        System.out.println("delete product button was pressed");
        wrapperPaneProducts.getChildren().clear();
        Pane newLoadedPane3 = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setLocation(getClass().getResource("delete-product-view.fxml"));
            this.loader.setResources(this.mainApp.getBundle());
            newLoadedPane3 = this.loader.load();
            DeleteProductView view = this.loader.getController();
            view.setMainApp(mainApp);
            //newLoadedPane3 = FXMLLoader.load(getClass().getResource("delete-product-view.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneProducts.getChildren().add(newLoadedPane3);
    }

    /**
     * Switch to edit-product-view.fxml
     */
    public void editProductPane(ActionEvent event) throws IOException {
        System.out.println("edit product button was pressed");
        wrapperPaneProducts.getChildren().clear();
        Pane newLoadedPane3 = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setLocation(getClass().getResource("edit-product-view.fxml"));
            this.loader.setResources(this.mainApp.getBundle());
            newLoadedPane3 = this.loader.load();
            EditProductView view = this.loader.getController();
            view.setMainApp(mainApp);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneProducts.getChildren().add(newLoadedPane3);
    }

    /**
     * Switch to edit-product-view.fxml with autofilled text fields
     */
    public void editProductPaneFillFields(Product product) {
        wrapperPaneProducts.getChildren().clear();
        Pane newLoadedPane = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setLocation(getClass().getResource("edit-product-view.fxml"));
            this.loader.setResources(this.mainApp.getBundle());
            newLoadedPane = this.loader.load();
            EditProductView view = this.loader.getController();
            view.setMainApp(mainApp);
            view.fillFieldsOnLoad(product);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneProducts.getChildren().add(newLoadedPane);
    }
}