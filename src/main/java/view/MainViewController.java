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
import javafx.scene.media.Media;
import javafx.scene.text.Text;
import model.classes.CurrencyHandler;
import model.classes.Product;
import org.controlsfx.control.Notifications;

import javafx.scene.media.MediaPlayer;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller for main-view.fxml
 *
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 * Samu Luoma
 */
public class MainViewController {
    public static final String HOTKEY_NOT_SET_STRING = "hotkeyNotSetString";
    public static final String NOTIFICATION_STRING = "notificationString";
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
    private Label howtouselabel;
    @FXML
    private ProgressBar feedbackProgressBar;
    @FXML
    private ChoiceBox<String> languageBox;
    @FXML
    private Text languageText;
    private final ObservableList<String> languages = FXCollections.observableArrayList("fi", "en");
    private final ObservableList<Product> items = FXCollections.observableArrayList();
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
        if (barcodeTextField.getText().length() > 0) {
            addProduct(productId);
        }
        barcodeTextField.clear();
        barcodeTextField.requestFocus();
    }

    /**
     * Adds product to the order
     * Runs setTotalPrice() if product added to the order successfully
     *
     * @param productId Takes a string of the productId (for example "12345678").
     */
    private void addProduct(final String productId) {
        final Product product = this.mainApp.getEngine().scanProduct(productId);
        if (product.getStock() <= -50) {
            Notifications.create()
                    .owner(barcodeTextField.getScene().getWindow())
                    .title(bundle.getString("errorString"))
                    .text(bundle.getString("productOutOfStockString"))
                    .position(Pos.TOP_RIGHT)
                    .showError();
            return;
        }
        if (product != null) {
            if (!items.contains(product)) {
                items.add(product);
            }
            scanListView.refresh();
            setTotalPrice();
            for (final Product p : this.mainApp.getEngine().getTransaction().getOrder().getProductList()) {
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
        beepSound();
    }

    /**
     * Takes the total price of the order, formats it to the correct price, and shows it in the view.
     */
    public void setTotalPrice() {
        if (this.mainApp.getEngine().getTransaction() != null) {
            final String formattedPrice = String.format("%.2f",
                    (this.mainApp.getEngine().getTransaction().getOrder().getTotalPrice() / 100f))
                    + CurrencyHandler.getCurrency();
            this.totalPriceLabel.setText(formattedPrice);
        } else {
            this.totalPriceLabel.setText("0.00" + CurrencyHandler.getCurrency());
        }
        barcodeTextField.requestFocus();
    }

    /**
     * Logs out of the system, when log out button is pressed.
     */
    @FXML
    private void handleLogoutButton() {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, bundle.getString("logoutConfirmationString"),
                ButtonType.YES, ButtonType.NO);
        alert.setTitle(bundle.getString("confirmationString"));
        alert.setHeaderText(bundle.getString("confirmationString"));
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            this.mainApp.getEngine().logout();
            this.mainApp.getStage().setTitle(MainApp.APP_TITLE);
            this.mainApp.showLoginView();
        }
    }

    /**
     * runs setHotkeyButton() for all the buttons.
     * Makes the buttons red and displays a message, if the hotkey is not set
     *
     * @param hotkeys Takes an ArrayList of buttons as parameter.
     */
    private void addHotkeys(final ArrayList<Button> hotkeys) {
        for (int i = 0; i < hotkeys.size(); i++) {
            setHotkeyButton(hotkeys.get(i));
            if (Objects.equals(hotkeyProductIds[i], "null") || Objects.equals(hotkeyProductIds[i], "")
                    || Objects.equals(hotkeyProductIds[i], null)) {
                final Button hotkey = hotkeys.get(i);
                hotkey.setText(bundle.getString(HOTKEY_NOT_SET_STRING));
                hotkey.setStyle("-fx-background-color: red;");
                hotkey.setOnMouseEntered(mouseEvent -> hotkey.setStyle("-fx-background-color: darkred;"));
                hotkey.setOnMouseExited(mouseEvent -> hotkey.setStyle("-fx-background-color: red;"));
            } else {
                hotkeys.get(i).setText(mainApp.getHotkeyButtonNames()[i]);
            }
        }
    }

    /**
     * Initializes hotkey buttons for use.
     * First, button gets an EventHandler for pressing the button.
     * Then a thread starts running, which runs the progress bar for setting the hotkey.
     * Saves new hotkey information if button pressed for 2 sec or more.
     * Otherwise, try to add product to order.
     */
    private void setHotkeyButton(final Button button) {
        final int buttonId = Integer.parseInt(button.getId().replace("hotkeyButton", ""));
        button.addEventFilter(MouseEvent.ANY, new EventHandler<>() {
            long startTime;
            final AtomicBoolean running = new AtomicBoolean(false);

            @Override
            public void handle(final MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                    feedbackProgressBar.setVisible(true);
                    startTime = System.currentTimeMillis();
                    final Thread thread = new Thread(() -> {
                        running.set(true);
                        while (running.get()) {
                            feedbackProgressBar.setProgress(
                                    (System.currentTimeMillis() - startTime) / 2000.0);
                        }
                    });
                    thread.start();
                } else if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                    if (System.currentTimeMillis() - startTime > 2000) {
                        if (Collections.max(privilegesOfUser) > 0) {
                            if (productId != null) {
                                final TextInputDialog tid = new TextInputDialog("");
                                tid.setTitle(bundle.getString("hotkeySettingTitleString"));
                                tid.setHeaderText(bundle.getString("hotkeySettingDefaultNameString") + " "
                                        + items.get(items.size() - 1).getName() + ".\n"
                                        + bundle.getString("hotkeySettingRenameString"));
                                tid.setContentText(bundle.getString("hotkeySettingNewNameString"));
                                final Optional<String> result = tid.showAndWait();
                                result.ifPresent(e -> {
                                    final TextField inputField = tid.getEditor();
                                    hotkeyProductIds[buttonId] = productId;
                                    hotkeyButtons.get(buttonId).setStyle(paymentButton.getStyle());
                                    final Button hotkey = hotkeyButtons.get(buttonId);
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
                                        .title(bundle.getString(NOTIFICATION_STRING))
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
                                        .title(bundle.getString(NOTIFICATION_STRING))
                                        .text(bundle.getString(HOTKEY_NOT_SET_STRING))
                                        .position(Pos.TOP_RIGHT)
                                        .show();
                            }
                        } catch (final Exception e) {
                            Notifications.create()
                                    .owner(mainAnchorPane.getScene().getWindow())
                                    .title(bundle.getString(NOTIFICATION_STRING))
                                    .text(bundle.getString(HOTKEY_NOT_SET_STRING))
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

    /**
     * Plays a sound when reading a barcode successfully
     */
    public void beepSound() {
        Media sound = new Media(Objects.requireNonNull(getClass().getResource("/sound/beep.mp3")).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setOnError(() ->
                System.out.println("media error" + mediaPlayer.getError().toString()));
        mediaPlayer.play();
    }

    @FXML
    public void showHelp() {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION, bundle.getString("mainViewHelpString"),
                ButtonType.CLOSE);
        alert.setTitle(bundle.getString("helpString"));
        alert.setHeaderText(bundle.getString("helpString"));
        alert.showAndWait();
        barcodeTextField.requestFocus();
    }

    public void negativeProductStockNotification() {
        Notifications.create()
                .owner(mainAnchorPane.getScene().getWindow())
                .title(bundle.getString(NOTIFICATION_STRING))
                .text(bundle.getString("lowProductQuantityString"))
                .position(Pos.TOP_RIGHT)
                .show();
    }

    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
        selfcheckoutlabel.setVisible(false);
        howtouselabel.setVisible(false);
        bundle = mainApp.getBundle();
        privilegesOfUser = this.mainApp.getEngine().getVerifiedPrivileges();
        if (privilegesOfUser.isEmpty() || Collections.max(privilegesOfUser) < 1) {
            languageBox.setItems(languages);
            languageBox.setOnAction(event -> {
                final String lang = switch (languageBox.getValue()) {
                    case "fi" -> "fi_FI";
                    case "en" -> "en_US";
                    default -> throw new IllegalStateException("Unexpected value: " + languageBox.getValue());
                };
                try {
                    final Locale locale = new Locale(lang.split("_")[0], lang.split("_")[1]);
                    Locale.setDefault(locale);
                    this.mainApp.setBundle(ResourceBundle.getBundle("TextResources", locale));
                    System.out.println(locale.getLanguage());
                    this.mainApp.showMainView();

                } catch (final Exception c) {
                    c.printStackTrace();
                }
            });
            languageBox.setVisible(true);
            settingsBtn.setVisible(false);
            logoutBtn.setVisible(false);
            selfcheckoutlabel.setVisible(true);
            howtouselabel.setVisible(true);
            languageText.setVisible(true);
        }
        feedbackProgressBar.setVisible(false);
        mainApp.getStage().setTitle(MainApp.APP_TITLE + " - " + mainApp.getEngine().getUser().getUsername());
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
                final List<Product> products = this.mainApp.getEngine().getTransaction().getOrder().getProductList();
                for (final Product product : products) {
                    if (!items.contains(product)) {
                        items.add(product);
                    }
                }
            } catch (final Exception e) {
                System.out.println("No products in order");
            }
        }
        scanListView.setItems(items);
        scanListView.setCellFactory(productListView -> new ProductListViewCell(this,
                this.mainApp.getEngine().getTransaction().getOrder(), this.items));
        setTotalPrice();
        // Pressing enter runs readBarcode()
        barcodeTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                readBarcode();
        });
        items.addListener((ListChangeListener<Product>) change -> setTotalPrice());
        barcodeTextField.requestFocus();
        final BooleanBinding booleanBind = Bindings.size(items).isEqualTo(0);
        paymentButton.disableProperty().bind(booleanBind);
        // if barcodeTextField's length is greater than 8 characters it is trimmed to 8
        // characters
        barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 8) {
                barcodeTextField.setText(newValue.substring(0, 8));
            }
        });
        // check if selfcheckoutlabel is clicked 5 times in a row and if so, call
        // handleLogoutButton() method
        selfcheckoutlabel.setOnMouseClicked(event -> {
            if (event.getClickCount() == 5) {
                handleLogoutButton();
            }
        });
    }
}
