package view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Vahvistus");
            alert.setHeaderText("Vahvistus");
            alert.setContentText("Poistetaanko varmasti?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                boolean res = this.mainApp.getEngine().productDao().deleteProduct(productBarcode.getText());
                alert.close();
                if(res == false){
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Virhe");
                    alert2.setHeaderText("Virhe");
                    alert2.setContentText("Tuotetta ei löytynyt");
                    alert2.showAndWait();
                }

            } else {
                alert.close();
            }


        }catch (Exception e){
            System.out.println("There was an error");
            e.printStackTrace();
        }
    }
}
