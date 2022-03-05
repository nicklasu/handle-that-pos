package view;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import model.classes.Privilege;
import model.classes.PrivilegeLevel;
import model.classes.User;
import org.controlsfx.control.Notifications;

import javax.persistence.PersistenceException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

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
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private ChoiceBox<String> privilegeLevel;

    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;

        BooleanBinding booleanBind = userFirstName.textProperty().isEmpty()
                .or(userLastName.textProperty().isEmpty())
                .or(userName.textProperty().isEmpty())
                .or(userPassword.textProperty().isEmpty());
        saveBtn.disableProperty().bind(booleanBind);
        ObservableList<String> availableChoices = FXCollections.observableArrayList("Myyjä", "Myymäläpäällikkö");
        privilegeLevel.setItems(availableChoices);
        privilegeLevel.setValue("Myyjä");
        startDate.setValue(LocalDate.now());
    }

    @FXML
    private void addUser() {
        try {
            String name = userFirstName.getText();
            String lastname = userLastName.getText();
            String username = userName.getText();
            String password = userPassword.getText();
            boolean isActive = activity.isSelected();
            LocalDate dateStart =  startDate.getValue();
            LocalDate dateEnd = endDate.getValue();
            PrivilegeLevel pLevel = switch(privilegeLevel.getValue()){
                case "Myyjä" -> PrivilegeLevel.USER;
                case "Myymäläpäällikkö" -> PrivilegeLevel.MANAGER;
                case "Järjestelmän ylläpitäjä" -> PrivilegeLevel.ADMIN;
                default -> throw new IllegalStateException("Unexpected value");
            };



            User user = new User(name, lastname, username, password, 1);
            Privilege privilege = new Privilege(user, java.sql.Date.valueOf(dateStart), dateEnd == null ? null : java.sql.Date.valueOf(dateEnd), pLevel);
            if (isActive) {
                this.mainApp.getEngine().addUser(user);
                this.mainApp.getEngine().privilegeDAO().addPrivilege(privilege);
            } else {
                user.setActivity(0);
                this.mainApp.getEngine().addUser(user);
                this.mainApp.getEngine().privilegeDAO().addPrivilege(privilege);
            }

            Notifications.create()
                    .owner(saveBtn.getScene().getWindow())
                    .title("Onnistui")
                    .text("Käyttäjä on luotu!")
                    .position(Pos.TOP_RIGHT)
                    .show();

        }catch(IllegalStateException p){
            System.out.println("Error! Username is taken!");
            Notifications.create()
                    .owner(saveBtn.getScene().getWindow())
                    .title("Virhe")
                    .text("Käyttäjänimi on varattu!")
                    .position(Pos.TOP_RIGHT)
                    .showError();
        } catch (Exception e) {
            System.out.println("There was an error");
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
