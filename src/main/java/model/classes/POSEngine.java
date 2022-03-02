package model.classes;

import at.favre.lib.crypto.bcrypt.BCrypt;
import model.interfaces.IPOSEngine;
import model.interfaces.ITransaction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name = "Maksupääte")
public class POSEngine implements IPOSEngine {

    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    private String id = HWID.getHWID();

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

    // constructor
    public POSEngine() {
        this.userDAO = new UserDAO();
        this.productDAO = new ProductDAO();
        this.transactionDAO = new TransactionDAO();
        this.id = HWID.getHWID();
    }

    @Override
    public boolean login(String username, String password) {

        User user = userDAO.getUser(username);

        BCrypt.Result result = compare(password, user.getPassword());
        System.out.println(HWID.getHWID());

        //TÄSSÄ KOHTAA LUETAAN DATABASESTA JA VERTAILLAAN SALIKSII

        if (user != null && result.verified /*JOS SALIKSET TÄSMÄÄ*/) {
            this.user = user;
            return true;
        }

        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

        System.out.println(product.getName());
        //Product product = new Product(id, "testi tuote", "tätä tuotetta käytetään testaamiseen", 60.20f, 90); // Luodaan uusi product databasesta haettujen tietojen perusteella

        this.transaction.getOrder().addProductToOrder(product);

        return product;
    }

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

    @Override
    public ProductDAO productDao() {
        return this.productDAO;
    }

    private String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    private BCrypt.Result compare(String password, String hashedPassword) {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
    }

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
