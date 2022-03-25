package view;

import javafx.beans.binding.Bindings;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
    private Button editBtn;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private ChoiceBox<String> privilegeLevel;
    @FXML
    private ListView privilegeListView;

    private ObservableList<Privilege> privilegeList = FXCollections.observableArrayList();

    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;

        BooleanBinding booleanBind = userFirstName.textProperty().isEmpty()
                .or(userLastName.textProperty().isEmpty())
                .or(userName.textProperty().isEmpty())
                .or(userPassword.textProperty().isEmpty());
        saveBtn.disableProperty().bind(booleanBind);
        privilegeListView.disableProperty().bind(Bindings.isEmpty(privilegeList));
        List<Integer> privilegeInts = this.mainApp.getEngine().getVerifiedPrivileges();
        EditUserView.checkPrivilegeLevel(privilegeInts, privilegeLevel, startDate);

        privilegeListView.setOnMouseClicked((click) -> {
            if (click.getButton() == MouseButton.SECONDARY){
                int index = privilegeListView.getSelectionModel().getSelectedIndex();
                System.out.println(index);
                if(index >= 0) {
                    privilegeList.remove(index);
                }
            }
            else if(click.getButton() == MouseButton.PRIMARY) {
                if (click.getClickCount() == 2) {
                    int index = privilegeListView.getSelectionModel().getSelectedIndex();
                    if(index >= 0) {
                    saveBtn.setVisible(false);
                    editBtn.setVisible(true);


                        Privilege p = privilegeList.get(index);
                        startDate.setValue(p.getPrivilegeStart().toLocalDate());
                        if (p.getPrivilegeEnd() != null) {
                            endDate.setValue(p.getPrivilegeEnd().toLocalDate());
                        }

                        String pLevel = switch (p.getPrivilegeLevelIndex()) {
                            case 0 -> "Myyjä";
                            case 1 -> "Myymäläpäällikkö";
                            case 2 -> "Järjestelmän ylläpitäjä";
                            default -> throw new IllegalStateException("Unexpected value");
                        };
                        privilegeLevel.setValue(pLevel);
                    }

                    editBtn.setOnAction((action) -> {
                        Privilege priv = privilegeList.get(privilegeListView.getSelectionModel().getSelectedIndex());
                        LocalDate dateStart = startDate.getValue();
                        priv.setPrivilegeStart(java.sql.Date.valueOf(dateStart));
                        LocalDate dateEnd = endDate.getValue();
                        if (dateEnd != null) {
                            priv.setPrivilegeEnd(java.sql.Date.valueOf(dateEnd));
                        }
                        String priviLevel = privilegeLevel.getValue();
                        PrivilegeLevel privilegeLvl = switch (priviLevel) {
                            case "Myyjä" -> PrivilegeLevel.USER;
                            case "Myymäläpäällikkö" -> PrivilegeLevel.MANAGER;
                            case "Järjestelmän ylläpitäjä" -> PrivilegeLevel.ADMIN;
                            default -> throw new IllegalStateException("Unexpected value");
                        };
                        priv.setPrivilegeLevel(privilegeLvl);
                        editBtn.setVisible(false);
                        saveBtn.setVisible(true);
                        privilegeListView.refresh();
                    });
                }
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

            User user = new User(name, lastname, username, password, 1);
            for (Privilege p : privilegeList){
                p.setUser(user);
            }
            if (isActive) {
                this.mainApp.getEngine().addUser(user);
                this.mainApp.getEngine().privilegeDAO().addPrivileges(privilegeList);
            } else {
                user.setActivity(0);
                this.mainApp.getEngine().addUser(user);
                this.mainApp.getEngine().privilegeDAO().addPrivileges(privilegeList);
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
            e.printStackTrace();
        }
    }
    @FXML
    private void addToListView(){
        LocalDate dateStart = startDate.getValue();
        LocalDate dateEnd = endDate.getValue();
        String pLevel = privilegeLevel.getValue();
        PrivilegeLevel privilegeLvl = switch (pLevel) {
            case "Myyjä" -> PrivilegeLevel.USER;
            case "Myymäläpäällikkö" -> PrivilegeLevel.MANAGER;
            case "Järjestelmän ylläpitäjä" -> PrivilegeLevel.ADMIN;
            default -> throw new IllegalStateException("Unexpected value");
        };
        Privilege privilege = new Privilege(java.sql.Date.valueOf(dateStart), dateEnd == null ? null : java.sql.Date.valueOf(dateEnd), privilegeLvl);
        privilegeList.add(privilege);
        privilegeListView.setItems(privilegeList);
    }
}
