package view;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.classes.Product;
import org.controlsfx.control.Notifications;


/**
 * Represents the hardware running the software
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class AddProductController {
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
    private Button addBtn;

    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
        final BooleanBinding booleanBind = productBarcode.textProperty().isEmpty()
                .or(productName.textProperty().isEmpty())
                .or(productDesc.textProperty().isEmpty())
                .or(productPrice.textProperty().isEmpty())
                .or(productStock.textProperty().isEmpty());
        addBtn.disableProperty().bind(booleanBind);

        productPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                productPrice.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        productStock.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                productStock.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

    }

    @FXML
    private void saveProduct() {

        try {

            final String barcode = productBarcode.getText();
            final String name = productName.getText();
            final String desc = productDesc.getText();
            final int price = Integer.parseInt(productPrice.getText());
            final int stock = Integer.parseInt(productStock.getText());
            final Product product = new Product(barcode, name, desc, price, stock);
            final boolean res = this.mainApp.getEngine().productDao().addProduct(product);
            if (res) {
                Notifications.create()
                        .owner(productBarcode.getScene().getWindow())
                        .title(this.mainApp.getBundle().getString("success"))
                        .text(this.mainApp.getBundle().getString("productAddedToDatabase"))
                        .position(Pos.TOP_RIGHT)
                        .show();
            } else {
                Notifications.create()
                        .owner(productBarcode.getScene().getWindow())
                        .title(this.mainApp.getBundle().getString("errorString"))
                        .text(this.mainApp.getBundle().getString("productAlreadyExists"))
                        .position(Pos.TOP_RIGHT)
                        .showError();
            }

        } catch (final Exception e) {
            System.out.println("There was an error");
            e.printStackTrace();
            Notifications.create()
                    .owner(productBarcode.getScene().getWindow())
                    .title(this.mainApp.getBundle().getString("errorString"))
                    .text(this.mainApp.getBundle().getString("checkFilledFields"))
                    .position(Pos.TOP_RIGHT)
                    .showError();
        }
    }

}
