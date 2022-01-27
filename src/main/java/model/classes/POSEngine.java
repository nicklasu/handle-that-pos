package model.classes;

import model.interfaces.IPOSEngine;
import model.interfaces.ITransaction;

public class POSEngine implements IPOSEngine {

    private ITransaction transaction = null;

    // constructor
    public POSEngine() {}

    @Override
    public boolean createTransaction() {
        if (transaction == null) {
            transaction = new Transaction();
            return true;
        } else return false;
    }
}
