package model.classes;

import model.interfaces.IOrder;
import model.interfaces.ITransaction;

public class Transaction implements ITransaction {

    private IOrder order;
    private Customer customer;
    private PaymentMethod paymentMethod;

    public Transaction() {
        //order = new Order();

    }


}
