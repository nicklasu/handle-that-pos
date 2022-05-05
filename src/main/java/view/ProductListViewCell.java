package view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import model.classes.CurrencyHandler;
import model.classes.Product;
import model.interfaces.IOrder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

/**
 * List cell controller for MainViewController.
 * Controls list-cell.fxml.
 * MainViewController calls this to add list-cell.fxml into the ListView.
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class ProductListViewCell extends ListCell<Product> {
    @FXML
    private Label productNameLabel;
    @FXML
    private Label productPriceLabel;
    @FXML
    private Label productAmountLabel;
    @FXML
    private Button minusButton;
    @FXML
    private Button plusButton;
    @FXML
    private GridPane gridPane;
    private FXMLLoader fxmlLoader;
    private final MainViewController mainViewController;
    private final IOrder order;
    private final ObservableList<Product> items;
    private int productAmount;
    private String currency;

    /**
     * Initializes the cell of ListView containing products
     *
     * @param mainViewController Controller of Main View
     * @param order IOrder
     * @param items ObservableList<Product>
     */
    public ProductListViewCell(final MainViewController mainViewController, final IOrder order, final ObservableList<Product> items) {
        this.mainViewController = mainViewController;
        this.order = order;
        this.items = items;
        final File file = new File("src/main/resources/HandleThatPos.properties");
        final Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(file)) {
            properties.load(fis);
            this.currency = properties.getProperty("currency");

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Overridden updateItem method to format the cell.
     * Adds two labels and two buttons to the listview cell.
     *
     * @param product
     * @param empty
     */
    @Override
    protected void updateItem(final Product product, final boolean empty) {
        super.updateItem(product, empty);
        if (empty || product == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (this.fxmlLoader == null) {
                this.fxmlLoader = new FXMLLoader(getClass().getResource("list-cell.fxml"));
                fxmlLoader.setController(this);
                try {
                    this.fxmlLoader.load();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            this.productNameLabel.setText(product.getName());
            productAmount = Collections.frequency(this.order.getProductList(), product);
            this.productAmountLabel.setText(String.valueOf(productAmount));
            productPriceSet(product);
            this.minusButton.setOnAction(event -> {
                this.order.removeProductFromOrder(product);
                productAmount = Collections.frequency(this.order.getProductList(), product);
                if (productAmount > 0) {
                    productPriceSet(product);
                    this.productAmountLabel.setText(String.valueOf(productAmount));
                } else {
                    this.items.remove(product);
                }
                this.mainViewController.setTotalPrice();
            });
            this.plusButton.setOnAction(event -> {
                this.order.addProductToOrder(product);
                productAmount = Collections.frequency(this.order.getProductList(), product);
                productPriceSet(product);
                this.productAmountLabel.setText(String.valueOf(productAmount));
                this.mainViewController.setTotalPrice();
                if (product.getStock() < 0) {
                    mainViewController.negativeProductStockNotification();
                }
            });
            setText(null);
            setGraphic(this.gridPane);
        }
    }

    /**
     * Formats products price according to its amount
     *
     * @param product
     */
    private void productPriceSet(final Product product) {
        this.productPriceLabel.setText(
                String.format("%.2f", (product.getPrice() * productAmount) / 100f) + CurrencyHandler.getCurrency());
    }
}
