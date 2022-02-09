package model.classes;

import model.interfaces.IOrder;
import model.interfaces.ITransaction;

public class Transaction implements ITransaction {

    private IOrder order;
    private Customer customer;
    private PaymentMethod paymentMethod;

    public Transaction() {
        this.order = new Order();
        this.paymentMethod = PaymentMethod.CARD; // Oletuksena maksukortti maksutapana
        this.customer = null; // Ei Bonsuasiakkuutta oletuksena
    }

    @Override
    public IOrder getOrder() {
        return this.order;
    }

    @Override
    public void setOrder(IOrder order) {
        this.order = order;
    }

    @Override
    public Customer getCustomer() {
        return this.customer;
    }

    @Override
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    @Override
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
