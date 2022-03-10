package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import model.classes.Product;

import java.io.IOException;

public class ListCellTransaction extends ListCell<Product> {
    @FXML
    private Label nameLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private GridPane gridPane;
    private FXMLLoader fxmlLoader;

    public ListCellTransaction() {

    }
    @Override
    protected void updateItem(Product product, boolean empty){
        super.updateItem(product, empty);
        if (empty || product == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (this.fxmlLoader == null) {
                this.fxmlLoader = new FXMLLoader(getClass().getResource("list-cell-transaction.fxml"));
                fxmlLoader.setController(this);
                try {
                    this.fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.nameLabel.setText(product.getName());
            this.priceLabel.setText(String.format("%.2f",product.getPrice() / 100f) + "€");
            setText(null);
            setGraphic(this.gridPane);
        }
    }
}