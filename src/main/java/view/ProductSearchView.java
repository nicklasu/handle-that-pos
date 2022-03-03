package view;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.classes.Product;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProductSearchView {
    private MainApp mainApp;

    private List allProducts;

    private ObservableList<Product> items = FXCollections.observableArrayList();

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> productDescription;

    @FXML
    private TableColumn<Product, String> productId;

    @FXML
    private TableColumn<Product, String> productName;

    @FXML
    private TableColumn<Product, Integer> productPrice;

    @FXML
    private TableColumn<Product, Integer> productStock;
    @FXML
    private Button fetchBtn;
    @FXML
    private TextField input;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        BooleanBinding booleanBind = input.textProperty().isEmpty();
        fetchBtn.disableProperty().bind(booleanBind);

        this.allProducts = this.mainApp.getEngine().productDao().getAllProducts();
        items.addAll(allProducts);
        //productListView.setItems(items);

        productDescription.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        productId.setCellValueFactory(new PropertyValueFactory<Product, String>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productPrice.setCellValueFactory(new PropertyValueFactory<Product, Integer>("price"));
        productStock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));

        productTable.setItems(items);


        productTable.setRowFactory(productTableView -> new TableRow<Product>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);
                if (product == null) {
                    setStyle("");
                }else if (product.getStock() <= 0) {
                    //setStyle("-fx-background-color: #ff3b3b;");
                    setStyle("-fx-background-color: rgba(255,59,59,0.5);");
                } else if (product.getStock() <= 50) {
                    setStyle("-fx-background-color: rgba(255,216,103,0.5);");
                } else {
                    //setStyle("");
                    setStyle("-fx-background-color: rgba(175,255,131,0.2);");
                }
            }
        });
    }


}