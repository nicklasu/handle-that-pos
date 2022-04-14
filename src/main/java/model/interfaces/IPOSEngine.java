package model.interfaces;

import model.DAOs.*;
import model.classes.*;

import java.util.List;

/**
 * Interface for POSEngine, representing the device that is running the software
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public interface IPOSEngine {
    /**
     * Login method
     * 
     * @param username username to log in with
     * @param password password to log in with
     * @return 0 if not found in database, 1 if ok, 2 if no sufficient privileges
     *         for this user
     */
    LoginStatus login(String username, String password);

    /**
     * logout method
     */
    void logout();

    /**
     * @return current logged in user
     */
    User getUser();

    /**
     * @return Current transaction
     */
    ITransaction getTransaction();

    /**
     * Scans a new product adding it to the order and creates a new transaction if
     * one does not exist
     * 
     * @param id Identifier for the product that is to be added
     * @return the product that was added
     */
    Product scanProduct(String id);

    /**
     * Finalizes the transaction and saves it to the database
     * 
     * @param printReceipt whether a receipt is to be printed or not
     */
    void confirmTransaction(boolean printReceipt);

    /**
     * Gets the customerDAO object
     * 
     * @return the data access object
     */
    CustomerDAO customerDAO();

    /**
     * Gets the productDAO object
     * 
     * @return the data access object
     */
    ProductDAO productDao();

    /**
     * Sets transaction to the transaction provided in the parameter, used in tests
     * 
     * @param testTransaction input transaction
     */
    void setTransaction(Transaction testTransaction);

    /**
     * Adds a new user
     * 
     * @param user user to be added
     */
    void addUser(User user);

    /**
     * Gets all privileges the logged in user is associated with
     * 
     * @return list of privileges
     */
    List<Privilege> getPrivileges();

    /**
     * Gets all the different levels of access from the current user at current time
     * 
     * @return list of numbers denoting access levels available
     */
    List<Integer> getPrivilegeIndexes();

    /**
     * Gets PrivilegeDAO
     * 
     * @return the data access object
     */
    PrivilegeDAO privilegeDAO();

    /**
     * Gets UserDAO
     * 
     * @return the data access object
     */
    UserDAO userDAO();

    /**
     * Gets TransactionDAO
     * 
     * @return the data access object
     */
    TransactionDAO transactionDAO();

    /**
     * Gets all the different levels of access from the current user at current time
     * 
     * @return list of access levels as integers
     */
    List<Integer> getVerifiedPrivileges();

    /**
     * Updates a user
     * 
     * @param user the user to be updated
     */
    void updateUser(User user);

    ProfileDAO profileDAO();
}
