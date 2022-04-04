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
import javafx.scene.text.Text;
import model.classes.CurrencyHandler;
import model.classes.Product;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.FileInputStream;
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
    private Button logoutBtn;
    @FXML
    private Button settingsBtn;
    @FXML
    private Label selfcheckoutlabel;
    @FXML
    private ProgressBar feedbackProgressBar;
    @FXML
    private ChoiceBox<String> languageBox;
    @FXML
    private Text languageText;
    private String currency;
    private ObservableList<String> languages = FXCollections.observableArrayList("fi", "en");
    final private ObservableList<Product> items = FXCollections.observableArrayList();
    private String productId;
    private final String[] hotkeyProductIds = new String[9];
    private final ArrayList<Button> hotkeyButtons = new ArrayList<>();
    private HotkeyFileHandler hotkeyFileHandler;
    private ResourceBundle bundle;
    private List<Integer> privilegesOfUser;


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


            String priceInEuros = String.format("%.2f", (this.mainApp.getEngine().getTransaction().getOrder().getTotalPrice() / 100f)) + CurrencyHandler.getCurrency();
            this.totalPriceLabel.setText(priceInEuros);


//            String priceInEuros = String.format("%.2f", (this.mainApp.getEngine().getTransaction().getOrder().getTotalPrice() / 100f)) + "€";
//            this.totalPriceLabel.setText(priceInEuros);
        } else {
            this.totalPriceLabel.setText("0.00" + CurrencyHandler.getCurrency());
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
                        if (Collections.max(privilegesOfUser) > 0) {
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
        //read from HandleThatPos.properties file and get currency propery
//        File file = new File("src/main/resources/HandleThatPos.properties");
//        Properties properties = new Properties();
//        try {
//            properties.load(new FileInputStream(file));
//            this.currency = properties.getProperty("currency");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        selfcheckoutlabel.setVisible(false);
        bundle = mainApp.getBundle();
        privilegesOfUser = this.mainApp.getEngine().getVerifiedPrivileges();
        if (privilegesOfUser.isEmpty() || Collections.max(privilegesOfUser) < 1) {
            languageBox.setItems(languages);
            languageBox.setOnAction(event -> {
                String lang = switch (languageBox.getValue()) {
                    case "fi" -> "fi_FI";
                    case "en" -> "en_US";
                    default -> throw new IllegalStateException("Unexpected value: " + languageBox.getValue());
                };
                try {
                    Locale locale = new Locale(lang.split("_")[0], lang.split("_")[1]);
                    Locale.setDefault(locale);
                    this.mainApp.setBundle(ResourceBundle.getBundle("TextResources", locale));
                    System.out.println(locale.getLanguage());
                    this.mainApp.showMainView();

                } catch (Exception ignored) {
                }
            });
            languageBox.setVisible(true);
            settingsBtn.setVisible(false);
            logoutBtn.setVisible(false);
            selfcheckoutlabel.setVisible(true);
            languageText.setVisible(true);
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
        //if barcodeTextField's length is greater than 8 characters it is trimmed to 8 characters
        barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 8) {
                barcodeTextField.setText(newValue.substring(0, 8));
            }
        });
        //check if selfcheckoutlabel is clicked 5 times in a row and if so, call handleLogoutButton() method
        selfcheckoutlabel.setOnMouseClicked(event -> {
            if (event.getClickCount() == 5) {
                handleLogoutButton();
            }
        });

    }
}
