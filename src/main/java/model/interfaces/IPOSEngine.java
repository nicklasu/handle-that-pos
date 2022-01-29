package model.interfaces;

import model.classes.User;

public interface IPOSEngine {
    boolean createTransaction();
    boolean login(String username, String password);
    void logout();
    User getUser();
}
