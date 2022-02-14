package model.classes;

import model.interfaces.IOrder;
import model.interfaces.ITransaction;

import java.sql.Timestamp;

public class Transaction implements ITransaction {

    private IOrder order;
    private Customer customer;
    private PaymentMethod paymentMethod;
    private User user;
    private Timestamp timestamp;


    public Transaction(User user) {
        this.order = new Order();
        this.paymentMethod = PaymentMethod.CARD; // Oletuksena maksukortti maksutapana
        this.customer = null; // Ei Bonsuasiakkuutta oletuksena
        this.user = user;
        this.timestamp = null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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

    @Override
    public String toString() {
        return "Transaction{" +
                "order=" + order +
                ", customer=" + customer +
                ", paymentMethod=" + paymentMethod +
                ", user=" + user +
                '}';
    }
}
