package view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.classes.Product;
import org.controlsfx.control.Notifications;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainView {
    private MainApp mainApp;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private ListView<Product> scanListView;
    @FXML
    private TextField barcodeTextField;
    @FXML
    private Button hotkeyButton0;
    @FXML
    private Button hotkeyButton1;
    @FXML
    private Button hotkeyButton2;
    @FXML
    private Button hotkeyButton3;
    @FXML
    private Button hotkeyButton4;
    @FXML
    private Button hotkeyButton5;
    @FXML
    private Button hotkeyButton6;
    @FXML
    private Button hotkeyButton7;
    @FXML
    private Button hotkeyButton8;
    @FXML
    private Button paymentButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button settingsBtn;
    @FXML
    private ProgressBar feedbackProgressBar;
    final private ObservableList<Product> items = FXCollections.observableArrayList();
    private String productId;
    private final String[] hotkeyProductIds = new String[9];
    private final ArrayList<Button> hotkeyButtons = new ArrayList<>();
    private HotkeyFileHandler hotkeyFileHandler;
    private ResourceBundle bundle;


    public void loadTransactionView() {
        mainApp.showTransactionView();
    }

    public void loadOptionsView() {
        mainApp.showOptionsView();
    }

    @FXML
    private void readBarcode() {
        productId = barcodeTextField.getText();
        addProduct(productId);
        barcodeTextField.clear();
        barcodeTextField.requestFocus();
    }

    private void addProduct(String productId) {
        Product product = this.mainApp.getEngine().scanProduct(productId);
        if (product != null) {
            if (!items.contains(product)) {
                items.add(product);
            }
            scanListView.refresh();
            setTotalPrice();
            for (Product p : this.mainApp.getEngine().getTransaction().getOrder().getProductList()) {
                if (p.equals(product)) {
                    if (p.getStock() < 0) {
                        negativeProductStockNotification();
                    }
                    break;
                }
            }
        } else {
            Notifications.create()
                    .owner(barcodeTextField.getScene().getWindow())
                    .title(bundle.getString("errorString"))
                    .text(bundle.getString("productNotFoundString"))
                    .position(Pos.TOP_RIGHT)
                    .showError();
        }
        barcodeTextField.requestFocus();
    }

    public void setTotalPrice() {
        if (!(this.mainApp.getEngine().getTransaction() == null)) {
            String priceInEuros = String.format("%.2f", (this.mainApp.getEngine().getTransaction().getOrder().getTotalPrice() / 100f)) + "€";
            this.totalPriceLabel.setText(priceInEuros);
        } else {
            this.totalPriceLabel.setText("0.00€");
        }
    }

    @FXML
    private void handleLogoutButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, bundle.getString("logoutConfirmationString"), ButtonType.YES, ButtonType.NO);
        alert.setTitle(bundle.getString("confirmationString"));
        alert.setHeaderText(bundle.getString("confirmationString"));
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            this.mainApp.getEngine().logout();
            this.mainApp.getStage().setTitle(this.mainApp.APP_TITLE);
            this.mainApp.showLoginView();
        }
    }

    private void addHotkeys(ArrayList<Button> hotkeys) {
        for (int i = 0; i < hotkeys.size(); i++) {
            setHotkeyButton(hotkeys.get(i));
            if (Objects.equals(hotkeyProductIds[i], "null") || Objects.equals(hotkeyProductIds[i], "") || Objects.equals(hotkeyProductIds[i], null)) {
                Button hotkey = hotkeys.get(i);
                hotkey.setText(bundle.getString("hotkeyNotSetString"));
                hotkey.setStyle("-fx-background-color: red;");
                hotkey.setOnMouseEntered(mouseEvent -> hotkey.setStyle("-fx-background-color: darkred;"));
                hotkey.setOnMouseExited(mouseEvent -> hotkey.setStyle("-fx-background-color: red;"));
            } else {
                hotkeys.get(i).setText(mainApp.getHotkeyButtonNames()[i]);
            }
        }
    }

    /**
     * Takes existing hotkey information and uses it by adding desired products to listview and order.
     * Saves new hotkey information if button pressed for 2 sec or more.
     */
    private void setHotkeyButton(Button button) {
        int buttonId = Integer.parseInt(button.getId().replace("hotkeyButton", ""));
        button.addEventFilter(MouseEvent.ANY, new EventHandler<>() {
            long startTime;
            final AtomicBoolean running = new AtomicBoolean(false);

            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                    feedbackProgressBar.setVisible(true);
                    startTime = System.currentTimeMillis();
                    Thread thread = new Thread(() -> {
                        running.set(true);
                        while (running.get()) {
                            feedbackProgressBar.setProgress(Long.valueOf(System.currentTimeMillis() - startTime).doubleValue() / 2000);
                        }
                    });
                    thread.start();
                } else if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                    if (System.currentTimeMillis() - startTime > 2000) {
                        if (productId != null) {
                            TextInputDialog tid = new TextInputDialog("");
                            tid.setTitle(bundle.getString("hotkeySettingTitleString"));
                            tid.setHeaderText(bundle.getString("hotkeySettingDefaultNameString") + " " + items.get(items.size() - 1).getName() + ".\n" + bundle.getString("hotkeySettingRenameString"));
                            tid.setContentText(bundle.getString("hotkeySettingNewNameString"));
                            Optional<String> result = tid.showAndWait();
                            result.ifPresent(e -> {
                                TextField inputField = tid.getEditor();
                                hotkeyProductIds[buttonId] = productId;
                                hotkeyButtons.get(buttonId).setStyle(paymentButton.getStyle());
                                Button hotkey = hotkeyButtons.get(buttonId);
                                hotkey.setOnMouseEntered(mouseEvent -> hotkey.setStyle(paymentButton.getStyle()));
                                hotkey.setOnMouseExited(mouseEvent -> hotkey.setStyle(paymentButton.getStyle()));
                                if (!Objects.equals(inputField.getText(), "")) {
                                    mainApp.setHotkeyButtonName(inputField.getText(), buttonId);
                                    hotkeyButtons.get(buttonId).setText(inputField.getText());
                                } else {
                                    mainApp.setHotkeyButtonName(items.get(items.size() - 1).getName(), buttonId);
                                    hotkeyButtons.get(buttonId).setText(items.get(items.size() - 1).getName());
                                }
                                hotkeyFileHandler.saveHotkeys(hotkeyProductIds, mainApp.getHotkeyButtonNames());
                            });
                        } else {
                            Notifications.create()
                                    .owner(mainAnchorPane.getScene().getWindow())
                                    .title(bundle.getString("notificationString"))
                                    .text(bundle.getString("hotkeyNoScanString"))
                                    .position(Pos.TOP_RIGHT)
                                    .show();
                        }
                    } else {
                        try {
                            if (!Objects.equals(hotkeyProductIds[buttonId], "null")) {
                                addProduct(hotkeyProductIds[buttonId]);
                            } else {
                                Notifications.create()
                                        .owner(mainAnchorPane.getScene().getWindow())
                                        .title(bundle.getString("notificationString"))
                                        .text(bundle.getString("hotkeyNotSetString"))
                                        .position(Pos.TOP_RIGHT)
                                        .show();
                            }
                        } catch (Exception e) {
                            Notifications.create()
                                    .owner(mainAnchorPane.getScene().getWindow())
                                    .title(bundle.getString("notificationString"))
                                    .text(bundle.getString("hotkeyNotSetString"))
                                    .position(Pos.TOP_RIGHT)
                                    .show();
                        }
                    }
                    running.set(false);
                    startTime = 0;
                    feedbackProgressBar.setProgress(0);
                    feedbackProgressBar.setVisible(false);
                    barcodeTextField.requestFocus();
                }
            }
        });
    }

    @FXML
    public void showHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, bundle.getString("mainViewHelpString"), ButtonType.CLOSE);
        alert.setTitle(bundle.getString("helpString"));
        alert.setHeaderText(bundle.getString("helpString"));
        alert.showAndWait();
        barcodeTextField.requestFocus();
    }

    public void negativeProductStockNotification() {
        Notifications.create()
                .owner(mainAnchorPane.getScene().getWindow())
                .title(bundle.getString("notificationString"))
                .text(bundle.getString("lowProductQuantityString"))
                .position(Pos.TOP_RIGHT)
                .show();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        bundle = mainApp.getBundle();
        List<Integer> privilegesOfUser = this.mainApp.getEngine().getVerifiedPrivileges();
        if (privilegesOfUser.isEmpty() || Collections.max(privilegesOfUser) < 1) {
            logoutButton.setVisible(false);
            settingsButton.setVisible(false);
        }
        feedbackProgressBar.setVisible(false);
        mainApp.getStage().setTitle(mainApp.APP_TITLE + " - " + mainApp.getEngine().getUser().getUsername());
        hotkeyFileHandler = new HotkeyFileHandler(bundle);
        hotkeyFileHandler.loadHotkeys(hotkeyProductIds, mainApp.getHotkeyButtonNames());
        hotkeyButtons.add(hotkeyButton0);
        hotkeyButtons.add(hotkeyButton1);
        hotkeyButtons.add(hotkeyButton2);
        hotkeyButtons.add(hotkeyButton3);
        hotkeyButtons.add(hotkeyButton4);
        hotkeyButtons.add(hotkeyButton5);
        hotkeyButtons.add(hotkeyButton6);
        hotkeyButtons.add(hotkeyButton7);
        hotkeyButtons.add(hotkeyButton8);
        addHotkeys(hotkeyButtons);
        // Populate listView with already existing products from open Transaction
        if (this.mainApp.getEngine().getTransaction() != null) {
            try {
                List<Product> products = this.mainApp.getEngine().getTransaction().getOrder().getProductList();
                for (Product product : products) {
                    if (!items.contains(product)) {
                        items.add(product);
                    }
                }
            } catch (Exception e) {
                System.out.println("No products in order");
            }
        }
        scanListView.setItems(items);
        scanListView.setCellFactory(productListView -> new ProductListViewCell(this, this.mainApp.getEngine().getTransaction().getOrder(), this.items));
        setTotalPrice();
        //Pressing enter runs readBarcode()
        barcodeTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                readBarcode();
        });
        items.addListener((ListChangeListener<Product>) change -> setTotalPrice());
        barcodeTextField.requestFocus();
        BooleanBinding booleanBind = Bindings.size(items).isEqualTo(0);
        paymentButton.disableProperty().bind(booleanBind);


        if (privilegesOfUser.isEmpty() || Collections.max(privilegesOfUser) < 1) {
            settingsBtn.setVisible(false);
            logoutBtn.setVisible(false);
        }

    }
}
