package model.interfaces;

import model.classes.*;

import java.util.List;

public interface IPOSEngine {
    int login(String username, String password);

    void logout();

    User getUser();

    ITransaction getTransaction();

    Product scanProduct(String id);

    CustomerDAO customerDAO();

    void confirmTransaction(boolean printReceipt, Customer customer);

    CustomerDAO getCustomerDAO();

    ProductDAO productDao();

    void setTransaction(Transaction testTransaction);

    void addUser(User user);

    List<Privilege> getPrivileges();

    List<Integer> getPrivilegeIndexes();

    PrivilegeDAO privilegeDAO();

    UserDAO userDAO();

    TransactionDAO transactionDAO();
}
