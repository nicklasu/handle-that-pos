package view;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import model.classes.Product;
import org.controlsfx.control.Notifications;

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
    @FXML
    private Button fetchBtn;

    /**
     * Represents the hardware running the software
     * 
     * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
     *         Samu Luoma
     */
    public void setMainApp(final MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        productBarcode.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                fillFields();
        });
        final BooleanBinding booleanBind = productBarcode.textProperty().isEmpty()
                .or(productName.textProperty().isEmpty())
                .or(productDesc.textProperty().isEmpty())
                .or(productPrice.textProperty().isEmpty())
                .or(productStock.textProperty().isEmpty());
        editBtn.disableProperty().bind(booleanBind);

        final BooleanBinding booleanBind2 = productBarcode.textProperty().isEmpty();
        fetchBtn.disableProperty().bind(booleanBind2);

        productPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                productPrice.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        productStock.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                productStock.setText(newValue.replaceAll("[^-?\\d+$]", ""));
            }
        });
    }

    @FXML
    private void editProduct() {
        try {
            final String barcode = productBarcode.getText();
            final String name = productName.getText();
            final String desc = productDesc.getText();
            final int price = Integer.parseInt(productPrice.getText());
            final int stock = Integer.parseInt(productStock.getText());
            final Product product = new Product(barcode, name, desc, price, stock);
            final boolean res = this.mainApp.getEngine().productDao().updateProduct(product);
            if (res) {
                Notifications.create()
                        .owner(productBarcode.getScene().getWindow())
                        .title("Onnistui")
                        .text("Tuote on muokattu!")
                        .position(Pos.TOP_RIGHT)
                        .show();
            }
        } catch (final Exception e) {
            System.out.println("There was an error");
            e.printStackTrace();
        }
    }

    @FXML
    private void fillFields() {
        try {
            final Product product = this.mainApp.getEngine().productDao().getProduct(productBarcode.getText());
            productName.setText(product.getName());
            productDesc.setText(product.getDescription());
            productPrice.setText(String.valueOf(product.getPrice()));
            productStock.setText(String.valueOf(product.getStock()));
        } catch (final Exception e) {
            System.out.println("There was an error");
            e.printStackTrace();
            Notifications.create()
                    .owner(productBarcode.getScene().getWindow())
                    .title("Virhe")
                    .text("Viivakoodilla ei löytynyt tuotetta!")
                    .position(Pos.TOP_RIGHT)
                    .showError();
        }
    }

    public void fillFieldsOnLoad(final Product product) {
        productBarcode.setText(product.getId());
        productName.setText(product.getName());
        productDesc.setText(product.getDescription());
        productPrice.setText(String.valueOf(product.getPrice()));
        productStock.setText(String.valueOf(product.getStock()));
    }
}
