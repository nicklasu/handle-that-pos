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
import model.classes.ProductDAO;

import java.io.*;
import java.util.ArrayList;
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
    private String hotkeyProductIds[] = new String[2];
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
        float priceInEuros = this.mainApp.getEngine().getTransaction().getOrder().getTotalPrice() / 100.0f;
        this.totalPriceLabel.setText(Float.toString(priceInEuros));
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
        ProductDAO productDAO = new ProductDAO();
        for (int i = 0; i < hotkeys.size(); i++) {
            setHotkeyButton(hotkeys.get(i));
            try {
                hotkeys.get(i).setText(productDAO.getProduct(hotkeyProductIds[i]).getName());
            } catch (Exception e) {

            }
        }
    }

    /**
     * Takes existing hotkey information and uses it by adding desired products to listview and order.
     * Saves new hotkey information if button pressed for 2 sec or more.
     *
     * @param button
     */
    private void setHotkeyButton(Button button) {
        int buttonId = Integer.parseInt(button.getId().replace("hotkeyButton", ""));

        button.addEventFilter(MouseEvent.ANY, new EventHandler<>() {
            long startTime;
            AtomicBoolean running = new AtomicBoolean(false);

            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
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
                            hotkeyProductIds[buttonId] = productId;
                            hotkeyFileHandler.saveHotkeys(hotkeyProductIds);
                            hotkeyButtons.get(buttonId).setText(items.get(items.size() - 1).getName());
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
                }
            }
        });
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.usernameLabel.setText(mainApp.getEngine().getUser().getUsername());
        //Make barcodeTextField accept only numbers
        barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                barcodeTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        hotkeyFileHandler = new HotkeyFileHandler();
        hotkeyFileHandler.loadHotkeys(hotkeyProductIds);
        hotkeyButtons.add(hotkeyButton0);
        hotkeyButtons.add(hotkeyButton1);
        addHotkeys(hotkeyButtons);
        // Populate listView with already existing products from open Transaction
        if (this.mainApp.getEngine().getTransaction() != null) {
            try {
                ArrayList<Product> products = this.mainApp.getEngine().getTransaction().getOrder().getProductList();

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
        //Pressing enter runs readBarcode()
        barcodeTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                readBarcode();
        });
        items.addListener((ListChangeListener<Product>) change -> setTotalPrice());
        barcodeTextField.requestFocus();
    }

    @FXML
    private void handleInputChange() {
        if (barcodeTextField.getText().length() == 8) {
            readBarcode();
        }
    }
}
