package view;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.classes.Product;

import java.io.*;
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
    private ProgressBar feedbackProgressBar;
    private ObservableList<Product> items = FXCollections.observableArrayList();
    private String productId;
    private final String[] hotkeyProductIds = new String[2];
    private ArrayList<Button> hotkeyButtons = new ArrayList<>();
    private HotkeyFileHandler hotkeyFileHandler;

    public void loadTransactionView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("transaction-view.fxml"));
        new ViewLoader(mainAnchorPane, fxmlLoader.load());
        ((TransactionView) fxmlLoader.getController()).setMainApp(this.mainApp);
    }

    public void loadOptionsView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("options-view.fxml"));
        new ViewLoader(mainAnchorPane, fxmlLoader.load());
        ((OptionsView) fxmlLoader.getController()).setMainApp(this.mainApp);
    }

    @FXML
    private void readBarcode() {
        try {
            productId = barcodeTextField.getText();
            addProduct(productId);
            barcodeTextField.clear();
            barcodeTextField.requestFocus();
        } catch (Exception e) {
            productId = null;
            Alert alert = new Alert(Alert.AlertType.ERROR, "Tuotetta ei löytynyt tietokannasta!", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    private void addProduct(String productId) {
        Product product = this.mainApp.getEngine().scanProduct(productId);
        if (!items.contains(product)) {
            items.add(product);
        }
        scanListView.refresh();
        setTotalPrice();
    }

    public void setTotalPrice() {
        try {
            float priceInEuros = this.mainApp.getEngine().getTransaction().getOrder().getTotalPrice() / 100.0f;
            this.totalPriceLabel.setText(Float.toString(priceInEuros));
        } catch (NullPointerException ignored) {
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
            hotkeys.get(i).setText(mainApp.getHotkeyButtonNames()[i]);
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
            AtomicBoolean running = new AtomicBoolean(false);

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
                            addProduct(hotkeyProductIds[buttonId]);
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

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.usernameLabel.setText(mainApp.getEngine().getUser().getUsername());
        feedbackProgressBar.setVisible(false);
        hotkeyFileHandler = new HotkeyFileHandler();
        hotkeyFileHandler.loadHotkeys(hotkeyProductIds, mainApp.getHotkeyButtonNames());
        hotkeyButtons.add(hotkeyButton0);
        hotkeyButtons.add(hotkeyButton1);
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
    }
}
