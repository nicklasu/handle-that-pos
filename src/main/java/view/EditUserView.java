package view;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import model.classes.Privilege;
import model.classes.PrivilegeLevel;

import model.classes.User;
import org.controlsfx.control.Notifications;

import java.io.IOException;

import java.time.LocalDate;

import java.util.Collections;
import java.util.List;

public class EditUserView {
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
    private ChoiceBox<String> privilegeLevelChoiceBox;
    @FXML
    private ListView<Privilege> privilegeListView;

    private List<Privilege> privileges;

    private User user = null;

    private final ObservableList<Privilege> privilegeList = FXCollections.observableArrayList();

    public void setMainApp(final MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        final BooleanBinding booleanBind = userFirstName.textProperty().isEmpty()
                .or(userLastName.textProperty().isEmpty())
                .or(userName.textProperty().isEmpty())
                .or(userPassword.textProperty().isEmpty());
        saveBtn.disableProperty().bind(booleanBind);
        final List<Integer> privilegeInts = this.mainApp.getEngine().getVerifiedPrivileges();
        System.out.println(privilegeInts);
        checkPrivilegeLevel(privilegeInts, privilegeLevelChoiceBox, startDate);
        privilegeListView.setOnMouseClicked(click -> {
            if (click.getButton() == MouseButton.SECONDARY) {
                final int index = privilegeListView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    privilegeList.remove(index);
                }
            } else if (click.getButton() == MouseButton.PRIMARY && click.getClickCount() == 2) {
                final int index = privilegeListView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    saveBtn.setVisible(false);
                    editBtn.setVisible(true);
                    final Privilege p = privilegeList.get(index);
                    startDate.setValue(p.getPrivilegeStart().toLocalDate());
                    if (p.getPrivilegeEnd() != null) {
                        endDate.setValue(p.getPrivilegeEnd().toLocalDate());
                    }
                    final String user1 = this.mainApp.getBundle().getString("user");
                    final String manager = this.mainApp.getBundle().getString("manager");
                    final String admin = this.mainApp.getBundle().getString("admin");
                    final String self_checkout = this.mainApp.getBundle().getString("self_checkout");

                    final String pLevel = switch (p.getPrivilegeLevelIndex()) {
                        case 0 -> self_checkout;
                        case 1 -> user1;
                        case 2 -> manager;
                        case 3 -> admin;
                        default -> throw new IllegalStateException("Unexpected value");
                    };
                    privilegeLevelChoiceBox.setValue(pLevel);

                }
                editBtnAction();
            }
        });

    }

    private void editBtnAction() {
        editBtn.setOnAction((action) -> {
            final Privilege priv = privilegeList
                    .get(privilegeListView.getSelectionModel().getSelectedIndex());
            final LocalDate dateStart = startDate.getValue();
            priv.setPrivilegeStart(java.sql.Date.valueOf(dateStart));
            final LocalDate dateEnd = endDate.getValue();
            if (dateEnd != null) {
                priv.setPrivilegeEnd(java.sql.Date.valueOf(dateEnd));
            }
            final String pLevel = privilegeLevelChoiceBox.getValue();
            PrivilegeLevel privilegeLvl = null;
            if (pLevel.equals(this.mainApp.getBundle().getString("user"))) {
                privilegeLvl = PrivilegeLevel.USER;
            } else if (pLevel.equals(this.mainApp.getBundle().getString("manager"))) {
                privilegeLvl = PrivilegeLevel.MANAGER;
            } else if (pLevel.equals(this.mainApp.getBundle().getString("self_checkout"))) {
                privilegeLvl = PrivilegeLevel.SELF;
            } else if (pLevel.equals(this.mainApp.getBundle().getString("admin"))) {
                privilegeLvl = PrivilegeLevel.ADMIN;
            }
            priv.setPrivilegeLevel(privilegeLvl);
            editBtn.setVisible(false);
            saveBtn.setVisible(true);
            privilegeListView.refresh();
        });
    }

    void checkPrivilegeLevel(final List<Integer> privilegeInts, final ChoiceBox<String> privilegeLevel,
            final DatePicker startDate) {
        ObservableList<String> availableChoices;
        if (Collections.max(privilegeInts) < 3) {
            availableChoices = FXCollections.observableArrayList(this.mainApp.getBundle().getString("user"),
                    this.mainApp.getBundle().getString("manager"), this.mainApp.getBundle().getString("self_checkout"));
        } else {
            availableChoices = FXCollections.observableArrayList(this.mainApp.getBundle().getString("user"),
                    this.mainApp.getBundle().getString("manager"), this.mainApp.getBundle().getString("admin"),
                    this.mainApp.getBundle().getString("self_checkout"));
        }
        privilegeLevel.setItems(availableChoices);
        privilegeLevel.setValue(this.mainApp.getBundle().getString("user"));
        startDate.setValue(LocalDate.now());
    }

    @FXML
    private void fillFields() {
        try {
            privilegeList.clear();
            this.user = this.mainApp.getEngine().userDAO().getUser(userName.getText());
            userName.setText(user.getUsername());
            userFirstName.setText(user.getfName());
            userLastName.setText(user.getlName());
            final boolean act = user.getActivity() == 0;
            activity.setSelected(act);

            privileges = this.mainApp.getEngine().privilegeDAO().getPrivileges(user);
            privilegeList.addAll(privileges);
            privilegeListView.setItems(privilegeList);

        } catch (final Exception e) {
            e.printStackTrace();
            Notifications.create()
                    .owner(userName.getScene().getWindow())
                    .title("Virhe")
                    .text("Käyttäjänimeä ei löytynyt!")
                    .position(Pos.TOP_RIGHT)
                    .showError();
        }
    }

    @FXML
    private void updateUser() {
        try {
            this.user = this.mainApp.getEngine().userDAO().getUser(userName.getText());

            if (activity.isSelected()) {
                this.user.setActivity(1);
            } else {
                this.user.setActivity(0);
            }
            final String name = userFirstName.getText();
            this.user.setfName(name);
            final String lastname = userLastName.getText();
            user.setlName(lastname);
            final String username = userName.getText();
            user.setUsername(username);
            final String password = userPassword.getText();
            user.setPassword(password);
            this.mainApp.getEngine().updateUser(user);

            for (int i = 0; i < privileges.size(); i++) {
                for (int j = 0; j < privilegeList.size(); j++) {
                    if (!privileges.isEmpty() && privileges.get(i).getId() == privilegeList.get(j).getId()) {
                        privileges.remove(i);
                    }
                }
            }

            if (!privileges.isEmpty()) {
                this.mainApp.getEngine().privilegeDAO().deletePrivileges(privileges);
            }

            this.mainApp.getEngine().privilegeDAO().updatePrivileges(privilegeList);
            userFirstName.clear();
            userLastName.clear();
            userName.clear();
            userPassword.clear();
            privilegeList.clear();
            Notifications.create()
                    .owner(userName.getScene().getWindow())
                    .title(this.mainApp.getBundle().getString("success"))
                    .text(this.mainApp.getBundle().getString("userChanged"))
                    .position(Pos.TOP_RIGHT)
                    .show();

        } catch (final Exception e) {
            e.printStackTrace();
            Notifications.create()
                    .owner(userName.getScene().getWindow())
                    .title(this.mainApp.getBundle().getString("errorString"))
                    .text(this.mainApp.getBundle().getString("userNotFound"))
                    .position(Pos.TOP_RIGHT)
                    .showError();
        }
    }

    @FXML
    private void addToListView() {
        final LocalDate dateStart = startDate.getValue();
        final LocalDate dateEnd = endDate.getValue();
        final String pLevel = privilegeLevelChoiceBox.getValue();
        PrivilegeLevel privilegeLvl = null;
        if (pLevel.equals(this.mainApp.getBundle().getString("user"))) {
            privilegeLvl = PrivilegeLevel.USER;
        } else if (pLevel.equals(this.mainApp.getBundle().getString("manager"))) {
            privilegeLvl = PrivilegeLevel.MANAGER;
        } else if (pLevel.equals(this.mainApp.getBundle().getString("self_checkout"))) {
            privilegeLvl = PrivilegeLevel.SELF;
        } else if (pLevel.equals(this.mainApp.getBundle().getString("admin"))) {
            privilegeLvl = PrivilegeLevel.ADMIN;
        }
        final Privilege privilege = new Privilege(this.user, java.sql.Date.valueOf(dateStart),
                dateEnd == null ? null : java.sql.Date.valueOf(dateEnd), privilegeLvl);
        privilegeList.add(privilege);
        privilegeListView.setItems(privilegeList);
    }
}
