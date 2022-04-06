package model.interfaces;

import model.classes.Customer;
import model.classes.Order;
import model.classes.PaymentMethod;
import model.classes.User;

import java.sql.Timestamp;

/**
 * Interface for Transaction, represents a sales transaction
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public interface ITransaction {
    /**
     * Gets the list of products associated with the transaction
     * 
     * @return the order object
     */
    Order getOrder();

    /**
     * Sets order of the transaction
     * 
     * @param order the order to be linked to the transaction
     */
    void setOrder(Order order);

    /**
     * Gets the customer associated with the transaction
     * 
     * @return customer object
     */
    Customer getCustomer();

    /**
     * Sets the customer associated with the transaction
     * 
     * @param customer the customer object
     */
    void setCustomer(Customer customer);

    /**
     * Gets the paymentmethod
     * 
     * @return Enum PaymentMethod
     */
    PaymentMethod getPaymentMethod();

    /**
     * Sets the Paymentmethod
     * 
     * @param paymentMethod Enum PaymentMethod
     */
    void setPaymentMethod(PaymentMethod paymentMethod);

    /**
     * Gets the user associated with the transaction
     * 
     * @return the user object
     */
    User getUser();

    /**
     * Sets the user associated with the transaction
     * 
     * @param user the user object
     */
    void setUser(User user);

    /**
     * Gets the time of transaction
     * 
     * @return timestamp
     */
    Timestamp getTimestamp();

    /**
     * Sets the tine of transaction
     * 
     * @param timestamp timestamp
     */
    void setTimestamp(Timestamp timestamp);
}
