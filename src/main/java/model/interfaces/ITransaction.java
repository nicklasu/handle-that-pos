package model.interfaces;

import model.classes.Customer;
import model.classes.PaymentMethod;

public interface ITransaction {
    IOrder getOrder();

    void setOrder(IOrder order);

    Customer getCustomer();

    void setCustomer(Customer customer);

    PaymentMethod getPaymentMethod();

    void setPaymentMethod(PaymentMethod paymentMethod);
}
