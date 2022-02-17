package model.interfaces;

import model.classes.Product;
import java.util.ArrayList;
import java.util.List;

public interface IOrder {

    /**
     * Adds a new product to an order.
     * @param product Scanned product
     * @return true if success
     */
    boolean addProductToOrder(Product product);
    boolean removeProductFromOrder(Product product);
    List<Product> getProductList();
    int getTotalPrice();
}
