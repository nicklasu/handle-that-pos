package view;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.classes.User;

import java.io.IOException;

public class AddUserView {
    private MainApp mainApp;

    @FXML
    private TextField userFirstName;
    @FXML
    private TextField userLastName;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField userPassword;
    @FXML
    private CheckBox activity;
    @FXML
    private Button saveBtn;

    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;

        BooleanBinding booleanBind = userFirstName.textProperty().isEmpty()
                .or(userLastName.textProperty().isEmpty())
                .or(userName.textProperty().isEmpty())
                .or(userPassword.textProperty().isEmpty());
        saveBtn.disableProperty().bind(booleanBind);

    }

    @FXML
    private void addUser() {
        try {
            String name = userFirstName.getText();
            String lastname = userLastName.getText();
            String username = userName.getText();
            String password = userPassword.getText();
            boolean isActive = activity.isSelected();

            User user = new User(name, lastname, username, password, 1);

            if (isActive) {
                this.mainApp.getEngine().addUser(user);
            } else {
                user.setActivity(0);
                this.mainApp.getEngine().addUser(user);
            }


        } catch (Exception e) {
            System.out.println("There was an error");
            e.printStackTrace();
        }
    }
}
