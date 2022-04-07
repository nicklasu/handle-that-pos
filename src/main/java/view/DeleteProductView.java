package view;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import model.classes.Product;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.util.Optional;

public class DeleteProductView {
    private MainApp mainApp;

    @FXML
    private TextField productBarcode;
    @FXML
    private Button deleteBtn;

    public void setMainApp(final MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        productBarcode.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                deleteProduct();
        });
        final BooleanBinding booleanBind = productBarcode.textProperty().isEmpty();
        deleteBtn.disableProperty().bind(booleanBind);
    }

    @FXML
    private void deleteProduct() {
        try {
            final Product product = this.mainApp.getEngine().productDao().getProduct(productBarcode.getText());
            if (product == null) {
                System.out.println("Ei löytynyt");
                Notifications.create()
                        .owner(productBarcode.getScene().getWindow())
                        .title("Virhe")
                        .text("Tuotetta ei löytynyt!")
                        .position(Pos.TOP_RIGHT)
                        .showError();
            }
            final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Vahvistus");
            alert.setHeaderText("Vahvistus");
            alert.setContentText(
                    "Poistetaanko varmasti?\n" + "Tuote: " + product.getName() + "\nKuvaus: " + product.getName());

            final Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                final boolean res = this.mainApp.getEngine().productDao().deleteProduct(productBarcode.getText());
                System.out.println(res);
                alert.close();
                if (res) {
                    System.out.println("Löytyi");
                    Notifications.create()
                            .owner(productBarcode.getScene().getWindow())
                            .title("Onnistui")
                            .text("Tuote poistettiin onnistuneesti!")
                            .position(Pos.TOP_RIGHT)
                            .show();

                } else {
                    System.out.println("Ei löytynyt");
                    Notifications.create()
                            .owner(productBarcode.getScene().getWindow())
                            .title("Virhe")
                            .text("Tuotetta ei löytynyt!")
                            .position(Pos.TOP_RIGHT)
                            .showError();

                }
            }

        } catch (final Exception e) {
            System.out.println("There was an error");
            e.printStackTrace();
        }
    }
}
