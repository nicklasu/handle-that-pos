package view;

import javafx.beans.binding.Bindings;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Represents the hardware running the software
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class AddUserView {
    public static final String USER = "user";
    public static final String MANAGER = "manager";
    public static final String ADMIN = "admin";
    public static final String SELF_CHECKOUT = "self_checkout";
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
    private Button saveBtn2;
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

    private final ObservableList<Privilege> privilegeList = FXCollections.observableArrayList();

    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;

        final BooleanBinding booleanBind = userFirstName.textProperty().isEmpty()
                .or(userLastName.textProperty().isEmpty())
                .or(userName.textProperty().isEmpty())
                .or(userPassword.textProperty().isEmpty());
        saveBtn2.disableProperty().bind(booleanBind);
        privilegeListView.disableProperty().bind(Bindings.isEmpty(privilegeList));
        final List<Integer> privilegeInts = this.mainApp.getEngine().getVerifiedPrivileges();
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

                    final String user = this.mainApp.getBundle().getString(USER);
                    final String manager = this.mainApp.getBundle().getString(MANAGER);
                    final String admin = this.mainApp.getBundle().getString(ADMIN);
                    final String self_checkout = this.mainApp.getBundle().getString(SELF_CHECKOUT);

                    privilegeSwitch(p, user, manager, admin, self_checkout, privilegeLevelChoiceBox);

                }

                editBtn.setOnAction(action -> {
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
                    if (pLevel.equals(this.mainApp.getBundle().getString(USER))) {
                        privilegeLvl = PrivilegeLevel.USER;
                    } else if (pLevel.equals(this.mainApp.getBundle().getString(MANAGER))) {
                        privilegeLvl = PrivilegeLevel.MANAGER;
                    } else if (pLevel.equals(this.mainApp.getBundle().getString(SELF_CHECKOUT))) {
                        privilegeLvl = PrivilegeLevel.SELF;
                    } else if (pLevel.equals(this.mainApp.getBundle().getString(ADMIN))) {
                        privilegeLvl = PrivilegeLevel.ADMIN;
                    }

                    priv.setPrivilegeLevel(privilegeLvl);
                    editBtn.setVisible(false);
                    saveBtn.setVisible(true);
                    privilegeListView.refresh();
                });
            }
        });

    }

    static void privilegeSwitch(Privilege p, String user, String manager, String admin, String self_checkout, ChoiceBox<String> privilegeLevelChoiceBox) {
        final String pLevel = switch (p.getPrivilegeLevelIndex()) {
            case 0 -> self_checkout;
            case 1 -> user;
            case 2 -> manager;
            case 3 -> admin;
            default -> throw new IllegalStateException("Unexpected value");
        };
        privilegeLevelChoiceBox.setValue(pLevel);
    }

    @FXML
    private void addUser() {
        try {
            final String name = userFirstName.getText();
            final String lastname = userLastName.getText();
            final String username = userName.getText();
            final String password = userPassword.getText();
            final boolean isActive = activity.isSelected();

            final User user = new User(name, lastname, username, password, 1);
            for (final Privilege p : privilegeList) {
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
                    .title(this.mainApp.getBundle().getString("success"))
                    .text(this.mainApp.getBundle().getString("user_added"))
                    .position(Pos.TOP_RIGHT)
                    .show();
            userFirstName.clear();
            userLastName.clear();
            userName.clear();
            userPassword.clear();
            privilegeList.clear();

        } catch (final IllegalStateException p) {
            System.out.println("Error! Username is taken!");
            Notifications.create()
                    .owner(saveBtn.getScene().getWindow())
                    .title(this.mainApp.getBundle().getString("errorString"))
                    .text(this.mainApp.getBundle().getString("username_taken"))
                    .position(Pos.TOP_RIGHT)
                    .showError();
        } catch (final Exception e) {
            System.out.println("There was an error");
            e.printStackTrace();
        }
    }

    @FXML
    private void addToListView() {
        final LocalDate dateStart = startDate.getValue();
        final LocalDate dateEnd = endDate.getValue();
        final String pLevel = privilegeLevelChoiceBox.getValue();
        PrivilegeLevel privilegeLvl = null;
        if (pLevel.equals(this.mainApp.getBundle().getString(USER))) {
            privilegeLvl = PrivilegeLevel.USER;
        } else if (pLevel.equals(this.mainApp.getBundle().getString(MANAGER))) {
            privilegeLvl = PrivilegeLevel.MANAGER;
        } else if (pLevel.equals(this.mainApp.getBundle().getString(SELF_CHECKOUT))) {
            privilegeLvl = PrivilegeLevel.SELF;
        } else if (pLevel.equals(this.mainApp.getBundle().getString(ADMIN))) {
            privilegeLvl = PrivilegeLevel.ADMIN;
        }
        final Privilege privilege = new Privilege(java.sql.Date.valueOf(dateStart),
                dateEnd == null ? null : java.sql.Date.valueOf(dateEnd), privilegeLvl);
        privilegeList.add(privilege);
        privilegeListView.setItems(privilegeList);
    }

    void checkPrivilegeLevel(final List<Integer> privilegeInts, final ChoiceBox<String> privilegeLevel,
            final DatePicker startDate) {
        ObservableList<String> availableChoices;
        if (Collections.max(privilegeInts) < 3) {
            availableChoices = FXCollections.observableArrayList(this.mainApp.getBundle().getString(USER),
                    this.mainApp.getBundle().getString(MANAGER), this.mainApp.getBundle().getString(SELF_CHECKOUT));
        } else {
            availableChoices = FXCollections.observableArrayList(this.mainApp.getBundle().getString(USER),
                    this.mainApp.getBundle().getString(MANAGER), this.mainApp.getBundle().getString(ADMIN),
                    this.mainApp.getBundle().getString(SELF_CHECKOUT));
        }
        privilegeLevel.setItems(availableChoices);
        privilegeLevel.setValue(this.mainApp.getBundle().getString(USER));
        startDate.setValue(LocalDate.now());
    }
}
