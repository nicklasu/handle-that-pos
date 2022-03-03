package model.interfaces;

import model.classes.*;

public interface IPOSEngine {
    boolean login(String username, String password);

    void logout();

    User getUser();

    ITransaction getTransaction();

    Product scanProduct(String id);

    void confirmTransaction(boolean printReceipt, Customer customer);

    CustomerDAO getCustomerDAO();

    ProductDAO productDao();

    void addUser(User user);
}
