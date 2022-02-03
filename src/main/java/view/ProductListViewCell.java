package view;

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

    private IOrder order;

    public ProductListViewCell(IOrder order) {
        this.order = order;
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
            this.productAmountLabel.setText(String.valueOf(60));
            this.minusButton.setOnAction(event -> {
                this.order.removeProductFromOrder(product);
            });

            this.plusButton.setOnAction(event -> {
                this.order.addProductToOrder(product);
                System.out.println("test");
            });

            setText(null);
            setGraphic(this.gridPane);
        }

    }

}
