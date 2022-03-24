package model.classes;

import model.interfaces.ITransaction;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Maksutapahtuma")
public class Transaction implements ITransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private Order order;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AsiakasID", referencedColumnName = "id")
    private Customer customer;

    @Transient
    private PaymentMethod paymentMethod;

    @Column(name = "MaksutapaID")
    private int paymentMethodIndex;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "KäyttäjäID", referencedColumnName = "id")
    private User user;

    @Column(name = "Aikaleima")
    private Timestamp timestamp;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MaksupääteID", referencedColumnName = "id")
    private POSEngine pos;

    public Transaction() {
    }

    public Transaction(User user) {
        this.order = new Order(this);
        this.paymentMethod = PaymentMethod.CARD; // Oletuksena maksukortti maksutapana
        this.paymentMethodIndex = 1; //PaymentMethod.valueOf(paymentMethod.name()).ordinal();
        this.customer = null; // Ei Bonsuasiakkuutta oletuksena
        this.user = user;
        this.timestamp = null;
        this.pos = null;
    }


    public POSEngine getPos() {
        return pos;
    }

    public void setPos(POSEngine pos) {
        this.pos = pos;
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
    public Order getOrder() {
        return this.order;
    }

    @Override
    public void setOrder(Order order) {
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
        this.paymentMethodIndex = this.paymentMethod.ordinal();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPaymentMethodIndex() {
        return paymentMethodIndex;
    }

    public void setPaymentMethodIndex(int paymentMethodIndex) {
        this.paymentMethodIndex = paymentMethodIndex;
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
