package view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.classes.Product;
import org.controlsfx.control.Notifications;

import java.io.IOException;

public class AddProductView {
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
    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        BooleanBinding booleanBind = productBarcode.textProperty().isEmpty()
                .or(productName.textProperty().isEmpty())
                .or(productDesc.textProperty().isEmpty())
                .or(productPrice.textProperty().isEmpty())
                .or(productStock.textProperty().isEmpty());
        addBtn.disableProperty().bind(booleanBind);


    }
    @FXML
    private void saveProduct(){

        try {

            String barcode = productBarcode.getText();
            String name = productName.getText();
            String desc = productDesc.getText();
            int price = Integer.parseInt(productPrice.getText());
            int stock = Integer.parseInt(productStock.getText());
            Product product = new Product(barcode, name, desc, price, stock);
            boolean res = this.mainApp.getEngine().productDao().addProduct(product);
            if(res){
                Notifications.create()
                        .owner(productBarcode.getScene().getWindow())
                        .title("Onnistui")
                        .text("Tuote lisättiin onnistuneesti tietokantaan!")
                        .position(Pos.TOP_RIGHT)
                        .show();
            }
            else {
                Notifications.create()
                        .owner(productBarcode.getScene().getWindow())
                        .title("Virhe")
                        .text("Kyseisellä viivakoodilla oleva tuote on jo tietokannassa!")
                        .position(Pos.TOP_RIGHT)
                        .showError();
            }


        }
        catch (Exception e){
            System.out.println("There was an error");
            e.printStackTrace();
            Notifications.create()
                    .owner(productBarcode.getScene().getWindow())
                    .title("Virhe")
                    .text("Tarkista syötetyt kentät!")
                    .position(Pos.TOP_RIGHT)
                    .showError();
        }
    }


}
