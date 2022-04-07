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
 * Represents the hardware running the software
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
    private final MainView mainView;
    private final IOrder order;
    private final ObservableList<Product> items;
    private int productAmount;
    private String currency;

    public ProductListViewCell(final MainView mainView, final IOrder order, final ObservableList<Product> items) {
        this.mainView = mainView;
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
                this.mainView.setTotalPrice();
            });
            this.plusButton.setOnAction(event -> {
                this.order.addProductToOrder(product);
                productAmount = Collections.frequency(this.order.getProductList(), product);
                productPriceSet(product);
                this.productAmountLabel.setText(String.valueOf(productAmount));
                this.mainView.setTotalPrice();
                if (product.getStock() < 0) {
                    mainView.negativeProductStockNotification();
                }
            });
            setText(null);
            setGraphic(this.gridPane);
        }
    }

    private void productPriceSet(final Product product) {
        this.productPriceLabel.setText(
                String.format("%.2f", (product.getPrice() * productAmount) / 100f) + CurrencyHandler.getCurrency());
    }
}
