package view;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import model.classes.Privilege;
import model.classes.PrivilegeLevel;
import model.classes.User;
import org.controlsfx.control.Notifications;

import javax.persistence.PersistenceException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

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
    @FXML
    private ListView privilegeListView;

    private ObservableList<PrivilegeItem> privilegeList = FXCollections.observableArrayList();

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
        privilegeListView.setOnMouseClicked(click -> {
            if (click.getButton() == MouseButton.SECONDARY){
                 privilegeList.remove(privilegeListView.getSelectionModel().getSelectedIndex());

            }
        });
    }

    @FXML
    private void addUser() {
        try {
            String name = userFirstName.getText();
            String lastname = userLastName.getText();
            String username = userName.getText();
            String password = userPassword.getText();
            boolean isActive = activity.isSelected();
            LocalDate dateStart = startDate.getValue();
            LocalDate dateEnd = endDate.getValue();
            PrivilegeLevel pLevel = switch (privilegeLevel.getValue()) {
                case "Myyjä" -> PrivilegeLevel.USER;
                case "Myymäläpäällikkö" -> PrivilegeLevel.MANAGER;
                case "Järjestelmän ylläpitäjä" -> PrivilegeLevel.ADMIN;
                default -> throw new IllegalStateException("Unexpected value");
            };


            User user = new User(name, lastname, username, password, 1);
//            Privilege privilege = new Privilege(user, java.sql.Date.valueOf(dateStart), dateEnd == null ? null : java.sql.Date.valueOf(dateEnd), pLevel);
           Privilege[] priv = new Privilege[privilegeList.size()];
            for (int i = 0; i<privilegeList.size(); i++){

                PrivilegeLevel privilegeleLvl = switch (privilegeList.get(i).getPrivilegeLevel()) {
                    case "Myyjä" -> PrivilegeLevel.USER;
                    case "Myymäläpäällikkö" -> PrivilegeLevel.MANAGER;
                    case "Järjestelmän ylläpitäjä" -> PrivilegeLevel.ADMIN;
                    default -> throw new IllegalStateException("Unexpected value");
                };
                Privilege privilege = new Privilege(user, java.sql.Date.valueOf(privilegeList.get(i).getStartDate()), privilegeList.get(i).getEndDate() == null ? null : java.sql.Date.valueOf(privilegeList.get(i).getEndDate()), privilegeleLvl);
                priv[i] = privilege;
            }
            if (isActive) {
                this.mainApp.getEngine().addUser(user);
                this.mainApp.getEngine().privilegeDAO().addPrivileges(priv);
            } else {
                user.setActivity(0);
                this.mainApp.getEngine().addUser(user);
                this.mainApp.getEngine().privilegeDAO().addPrivileges(priv);
            }

            Notifications.create()
                    .owner(saveBtn.getScene().getWindow())
                    .title("Onnistui")
                    .text("Käyttäjä on lisätty!")
                    .position(Pos.TOP_RIGHT)
                    .show();
            userFirstName.clear();
            userLastName.clear();
            userName.clear();
            userPassword.clear();
            privilegeList.clear();

        } catch (IllegalStateException p) {
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
    @FXML
    private void addToListView(){
        LocalDate dateStart = startDate.getValue();
        LocalDate dateEnd = endDate.getValue();
        String pLevel = privilegeLevel.getValue();
        PrivilegeItem item = new PrivilegeItem(dateStart, dateEnd == null ? null : dateEnd, pLevel);
        privilegeList.add(item);
        privilegeListView.setItems(privilegeList);
    }
}
