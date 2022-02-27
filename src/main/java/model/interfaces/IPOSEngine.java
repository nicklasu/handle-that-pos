package model.interfaces;

import model.classes.Product;
import model.classes.ProductDAO;
import model.classes.User;

public interface IPOSEngine {
    boolean login(String username, String password);
    void logout();
    User getUser();
    ITransaction getTransaction();
    Product scanProduct(String id);
    void confirmTransaction(boolean printReceipt);
    ProductDAO productDao();
    void addUser(User user);
    }
