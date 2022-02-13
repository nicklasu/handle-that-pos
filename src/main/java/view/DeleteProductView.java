package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class DeleteProductView {
    private MainApp mainApp;
    @FXML
    private TextField productBarcode;
    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
    }
    @FXML
    private void deleteProduct(){
        try {
            this.mainApp.getEngine().productDao().deleteProduct(productBarcode.getText());
        }catch (Exception e){
            System.out.println("There was an error");
            e.printStackTrace();
        }
    }
}
