package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.classes.PaymentMethod;
import model.classes.Product;

import java.io.IOException;
import java.util.ArrayList;

public class TransactionView {
    private MainApp mainApp;
    @FXML
    private AnchorPane transactionAnchorPane;
    @FXML
    private ListView<Product> scanListView;
    @FXML
    private ToggleButton cardToggleButton;
    @FXML
    private ToggleButton cashToggleButton;
    @FXML
    private CheckBox receiptCheckBox;
    @FXML
    private TextField receiptEmailTextField;
    private boolean printReceipt = false;
    private boolean sendReceiptEmail = false;
    private ToggleGroup paymentButtonGroup = new ToggleGroup();
    private ObservableList<Product> items = FXCollections.observableArrayList();

    @FXML
    public void loadMainView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        new ViewLoader(transactionAnchorPane, fxmlLoader.load());
        ((MainView) fxmlLoader.getController()).setMainApp(this.mainApp);
    }

    @FXML
    public void confirmPayment() throws IOException {
        this.mainApp.getEngine().confirmTransaction();
        loadMainView();
    }

    @FXML
    private void confirmReceipt() {
        if (receiptCheckBox.isSelected()) {
            printReceipt = true;
        } else {
            printReceipt = false;
        }
        System.out.println(printReceipt);
    }

    @FXML
    private void sendReceiptEmail() {
        if (receiptEmailTextField.getText() != "") {
            sendReceiptEmail = true;
            System.out.println(receiptEmailTextField.getText());
        } else {
            sendReceiptEmail = false;
        }
        System.out.println(sendReceiptEmail);
    }

    @FXML
    public void selectCash() {
        selectPaymentMethod(PaymentMethod.CASH, cashToggleButton, cardToggleButton);
    }

    @FXML
    public void selectCard() {
        selectPaymentMethod(PaymentMethod.CARD, cardToggleButton, cashToggleButton);
    }

    private void selectPaymentMethod(PaymentMethod paymentMethod, ToggleButton disabledButton, ToggleButton enabledButton) {
        if (this.mainApp.getEngine().getTransaction() != null) {
            this.mainApp.getEngine().getTransaction().setPaymentMethod(paymentMethod);
            System.out.println(this.mainApp.getEngine().getTransaction().getPaymentMethod());
        }
        disabledButton.setDisable(true);
        enabledButton.setDisable(false);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        cardToggleButton.setToggleGroup(paymentButtonGroup);
        cashToggleButton.setToggleGroup(paymentButtonGroup);
        if (this.mainApp.getEngine().getTransaction() != null) {
            ArrayList<Product> products = this.mainApp.getEngine().getTransaction().getOrder().getProductList();
            items.addAll(products);
        }
        scanListView.setItems(items);
    }
}
