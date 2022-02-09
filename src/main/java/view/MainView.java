package view;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.Properties;

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

    private ObservableList<Product> items = FXCollections.observableArrayList();

    @FXML
    private Button hotkeyButton0;
    @FXML
    private Button hotkeyButton1;
    private int productId;
    private int hotkeyProductIds[] = new int [2];
    private ArrayList<Button> hotkeyButtons = new ArrayList<>();


    //Could try to remove (ActionEvent event) from this function and see if it still works.
    public void loadTransactionView(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("transaction-view.fxml"));
        new ViewLoader(mainAnchorPane, fxmlLoader.load());
        ((TransactionView) fxmlLoader.getController()).setMainApp(this.mainApp);
    }

    public void loadOptionsView(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("options-view.fxml"));
        new ViewLoader(mainAnchorPane, fxmlLoader.load());
        ((OptionsView) fxmlLoader.getController()).setMainApp(this.mainApp);
    }



    @FXML
    private void readBarcode() {
        try {
            productId = Integer.parseInt(barcodeTextField.getText());
            addProduct(productId);
            barcodeTextField.clear();
            barcodeTextField.requestFocus();
        } catch (Exception e) {
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Tuotetta ei löytynyt tietokannasta!", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    private void addProduct(int productId) {
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

    private void addHotkeys(ArrayList<Button> hotkeys){
        for (int i = 0; i < hotkeys.size(); i++) {
            setHotkeyButton(hotkeys.get(i));
        }

    }
    private void setHotkeyButton(Button button){
        int buttonId = Integer.parseInt(button.getId().replace("hotkeyButton", ""));
        System.out.println(buttonId);
        button.addEventFilter(MouseEvent.ANY, new EventHandler<>() {
            long startTime;
            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                    startTime = System.currentTimeMillis();
                } else if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                    if (System.currentTimeMillis() - startTime > 2000) {
                        if (productId != 0) {
                            hotkeyProductIds[buttonId] = productId;
                            saveHotkeys();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Skannaa tuote ennen kuin yrität tallentaa sitä pikanäppäimeen!", ButtonType.CLOSE);
                            alert.showAndWait();
                        }
                    } else {
                        try {
                            addProduct(hotkeyProductIds[buttonId]);
                        }
                        catch (NullPointerException e) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pikanäppäintä ei ole asetettu.", ButtonType.CLOSE);
                            alert.showAndWait();
                        }
                    }
                }
            }
        });
    }
    private void saveHotkeys(){
        File configFile = new File("hotkey.properties");
        try {
            Properties props = new Properties();
            for (int i = 0; i < hotkeyProductIds.length; i++) {
                props.setProperty("hotkey" + i, String.valueOf(hotkeyProductIds[i]));
            }
            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "hotkey settings");
            writer.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pikanäppäimen tallennus onnistui!", ButtonType.CLOSE);
            alert.showAndWait();
        } catch (FileNotFoundException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Pikanäppäimen tallennustiedostoa ei löytynyt!", ButtonType.CLOSE);
            alert.showAndWait();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Pikanäppäintallennus epäonnistui!", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    private void loadHotkeys(){
        try {
            File configFile = new File("hotkey.properties");
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            for (int i = 0; i < hotkeyProductIds.length; i++) {
                try {
                    hotkeyProductIds[i] = Integer.parseInt(props.getProperty("hotkey" + i));
                } catch (NumberFormatException e){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pikanäppäinasetuksia ei löytynyt. Jos haluat asettaa pikanäppäimen, aloita skannaamalla haluamasi tuote. Tämän jälkeen pidä jotakin pikanäppäintä pohjassa yli 2 sekuntia.", ButtonType.CLOSE);
                    alert.showAndWait();
                }
            }
            reader.close();
            //System.out.print("hotkey is: " + hotkeyProductId);
        } catch (FileNotFoundException ex) {
            // file does not exist
        } catch (IOException ex) {
            // I/O error
        }
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
        loadHotkeys();
        hotkeyButtons.add(hotkeyButton0);
        hotkeyButtons.add(hotkeyButton1);
        addHotkeys(hotkeyButtons);
        // Populate listView with already existing products from open Transaction
        if (this.mainApp.getEngine().getTransaction() != null) {
            ArrayList<Product> products = this.mainApp.getEngine().getTransaction().getOrder().getProductList();
            for (Product product : products) {
                if (!items.contains(product)) {
                    items.add(product);
                }
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
        if(barcodeTextField.getText().length() == 8){
            readBarcode();
        }
    }
}
