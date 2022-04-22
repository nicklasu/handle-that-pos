package view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.DAOs.CustomerDAO;
import model.classes.*;
import org.controlsfx.control.Notifications;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents the hardware running the software
 *
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 * Samu Luoma
 */
public class TransactionController {
    public static final String ERROR_STRING = "errorString";
    public static final String CUSTOMERNOTFOUND = "customernotfound";
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
    @FXML
    private ProgressBar feedbackProgressBar;
    @FXML
    private Text insertCustomerText;
    @FXML
    private Label bonusCustomerLabel;
    private boolean printReceipt = false;
    private boolean sendReceiptEmail = false;
    private final ToggleGroup paymentButtonGroup = new ToggleGroup();
    private final ObservableList<Product> items = FXCollections.observableArrayList();
    private CustomerDAO customerDAO = null;
    private volatile AtomicBoolean customerKeyPressed = new AtomicBoolean(false);

    @FXML
    private void loadMainView() {
        this.mainApp.showMainView();
    }

    @FXML
    private void confirmPayment() {
        try {
            if (this.mainApp.getEngine().getTransaction().getCustomer() == null && bonusCustomerCheckBox.isSelected()) {
                if (customerDAO.getCustomer(Integer.parseInt(customerTextField.getText())) != null) {
                    this.mainApp.getEngine().getTransaction()
                            .setCustomer(customerDAO.getCustomer(Integer.parseInt(customerTextField.getText())));
                    this.mainApp.getEngine().confirmTransaction(printReceipt);
                    this.mainApp.showMainView();
                } else {
                    Notifications.create()
                            .owner(transactionAnchorPane.getScene().getWindow())
                            .title(this.mainApp.getBundle().getString(ERROR_STRING))
                            .text(this.mainApp.getBundle().getString(CUSTOMERNOTFOUND))
                            .position(Pos.TOP_RIGHT)
                            .showError();
                }
            } else {
                this.mainApp.getEngine().confirmTransaction(printReceipt);
                this.mainApp.showMainView();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            final Alert alert = new Alert(Alert.AlertType.ERROR,
                    this.mainApp.getBundle().getString("noproductsinorder"), ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    @FXML
    private void confirmReceipt() {
        printReceipt = receiptCheckBox.isSelected();
        System.out.println(printReceipt);
    }

    private String generateOverviewText() {
        return MessageFormat.format(
                this.mainApp.getBundle().getString("transactionoverviewtext") + CurrencyHandler.getCurrency(),
                this.mainApp.getEngine().getTransaction().getOrder().getProductList().size(),
                (String.format("%.2f", (this.mainApp.getEngine().getTransaction().getOrder().getTotalPrice() / 100f))));
    }

    private String generateProductInfoText(final Product product) {
        return MessageFormat.format(this.mainApp.getBundle().getString("productinfo"), product.getId(),
                product.getDescription(), String.format("%.2f", (product.getPrice() / 100f)), product.getStock());
    }

    @FXML
    private void bonusCustomerCheck() {
        if (!Objects.equals(customerTextField.getText(), "") && !customerKeyPressed.get()) {
            customerKeyPressed.set(true);
            final AtomicBoolean running = new AtomicBoolean(false);
            final long startTime = System.currentTimeMillis();
            feedbackProgressBar.setVisible(true);
            final Thread thread = new Thread(() -> {
                running.set(true);
                while (running.get()) {
                    feedbackProgressBar
                            .setProgress((System.currentTimeMillis() - startTime) / 2000.0);
                    try {
                        Thread.sleep(100);
                        if ((System.currentTimeMillis() - startTime) >= 2000) {
                            Platform.runLater(() -> {
                                if (this.mainApp.getEngine().getTransaction() != null) {
                                    try {
                                        if (customerDAO.getCustomer(
                                                Integer.parseInt(customerTextField.getText())) != null) {
                                            this.mainApp.getEngine().getTransaction().setCustomer(customerDAO
                                                    .getCustomer(Integer.parseInt(customerTextField.getText())));
                                            transactionOverviewLabel.setText(generateOverviewText());
                                            bonusCustomerLabel.setText(customerTextField.getText());
                                            customerTextField.setDisable(true);
                                            Notifications.create()
                                                    .owner(transactionAnchorPane.getScene().getWindow())
                                                    .title(this.mainApp.getBundle().getString("info"))
                                                    .text(this.mainApp.getBundle().getString("customerfound"))
                                                    .position(Pos.TOP_RIGHT)
                                                    .showConfirm();
                                        } else {
                                            Notifications.create()
                                                    .owner(transactionAnchorPane.getScene().getWindow())
                                                    .title(this.mainApp.getBundle().getString(ERROR_STRING))
                                                    .text(this.mainApp.getBundle().getString(CUSTOMERNOTFOUND))
                                                    .position(Pos.TOP_RIGHT)
                                                    .showError();
                                        }

                                    } catch (final NumberFormatException e) {
                                        Notifications.create()
                                                .owner(transactionAnchorPane.getScene().getWindow())
                                                .title(this.mainApp.getBundle().getString(ERROR_STRING))
                                                .text(this.mainApp.getBundle().getString(CUSTOMERNOTFOUND))
                                                .position(Pos.TOP_RIGHT)
                                                .showError();
                                    }
                                }
                                Thread.currentThread().interrupt();
                            });
                            running.set(false);
                            customerKeyPressed.set(false);
                            feedbackProgressBar.setVisible(false);
                        }
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
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
    private void selectPaymentMethod(final PaymentMethod paymentMethod, final ToggleButton disabledButton,
                                     final ToggleButton enabledButton) {
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

    private void setPaymentMethodLabelText(final PaymentMethod paymentMethod) {
        if (paymentMethod == PaymentMethod.CARD) {
            paymentMethodLabel.setText(this.mainApp.getBundle().getString("creditcard"));
        } else if (paymentMethod == PaymentMethod.CASH) {
            paymentMethodLabel.setText(this.mainApp.getBundle().getString("cash"));
        } else {
            paymentMethodLabel.setText("");
        }
    }

    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
        customerDAO = this.mainApp.getEngine().customerDAO();
        cardToggleButton.setToggleGroup(paymentButtonGroup);
        cashToggleButton.setToggleGroup(paymentButtonGroup);
        if (this.mainApp.getEngine().getTransaction() != null) {
            if (this.mainApp.getEngine().getTransaction().getPaymentMethod().ordinal() == 1) {
                cardToggleButton.setDisable(true);
            } else {
                cashToggleButton.setDisable(true);
            }
            final List<Product> products = this.mainApp.getEngine().getTransaction().getOrder().getProductList();
            items.addAll(products);

            scanListView.setItems(items);
            scanListView.setCellFactory(productListView -> new ListCellTransaction());
            transactionOverviewLabel.setText(generateOverviewText());
            setPaymentMethodLabelText(this.mainApp.getEngine().getTransaction().getPaymentMethod());
        }
        // scanListView.setItems(items);
        scanListView.setOnMouseClicked(event -> {
            try {
                final Product product = scanListView.getSelectionModel().getSelectedItem();
                final Dialog<Void> dialog = new Dialog<>();
                dialog.setTitle(product.getName());
                dialog.setHeaderText(generateProductInfoText(product));
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                dialog.showAndWait();
            } catch (final Exception ignored) {
            }
        });
        feedbackProgressBar.setVisible(false);
        customerTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                requestFocus();
        });
        customerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*"))
                return;
            customerTextField.setText(newValue.replaceAll("[^\\d]", ""));
        });
        customerTextField.visibleProperty().bind(Bindings.createBooleanBinding(bonusCustomerCheckBox::isSelected,
                bonusCustomerCheckBox.selectedProperty()));
        insertCustomerText.visibleProperty().bind(Bindings.createBooleanBinding(
                bonusCustomerCheckBox::isSelected, bonusCustomerCheckBox.selectedProperty()));
        try {
            bonusCustomerLabel.setText(String.valueOf(this.mainApp.getEngine().getTransaction().getCustomer().getId()));
            bonusCustomerCheckBox.setSelected(true);
        } catch (final Exception ignored) {

        }
    }

    @FXML
    public void showHelp() {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION,
                this.mainApp.getBundle().getString("transactionviewhelp"), ButtonType.CLOSE);
        alert.setTitle(this.mainApp.getBundle().getString("helpString"));
        alert.setHeaderText(this.mainApp.getBundle().getString("helpString"));
        alert.showAndWait();
    }
}
