package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.classes.Order;
import model.classes.Product;
import model.interfaces.IOrder;

import java.io.IOException;
import java.util.Collections;

public class ProductListViewCell extends ListCell<Product> {

    @FXML
    private Label productNameLabel;

    @FXML
    private Label productAmountLabel;

    @FXML
    private Button minusButton;

    @FXML
    private Button plusButton;

    @FXML
    private GridPane gridPane;

    private FXMLLoader fxmlLoader;

    private MainView mainView;

    private IOrder order;

    private ObservableList<Product> items;

    private int productAmount;

    public ProductListViewCell(MainView mainView, IOrder order, ObservableList<Product> items) {
        this.mainView   = mainView;
        this.order      = order;
        this.items      = items;
    }


    @Override
    protected void updateItem(Product product, boolean empty) {
        super.updateItem(product, empty);

        if(empty || product == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (this.fxmlLoader == null) {
                this.fxmlLoader = new FXMLLoader(getClass().getResource("list-cell.fxml"));
                fxmlLoader.setController(this);

                try {
                    this.fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            this.productNameLabel.setText(product.getName());
            productAmount = Collections.frequency(this.order.getProductList(), product);
            this.productAmountLabel.setText(String.valueOf(productAmount));


            this.minusButton.setOnAction(event -> {
                this.order.removeProductFromOrder(product);

                productAmount = Collections.frequency(this.order.getProductList(), product);
                if (productAmount > 0) {
                    this.productAmountLabel.setText(String.valueOf(productAmount));
                } else {
                    this.items.remove(product);
                }

                this.mainView.setTotalPrice();
            });

            this.plusButton.setOnAction(event -> {
                this.order.addProductToOrder(product);

                productAmount = Collections.frequency(this.order.getProductList(), product);
                this.productAmountLabel.setText(String.valueOf(productAmount));

                this.mainView.setTotalPrice();
            });

            setText(null);
            setGraphic(this.gridPane);
        }

    }

}
