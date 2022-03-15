package model.classes;

import at.favre.lib.crypto.bcrypt.BCrypt;
import model.interfaces.IPOSEngine;
import model.interfaces.ITransaction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Maksupääte")
public class POSEngine implements IPOSEngine {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    private String id;
    @Transient
    private ITransaction transaction = null;
    @Transient
    private User user = null;
    @Transient
    private UserDAO userDAO;
    @Transient
    private ProductDAO productDAO;
    @Transient
    private TransactionDAO transactionDAO;
    @Transient
    private CustomerDAO customerDAO;
    @Transient
    private PrivilegeDAO privilegeDAO;
    @Transient
    private List<Privilege> privileges;
    @Transient
    private List<Integer> verifiedPrivileges;
    // constructor
    public POSEngine() {
        this.userDAO = new UserDAO();
        this.productDAO = new ProductDAO();
        this.transactionDAO = new TransactionDAO();
        this.customerDAO = new CustomerDAO();
        this.privilegeDAO = new PrivilegeDAO();
        this.id = HWID.getHWID();
    }
    @Override
    public List<Integer> getVerifiedPrivileges(){
        return this.verifiedPrivileges;
    }
    @Override
    public int login(String username, String password) {
        User user = userDAO.getUser(username);
        if (user != null) {
            BCrypt.Result result = compare(password, user.getPassword());
            System.out.println(HWID.getHWID());
            //TÄSSÄ KOHTAA LUETAAN DATABASESTA JA VERTAILLAAN SALIKSII
            if (result.verified /*JOS SALIKSET TÄSMÄÄ*/) {

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<Privilege> getPrivileges() {
        return privileges;
    }

    @Override
    public List<Integer> getPrivilegeIndexes() {
        return privileges.stream().map(p -> p.getPrivilegeLevelIndex()).collect(Collectors.toList());
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    @Override
    public void logout() {
        this.user = null;
        this.transaction = null;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public ITransaction getTransaction() {
        return this.transaction;
    }

    @Override
    public Product scanProduct(String id) {
        if (this.transaction == null) {
            this.transaction = new Transaction(this.user);
        }
        //HAETAAN DATABASESTA ID:llä
        Product product = productDAO.getProduct(id);
        //System.out.println(product.getName());
        this.transaction.getOrder().addProductToOrder(product);
        return product;
    }

    @Override
    public void confirmTransaction(boolean printReceipt, Customer customer) {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        transaction.setTimestamp(ts);
        if (customer != null) {
            if (customer.getCustomerLevelIndex() == 1) {
                float totalPrice = transaction.getOrder().getTotalPrice();
                totalPrice *= 0.95;
                transaction.getOrder().setTotalPrice(Math.round(totalPrice));
            }
            transaction.setCustomer(customer);
        } else {
            transaction.setCustomer(null);
        }
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

    @Override
    public CustomerDAO getCustomerDAO() {
        return this.customerDAO;
    }

    @Override
    public ProductDAO productDao() {
        return this.productDAO;
    }

    @Override
    public PrivilegeDAO privilegeDAO() {
        return this.privilegeDAO;
    }

    @Override
    public UserDAO userDAO() {
        return this.userDAO;
    }

    @Override
    public TransactionDAO transactionDAO() {
        return this.transactionDAO;
    }

    @Override
    public CustomerDAO customerDAO() {
        return this.customerDAO;
    }

    private String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    private BCrypt.Result compare(String password, String hashedPassword) {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
    }

    @Override
    public void setTransaction(Transaction testTransaction) {
        this.transaction = testTransaction;
    }

    @Override
    public void addUser(User user) {
        user.setPassword(hashPassword(user.getPassword()));
        userDAO.createUser(user);
    }

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
