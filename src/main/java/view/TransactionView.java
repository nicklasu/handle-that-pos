package view;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import model.classes.*;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TransactionView {
    private MainApp mainApp;
    @FXML
    private AnchorPane transactionAnchorPane;
    @FXML
    private Label transactionOverviewLabel;
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
    @FXML
    private TextField customerTextField;
    @FXML
    private CheckBox bonusCustomerCheckBox;
    @FXML
    private Label paymentMethodLabel;
    private boolean printReceipt = false;
    private boolean sendReceiptEmail = false;
    private final ToggleGroup paymentButtonGroup = new ToggleGroup();
    private final ObservableList<Product> items = FXCollections.observableArrayList();

    @FXML
    public void loadMainView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        new ViewLoader(transactionAnchorPane, fxmlLoader.load());
        ((MainView) fxmlLoader.getController()).setMainApp(this.mainApp);
    }

    @FXML
    private void confirmPayment() {
        try {
            if (!Objects.equals(customerTextField.getText(), "")) {
                CustomerDAO customerDAO = this.mainApp.getEngine().getCustomerDAO();
                if (customerDAO.getCustomer(Integer.parseInt(customerTextField.getText())) == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Asiakasta ei löytynyt!", ButtonType.CLOSE);
                    alert.showAndWait();
                } else {
                    this.mainApp.getEngine().confirmTransaction(printReceipt, customerDAO.getCustomer(Integer.parseInt(customerTextField.getText())));
                }
            } else {
                this.mainApp.getEngine().confirmTransaction(printReceipt, null);
            }
            loadMainView();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ei tuotteita tilauksessa!", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    @FXML
    private void confirmReceipt() {
        printReceipt = receiptCheckBox.isSelected();
        System.out.println(printReceipt);
    }

    @FXML
    private void sendReceiptEmail() {
        if (!receiptEmailTextField.getText().equals("")) {
            sendReceiptEmail = true;
            System.out.println(receiptEmailTextField.getText());
        } else {
            sendReceiptEmail = false;
        }
        System.out.println(sendReceiptEmail);
    }

    @FXML
    private void selectCash() {
        selectPaymentMethod(PaymentMethod.CASH, cashToggleButton, cardToggleButton);
    }

    @FXML
    private void selectCard() {
        selectPaymentMethod(PaymentMethod.CARD, cardToggleButton, cashToggleButton);
    }

    /**
     * If you want to test this method, add products to order beforehand.
     *
     * @param paymentMethod  PaymentMethod enum.
     * @param disabledButton Button to be disabled.
     * @param enabledButton  Button to be enabled.
     */
    private void selectPaymentMethod(PaymentMethod paymentMethod, ToggleButton disabledButton, ToggleButton enabledButton) {
        if (this.mainApp.getEngine().getTransaction() != null) {
            this.mainApp.getEngine().getTransaction().setPaymentMethod(paymentMethod);
            System.out.println(this.mainApp.getEngine().getTransaction().getPaymentMethod());
        }
        disabledButton.setDisable(true);
        enabledButton.setDisable(false);

        setPaymentMethodLabelText(paymentMethod);
    }

    @FXML
    private void requestFocus() {
        customerTextField.requestFocus();
    }

    private void setPaymentMethodLabelText(PaymentMethod paymentMethod) {
        if (paymentMethod == PaymentMethod.CARD) {
            paymentMethodLabel.setText("Maksukortti");
        } else if (paymentMethod == PaymentMethod.CASH) {
            paymentMethodLabel.setText("Käteinen");
        } else {
            paymentMethodLabel.setText("");
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        cardToggleButton.setToggleGroup(paymentButtonGroup);
        cashToggleButton.setToggleGroup(paymentButtonGroup);
        if (this.mainApp.getEngine().getTransaction() != null) {
            if (this.mainApp.getEngine().getTransaction().getPaymentMethod().ordinal() == 1) {
                cardToggleButton.setDisable(true);
            } else {
                cashToggleButton.setDisable(true);
            }
            List<Product> products = this.mainApp.getEngine().getTransaction().getOrder().getProductList();
            items.addAll(products);

            scanListView.setItems(items);
            scanListView.setCellFactory(productListView -> new ListCellTransaction());
            String overviewText = "Tilauksessa " + this.mainApp.getEngine().getTransaction().getOrder().getProductList().size() + " tuotetta hintaan " + (String.format("%.2f", (this.mainApp.getEngine().getTransaction().getOrder().getTotalPrice() / 100f))) + "€";
            transactionOverviewLabel.setText(overviewText);
            setPaymentMethodLabelText(this.mainApp.getEngine().getTransaction().getPaymentMethod());
        }
       // scanListView.setItems(items);
        scanListView.setOnMouseClicked(event -> {
            try {
                Product product = scanListView.getSelectionModel().getSelectedItem();
                Dialog<Void> dialog = new Dialog<>();
                dialog.setTitle(product.getName());
                dialog.setHeaderText("ID: " + product.getId() + "\n" + "Kuvaus: " + product.getDescription() + "\nHinta: " + String.format("%.2f", (product.getPrice() / 100f)) + "€" + " per kpl" + "\nVarastomäärä: " + product.getStock());
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                dialog.showAndWait();
            }
            catch (Exception ignored){
            }
        });
        customerTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                requestFocus();
        });
        customerTextField.visibleProperty().bind(Bindings.createBooleanBinding(() -> bonusCustomerCheckBox.isSelected(), bonusCustomerCheckBox.selectedProperty()));
    }
}

