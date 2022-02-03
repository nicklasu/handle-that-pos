package model.interfaces;

import model.classes.Product;
import model.classes.User;

public interface IPOSEngine {
    boolean login(String username, String password);
    void logout();
    User getUser();
    ITransaction getTransaction();
    Product scanProduct(int id);
    void confirmTransaction();

    }
