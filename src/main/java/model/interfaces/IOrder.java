package model.interfaces;

import model.classes.Product;

public interface IOrder {

    /**
     * Adds a new product to an order.
     * @param x Scanned product
     * @return true if success
     */
    boolean addProductToOrder(Product x);
}
