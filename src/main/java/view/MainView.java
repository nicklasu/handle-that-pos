package view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.classes.Product;

import java.io.IOException;
import java.util.ArrayList;

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

            int productId = Integer.parseInt(barcodeTextField.getText());

            Product product = this.mainApp.getEngine().scanProduct(productId);

            if (!items.contains(product)) {
                items.add(product);
            }
            scanListView.refresh();
            setTotalPrice();

            barcodeTextField.clear();


            barcodeTextField.requestFocus();
        } catch(Exception e){
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Tuotetta ei löytynyt tietokannasta!", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    public void setTotalPrice() {
        this.totalPriceLabel.setText(Float.toString(this.mainApp.getEngine().getTransaction().getOrder().getTotalPrice()));
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



    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.usernameLabel.setText(mainApp.getEngine().getUser().getUsername());
        //Make barcodeTextField accept only numbers
        barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                barcodeTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }

        });

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
