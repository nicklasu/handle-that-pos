package view;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class EditFirmInfoView {
    private MainApp mainApp;
    @FXML
    private TextField firmNameTF;
    @FXML
    private TextField phoneNumberTF;
    @FXML
    private TextField addressTF;
    @FXML
    private Button saveBtn;

    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
    }

    @FXML
    private void editInfo() {
        String name = firmNameTF.getText();
        String phonenumber = phoneNumberTF.getText();
        String address = addressTF.getText();
    }

    @FXML
    private void updateInfo() {
        firmNameTF.clear();
        phoneNumberTF.clear();
        addressTF.clear();
    }
}
