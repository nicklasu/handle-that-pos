package view;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import model.classes.Product;
import org.controlsfx.control.Notifications;

import java.util.Optional;

/**
 * Controller for delete-product-view.fxml.
 *
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 * Samu Luoma
 */
public class DeleteProductController {
    private MainApp mainApp;
    @FXML
    private TextField productBarcode;
    @FXML
    private Button deleteBtn;

    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
        productBarcode.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) deleteProduct();
        });
        final BooleanBinding booleanBind = productBarcode.textProperty().isEmpty();
        deleteBtn.disableProperty().bind(booleanBind);
    }

    /**
     * Delete product from the database.
     * Called with the "Delete" -button from delete-product-view.fxml.
     */
    @FXML
    private void deleteProduct() {
        try {
            final Product product = this.mainApp.getEngine().productDao().getProduct(productBarcode.getText());
            if (product == null) {
                Notifications.create().owner(productBarcode.getScene().getWindow()).title(this.mainApp.getBundle().getString("errorString")).text(this.mainApp.getBundle().getString("productNotFoundString")).position(Pos.TOP_RIGHT).showError();
            }
            final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(this.mainApp.getBundle().getString("confirmationString"));
            alert.setHeaderText(this.mainApp.getBundle().getString("confirmationString")); //doYouWantoToDelete
            alert.setContentText(this.mainApp.getBundle().getString("doYouWantoToDelete") + "\n" + this.mainApp.getBundle().getString("product") + ": " + product.getName() + "\n" + this.mainApp.getBundle().getString("productDescription") + ": " + product.getName());
            final Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                final boolean res = this.mainApp.getEngine().productDao().deleteProduct(productBarcode.getText());
                alert.close();
                if (res) {
                    Notifications.create().owner(productBarcode.getScene().getWindow()).title(this.mainApp.getBundle().getString("success")).text(this.mainApp.getBundle().getString("productSuccessfullyDeleted")).position(Pos.TOP_RIGHT).show();
                } else {
                    Notifications.create().owner(productBarcode.getScene().getWindow()).title(this.mainApp.getBundle().getString("errorString")).text(this.mainApp.getBundle().getString("productNotFoundString")).position(Pos.TOP_RIGHT).showError();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
