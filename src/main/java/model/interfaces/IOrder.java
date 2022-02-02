package model.interfaces;

import model.classes.Product;
import java.util.ArrayList;

public interface IOrder {

    /**
     * Adds a new product to an order.
     * @param product Scanned product
     * @return true if success
     */
    boolean addProductToOrder(Product product);
    boolean removeProductFromOrder(Product product);
    ArrayList<Product> getProductList();
    float getTotalPrice();
}
