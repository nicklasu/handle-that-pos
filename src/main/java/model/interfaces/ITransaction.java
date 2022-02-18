package model.interfaces;

import model.classes.Customer;
import model.classes.Order;
import model.classes.PaymentMethod;
import model.classes.User;

import java.sql.Timestamp;

public interface ITransaction {
    Order getOrder();

    void setOrder(Order order);

    Customer getCustomer();

    void setCustomer(Customer customer);

    PaymentMethod getPaymentMethod();

    void setPaymentMethod(PaymentMethod paymentMethod);

    User getUser();

    void setUser(User user);

    Timestamp getTimestamp();

    void setTimestamp(Timestamp timestamp);
}
