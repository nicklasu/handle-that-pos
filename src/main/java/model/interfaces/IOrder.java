package model.interfaces;

import model.classes.Product;

import java.util.List;

/**
 * Interface for order
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public interface IOrder {

    /**
     * Adds a new product to an order.
     * 
     * @param product Scanned product
     * @return true if success
     */
    boolean addProductToOrder(Product product);

    /**
     * Removes a product from the order
     * 
     * @param product product to be deleted
     * @return true if success
     */
    boolean removeProductFromOrder(Product product);

    /**
     * Gets all the products in the order currently
     * 
     * @return list of products
     */
    List<Product> getProductList();

    /**
     * Gets the total price of all the products in the order
     * 
     * @return price of products as int
     */
    int getTotalPrice();
}
