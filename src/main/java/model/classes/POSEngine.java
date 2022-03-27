package model.classes;

import at.favre.lib.crypto.bcrypt.BCrypt;
import model.DAOs.*;
import model.interfaces.IPOSEngine;
import model.interfaces.ITransaction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the hardware running the software
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and Samu Luoma
 */
@Entity
@Table(name = "Maksupääte")
public class POSEngine implements IPOSEngine {
    /**
     * Identifier, generated from hardwareID
     */
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    private String id;

    /**
     * An object depicting a sales transaction
     */
    @Transient
    private ITransaction transaction = null;

    /**
     * Logged in user
     */
    @Transient
    private User user = null;

    /**
     * Data access object for POSEngines
     */
    @Transient
    private POSEngineDAO ped;

    /**
     * Data access object for users
     */
    @Transient
    private UserDAO userDAO;

    /**
     * Data access object for products
     */
    @Transient
    private ProductDAO productDAO;

    /**
     * Data access object for transactions
     */
    @Transient
    private TransactionDAO transactionDAO;

    /**
     * Data access object for customers
     */
    @Transient
    private CustomerDAO customerDAO;

    /**
     * Data access object for handling privileges
     */
    @Transient
    private PrivilegeDAO privilegeDAO;

    /**
     * Privileges for the current logged in user
     */
    @Transient
    private List<Privilege> privileges;

    /**
     * All the current active privileges for the current user
     */
    @Transient
    private List<Integer> verifiedPrivileges;

    /**
     * Constructor for POSEngine, creates objects from all the needed DAO classes and generates own id
     */
    public POSEngine() {
        this.userDAO = new UserDAO();
        this.ped = new POSEngineDAO();
        this.productDAO = new ProductDAO();
        this.transactionDAO = new TransactionDAO();
        this.customerDAO = new CustomerDAO();
        this.privilegeDAO = new PrivilegeDAO();
        this.ped = new POSEngineDAO();
        this.id = HWID.getHWID();
    }

    /**
     * @return current user's privileges
     */
    @Override
    public List<Integer> getVerifiedPrivileges() {
        return this.verifiedPrivileges;
    }

    /**
     * Login a user to the POSEngine
     * @param username
     * @param password
     * @return 0 if not found in database, 1 if ok, 2 if no privileges
     */
    @Override
    public int login(String username, String password) {
        ped.addID(this);
        User user = userDAO.getUser(username);
        if (user != null) {
            BCrypt.Result result = compare(password, user.getPassword());
            System.out.println(HWID.getHWID());
            if (result.verified) {

                this.user = user;
                privileges = privilegeDAO.getPrivileges(user);
                this.verifiedPrivileges = new ArrayList<>();
                List<Date> privilegeEndDates = privileges.stream().map(p -> p.getPrivilegeEnd()).collect(Collectors.toList());
                List<Date> privilegeStartDates = privileges.stream().map(p -> p.getPrivilegeStart()).collect(Collectors.toList());
                List<Privilege> validPrivileges = new ArrayList<>();
                for (int i = 0; i < privilegeEndDates.size(); i++) {
                    if (!privilegeStartDates.get(i).after(new Date()) && (privilegeEndDates.get(i) == null || !privilegeEndDates.get(i).before(new Date()))) {
                        validPrivileges.add(privileges.get(i));
                        this.verifiedPrivileges.add(privileges.get(i).getPrivilegeLevelIndex());
                    }
                }
                System.out.println("Valid privileges");
                System.out.println(validPrivileges);
                System.out.println(this.verifiedPrivileges);
                if (validPrivileges.isEmpty()) {
                    return 2;
                }
                return 1;
            }
        }
        return 0;
    }

    /**
     * @return identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Sets identifier
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return List of privileges the user is associated with
     */
    @Override
    public List<Privilege> getPrivileges() {
        return privileges;
    }

    /**
     * @return the privilege levels of current user
     */
    @Override
    public List<Integer> getPrivilegeIndexes() {
        return privileges.stream().map(p -> p.getPrivilegeLevelIndex()).collect(Collectors.toList());
    }

    /**
     * Sets privileges of current user
     * @param privileges
     */
    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    /**
     * Logs out current user from the system
     */
    @Override
    public void logout() {
        this.user = null;
        this.transaction = null;
    }

    /**
     * @return Current user
     */
    @Override
    public User getUser() {
        return this.user;
    }

    /**
     * @return current transaction
     */
    @Override
    public ITransaction getTransaction() {
        return this.transaction;
    }

    /**
     * Scans a new product adding it to the current order.
     * @param id identifier of product
     * @return the product that was scanned
     */
    @Override
    public Product scanProduct(String id) {
        if (this.transaction == null) {
            this.transaction = new Transaction(this.user);
        }
        Product product = productDAO.getProduct(id);
        this.transaction.getOrder().addProductToOrder(product);
        return product;
    }

    /**
     * Finalizes a sales transaction and lastly sets current transaction to null
     * @param printReceipt true if customer wants a receipt
     */
    @Override
    public void confirmTransaction(boolean printReceipt) {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        transaction.setTimestamp(ts);
        ((Transaction) transaction).setPos(this);
        transactionDAO.addTransaction((Transaction) this.transaction);
        try {
            ArrayList<Product> productsToUpdate = new ArrayList<>();
            for (Product product : this.transaction.getOrder().getProductList()) {
                if (!productsToUpdate.contains(product)) {
                    productsToUpdate.add(product);
                }
            }
            for (Product product : productsToUpdate) {
                productDAO.updateProduct(product);
            }
        } catch (Exception e) {
            System.out.println("No products in order!");
        }
        if (printReceipt) {
            new ReceiptPrinter().actionPerformed((Transaction) this.transaction);
        }
        transaction = null;
    }

    /**
     * @return customer data access object
     */
    @Override
    public CustomerDAO getCustomerDAO() {
        return this.customerDAO;
    }

    /**
     * @return product data access object
     */
    @Override
    public ProductDAO productDao() {
        return this.productDAO;
    }

    /**
     * @return privilege data access object
     */
    @Override
    public PrivilegeDAO privilegeDAO() {
        return this.privilegeDAO;
    }

    /**
     * @return user data access object
     */
    @Override
    public UserDAO userDAO() {
        return this.userDAO;
    }

    /**
     * @return transaction data access object
     */
    @Override
    public TransactionDAO transactionDAO() {
        return this.transactionDAO;
    }

    /**
     * @return customer data access object
     */
    @Override
    public CustomerDAO customerDAO() {
        return this.customerDAO;
    }

    /**
     * Hashes the password with BCrypt
     * @param password the password in normal format
     * @return hashed password
     */
    private String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    /**
     * Compares as hashed version and a plain version of a password
     * @param password
     * @param hashedPassword
     * @return essentially true if matches
     */
    private BCrypt.Result compare(String password, String hashedPassword) {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
    }

    /**
     * sets transaction to input, used for tests
     * @param testTransaction transaction object
     */
    @Override
    public void setTransaction(Transaction testTransaction) {
        this.transaction = testTransaction;
    }

    /**
     * creates a user
     * @param user
     */
    @Override
    public void addUser(User user) {
        user.setPassword(hashPassword(user.getPassword()));
        userDAO.createUser(user);
    }

    /**
     * Updates a user
     * @param user
     */
    @Override
    public void updateUser(User user) {
        user.setPassword(hashPassword(user.getPassword()));
        userDAO.updateUser(user);
    }

    /**
     * @return returns POSEngine in string format
     */
    @Override
    public String toString() {
        return "POSEngine{" +
                "transaction=" + transaction +
                ", user=" + user +
                ", userDAO=" + userDAO +
                ", productDAO=" + productDAO +
                '}';
    }
}
