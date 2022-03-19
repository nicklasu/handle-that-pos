package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import model.classes.*;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @FXML
    private ListView<Transaction> transactionListView;

    final private ObservableList<Transaction> items = FXCollections.observableArrayList();

    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        searchField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                searchUser();
        });
        activity.setDisable(true);
        User user = this.mainApp.getEngine().getUser();
        fName.setText(user.getfName());
        lName.setText(user.getlName());
        setListViewItems(user);
        if (user.getActivity() == 1) {
            activity.setSelected(true);
        } else {
            activity.setSelected(false);
        }

        transactionListView.setOnMouseClicked(event -> {
            Transaction transaction = transactionListView.getSelectionModel().getSelectedItem();
            showTransactionDetails(transaction);
        });

        transactionListView.setCellFactory(param -> new ListCell<Transaction>() {
            @Override
            protected void updateItem(Transaction item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId() + ": " + item.getTimestamp());
                }
            }
        });
    }

    @FXML
    private void searchUser() {
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
                setListViewItems(user);
            } else {
                Notifications.create()
                        .owner(searchField.getScene().getWindow())
                        .title("Virhe")
                        .text("Käyttäjänimeä ei löydy!")
                        .position(Pos.TOP_RIGHT)
                        .showError();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListViewItems(User user) {
        List<Transaction> transactions = this.mainApp.getEngine().transactionDAO().getTransactions(user);
        this.items.addAll(transactions);
        this.transactionListView.setItems(this.items);
    }

    private void showTransactionDetails(Transaction transaction) {

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(Integer.toString(transaction.getId()));

        PaymentMethod paymentMethod;

        if (transaction.getPaymentMethodIndex() == 0) {
            paymentMethod = PaymentMethod.CASH;
        } else {
            paymentMethod = PaymentMethod.CARD;
        }

        String transactionInformation = "ID: " + transaction.getId() + "\n" +
                                        "Pvm ja kellonaika: " + transaction.getTimestamp() + "\n" +
                                        "Maksutapa: " + paymentMethod.name() + "\n" +
                                        "Myyjä: " + transaction.getUser().getFullName() + "\n" +
                                        "Maksupäätteen ID: " + transaction.getPos().getId() + "\n\n";

        String productsAndAmounts = "";
        Set<OrderProduct> ops = transaction.getOrder().getOrderProducts();
        for (OrderProduct op : ops) {
            productsAndAmounts += op.getProduct() + " x " + op.getAmount() + "\n";
        }

        dialog.setHeaderText(transactionInformation);
        dialog.setContentText(productsAndAmounts);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();

    }
}
