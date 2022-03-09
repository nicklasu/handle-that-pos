package view;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import model.classes.Transaction;
import model.classes.User;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;
import java.util.List;
import java.awt.*;
import java.io.IOException;

public class UsersView {
    private MainApp mainApp;
    @FXML
    private TextField searchField;
    @FXML
    private Text fName;
    @FXML
    private Text lName;
    @FXML
    private CheckBox activity;

    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        activity.setDisable(true);
        User user = this.mainApp.getEngine().getUser();
        fName.setText(user.getfName());
        lName.setText(user.getlName());
        if(user.getActivity() == 1){
            activity.setSelected(true);
        }
        else {
            activity.setSelected(false);
        }
    }

    @FXML
    private void searchUser(){
        try {
            System.out.println("searching");
            String username = searchField.getText();
            User user = this.mainApp.getEngine().userDAO().getUser(username);
            if (user != null) {
                fName.setText(user.getfName());
                lName.setText(user.getlName());
                if (user.getActivity() == 1) {
                    activity.setSelected(true);
                } else {
                    activity.setSelected(false);
                }
            }

            else {
                Notifications.create()
                        .owner(searchField.getScene().getWindow())
                        .title("Virhe")
                        .text("Käyttäjänimeä ei löydy!")
                        .position(Pos.TOP_RIGHT)
                        .showError();
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
