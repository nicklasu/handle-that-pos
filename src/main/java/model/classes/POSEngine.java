package model.classes;

import model.interfaces.IPOSEngine;
import model.interfaces.ITransaction;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class POSEngine implements IPOSEngine {

    private ITransaction transaction = null;
    private User user = null;
    private UserDAO userDAO;
    private ProductDAO productDAO;
    // constructor
    public POSEngine() {
        this.userDAO = new UserDAO();
        this.productDAO = new ProductDAO();
    }

    @Override
    public boolean login(String username, String password) {
//        User testi = userDAO.getUser("testuser");
//        System.out.println("Tulostetaan kaikki käyttäjät tietokannasta: " + userDAO.getAllUsers());
//        System.out.println("Tulostetaan yksi käyttäjä tietokannasta: " + testi.toString());

        //User testi = new User("Heikki Harju", "heikha", "enkerro");
          //  userDAO.createUser(testi);
        User user = userDAO.getUser(username);
        System.out.println(user.getPassword() + ", " + password);


        /**
         * TÄSSÄ KOHTAA LUETAAN DATABASESTA JA VERTAILLAAN SALIKSII
         */
        if (user != null && password.equals(user.getPassword()) /*JOS SALIKSET TÄSMÄÄ*/) {
            this.user = new User("test", username);
            return true;
        }

        return false;
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
    public ITransaction getTransaction() { return this.transaction; }

    @Override
    public Product scanProduct(int id) {
        if (this.transaction == null) {
            this.transaction = new Transaction();
        }

        /**
         * HAETAAN DATABASESTA ID:llä
         */
        Product product = productDAO.getProduct(id);

        System.out.println(product.getName());
        //Product product = new Product(id, "testi tuote", "tätä tuotetta käytetään testaamiseen", 60.20f, 90); // Luodaan uusi product databasesta haettujen tietojen perusteella

        this.transaction.getOrder().addProductToOrder(product);

        return product;
    }
    @Override
    public void confirmTransaction(){

        transaction = null;
    }
}
