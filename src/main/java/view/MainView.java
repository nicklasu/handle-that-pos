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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainView {
    private MainApp mainApp;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Label usernameLabel;
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
    private Button paymentButton;
    @FXML
    private ProgressBar feedbackProgressBar;
    final private ObservableList<Product> items = FXCollections.observableArrayList();
    private String productId;
    private final String[] hotkeyProductIds = new String[6];
    private final ArrayList<Button> hotkeyButtons = new ArrayList<>();
    private HotkeyFileHandler hotkeyFileHandler;

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
                    .title("Virhe")
                    .text("Tuotetta ei löydy tietokannasta!")
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Haluatko varmasti kirjautua ulos?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            this.mainApp.getEngine().logout();
            this.mainApp.showLoginView();
        }
    }

    private void addHotkeys(ArrayList<Button> hotkeys) {
        for (int i = 0; i < hotkeys.size(); i++) {
            setHotkeyButton(hotkeys.get(i));
            if (Objects.equals(hotkeyProductIds[i], "null") || Objects.equals(hotkeyProductIds[i], "") || Objects.equals(hotkeyProductIds[i], null)) {
                Button hotkey = hotkeys.get(i);
                hotkey.setText("Ei asetettu");
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
                            tid.setTitle("Pikanäppäimen asetus");
                            tid.setHeaderText("Pikanäppäimen oletusnimi: " + items.get(items.size() - 1).getName() + ".\nJos haluat uudelleennimetä, kirjoita alla olevaan kenttään.");
                            tid.setContentText("Uusi nimi:");
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
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Skannaa tuote ennen kuin yrität tallentaa sitä pikanäppäimeen!", ButtonType.CLOSE);
                            alert.showAndWait();
                        }
                    } else {
                        try {
                            if (!Objects.equals(hotkeyProductIds[buttonId], "null")) {
                                addProduct(hotkeyProductIds[buttonId]);
                            } else {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pikanäppäintä ei ole asetettu.", ButtonType.CLOSE);
                                alert.showAndWait();
                            }
                        } catch (Exception e) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pikanäppäintä ei ole asetettu.", ButtonType.CLOSE);
                            alert.showAndWait();
                        }
                    }
                    running.set(false);
                    startTime = 0;
                    feedbackProgressBar.setProgress(0);
                    feedbackProgressBar.setVisible(false);
                }
            }
        });
    }

    public void negativeProductStockNotification() {
        Notifications.create()
                .owner(mainAnchorPane.getScene().getWindow())
                .title("Huomautus!")
                .text("Lisätyn tuotteen varastomäärä on alle 0.")
                .position(Pos.TOP_RIGHT)
                .show();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        usernameLabel.setText(mainApp.getEngine().getUser().getUsername());
        feedbackProgressBar.setVisible(false);
        hotkeyFileHandler = new HotkeyFileHandler();
        hotkeyFileHandler.loadHotkeys(hotkeyProductIds, mainApp.getHotkeyButtonNames());
        hotkeyButtons.add(hotkeyButton0);
        hotkeyButtons.add(hotkeyButton1);
        hotkeyButtons.add(hotkeyButton2);
        hotkeyButtons.add(hotkeyButton3);
        hotkeyButtons.add(hotkeyButton4);
        hotkeyButtons.add(hotkeyButton5);
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
    }
}
