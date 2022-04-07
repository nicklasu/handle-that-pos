package model.classes;

import model.interfaces.ITransaction;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Represents a sales transaction
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
@Entity
@Table(name = "Maksutapahtuma")
public class Transaction implements ITransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    /**
     * Order consists of products
     */
    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private Order order;

    /**
     * Customer for the transaction
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AsiakasID", referencedColumnName = "id")
    private Customer customer;

    /**
     * Payment method used for the transaction
     */
    @Transient
    private PaymentMethod paymentMethod;

    /**
     * Ordinal value for enum PaymentMethod
     */
    @Column(name = "MaksutapaID")
    private int paymentMethodIndex;

    /**
     * Logged in user that handled the transaction
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "KäyttäjäID", referencedColumnName = "id")
    private User user;

    /**
     * Time of sale
     */
    @Column(name = "Aikaleima")
    private Timestamp timestamp;

    /**
     * The device the sale was handled on
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MaksupääteID", referencedColumnName = "id")
    private POSEngine pos;

    /**
     * Empty constructor for hibernate
     */
    public Transaction() {
    }

    /**
     * Constructor for a new transaction
     * 
     * @param user the logged-in user handling the transaction
     */
    public Transaction(final User user) {
        this.order = new Order(this);
        this.paymentMethod = PaymentMethod.CARD; // By default the payment method is credit/debit card
        this.paymentMethodIndex = 1; // PaymentMethod.valueOf(paymentMethod.name()).ordinal();
        this.customer = null; // Ei Bonsuasiakkuutta oletuksena
        this.user = user;
        this.timestamp = null;
        this.pos = null;
    }

    /**
     * @return the device handling the transaction
     */
    public POSEngine getPos() {
        return pos;
    }

    /**
     * @param pos the device handling the transaction
     */
    public void setPos(final POSEngine pos) {
        this.pos = pos;
    }

    /**
     * @return the user handling the transaction
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user handling the transaction
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * @return time of transaction
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp time of transaction
     */
    public void setTimestamp(final Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return order consisting of products
     */
    @Override
    public Order getOrder() {
        return this.order;
    }

    /**
     * @param order order consisting of products
     */
    @Override
    public void setOrder(final Order order) {
        this.order = order;
    }

    /**
     * @return customer of transaction
     */
    @Override
    public Customer getCustomer() {
        return this.customer;
    }

    /**
     * @param customer customer of transaction
     */
    @Override
    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    /**
     * @return payment method used in the transaction
     */
    @Override
    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    /**
     * @param paymentMethod payment method used in the transaction
     */
    @Override
    public void setPaymentMethod(final PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.paymentMethodIndex = this.paymentMethod.ordinal();
    }

    /**
     * @return identifier for transaction
     */
    public int getId() {
        return this.id;
    }

    /**
     * @param id identifier for transaction
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * @return payment method ordinal value
     */
    public int getPaymentMethodIndex() {
        return paymentMethodIndex;
    }

    /**
     * @param paymentMethodIndex payment method ordinal value
     */
    public void setPaymentMethodIndex(final int paymentMethodIndex) {
        this.paymentMethodIndex = paymentMethodIndex;
    }

    /**
     * @return Transaction in string format
     */
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
