package view;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.classes.Product;

import java.io.IOException;

public class EditProductView {
    private MainApp mainApp;
    @FXML
    private TextField productBarcode;
    @FXML
    private TextField productName;
    @FXML
    private TextField productDesc;
    @FXML
    private TextField productPrice;
    @FXML
    private TextField productStock;
    @FXML
    private Button editBtn;

    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        BooleanBinding booleanBind = productBarcode.textProperty().isEmpty()
                .or(productName.textProperty().isEmpty())
                .or(productDesc.textProperty().isEmpty())
                .or(productPrice.textProperty().isEmpty())
                .or(productStock.textProperty().isEmpty());
        editBtn.disableProperty().bind(booleanBind);
    }

    @FXML
    private void editProduct() {
        try {
            String barcode = productBarcode.getText();
            String name = productName.getText();
            String desc = productDesc.getText();
            int price = Integer.parseInt(productPrice.getText());
            int stock = Integer.parseInt(productStock.getText());
            Product product = new Product(barcode, name, desc, price, stock);
            this.mainApp.getEngine().productDao().updateProduct(product);
        } catch (Exception e) {
            System.out.println("There was an error");
            e.printStackTrace();
        }
    }
}
