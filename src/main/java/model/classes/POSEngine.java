package model.classes;

import model.interfaces.IPOSEngine;
import model.interfaces.ITransaction;
import model.interfaces.IUser;

public class POSEngine implements IPOSEngine {

    private ITransaction transaction = null;
    private User user = null;

    // constructor
    public POSEngine() {}

    @Override
    public boolean createTransaction() {
        if (transaction == null) {
            transaction = new Transaction();
            return true;
        } else return false;
    }

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
}
