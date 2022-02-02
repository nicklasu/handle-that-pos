package model.classes;

import model.interfaces.IPOSEngine;
import model.interfaces.ITransaction;

public class POSEngine implements IPOSEngine {

    private ITransaction transaction = null;
    private User user = null;

    // constructor
    public POSEngine() {}

    @Override
    public boolean login(String username, String password) {
        /**
         * TÄSSÄ KOHTAA LUETAAN DATABASESTA JA VERTAILLAAN SALIKSII
         */
        if (true /*JOS SALIKSET TÄSMÄÄ*/) {
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
        Product product = new Product(id, "testi tuote", "tätä tuotetta käytetään testaamiseen", 60.20f, 90); // Luodaan uusi product databasesta haettujen tietojen perusteella

        this.transaction.getOrder().addProductToOrder(product);

        return product;
    }
}
