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

    public void setMainApp(final MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
    }

    @FXML
    Pane wrapperPaneProducts = new Pane();

    /**
     * Switch to add-product-view.fxml
     */
    public void addProductPane() throws IOException {
        System.out.println("add product button was pressed");
        wrapperPaneProducts.getChildren().clear();
        Pane newLoadedPane3 = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setResources(this.mainApp.getBundle());
            this.loader.setLocation(getClass().getResource("add-product-view.fxml"));
            this.loader.setResources(this.mainApp.getBundle());
            newLoadedPane3 = this.loader.load();
            final AddProductView view = this.loader.getController();
            view.setMainApp(mainApp);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneProducts.getChildren().add(newLoadedPane3);
    }

    /**
     * Switch to delete-product-view.fxml
     */
    public void deleteProductPane() throws IOException {
        System.out.println("delete product button was pressed");
        wrapperPaneProducts.getChildren().clear();
        Pane newLoadedPane3 = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setResources(this.mainApp.getBundle());
            this.loader.setLocation(getClass().getResource("delete-product-view.fxml"));
            this.loader.setResources(this.mainApp.getBundle());
            newLoadedPane3 = this.loader.load();
            final DeleteProductView view = this.loader.getController();
            view.setMainApp(mainApp);
            // newLoadedPane3 =
            // FXMLLoader.load(getClass().getResource("delete-product-view.fxml"));
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneProducts.getChildren().add(newLoadedPane3);
    }

    /**
     * Switch to edit-product-view.fxml
     */
    public void editProductPane() throws IOException {
        System.out.println("edit product button was pressed");
        wrapperPaneProducts.getChildren().clear();
        Pane newLoadedPane3 = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setResources(this.mainApp.getBundle());
            this.loader.setLocation(getClass().getResource("edit-product-view.fxml"));
            this.loader.setResources(this.mainApp.getBundle());
            newLoadedPane3 = this.loader.load();
            final EditProductView view = this.loader.getController();
            view.setMainApp(mainApp);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneProducts.getChildren().add(newLoadedPane3);
    }

    /**
     * Switch to edit-product-view.fxml with autofilled text fields
     */
    public void editProductPaneFillFields(final Product product) {
        wrapperPaneProducts.getChildren().clear();
        Pane newLoadedPane = null;
        try {
            this.loader = new FXMLLoader();
            this.loader.setResources(this.mainApp.getBundle());
            this.loader.setLocation(getClass().getResource("edit-product-view.fxml"));
            this.loader.setResources(this.mainApp.getBundle());
            newLoadedPane = this.loader.load();
            final EditProductView view = this.loader.getController();
            view.setMainApp(mainApp);
            view.fillFieldsOnLoad(product);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        wrapperPaneProducts.getChildren().add(newLoadedPane);
    }
}