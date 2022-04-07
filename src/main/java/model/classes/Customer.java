package model.classes;

import javax.persistence.*;

/**
 * Represents a customer that is linked to a transaction
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
@Entity
@Table(name = "Asiakas")
public class Customer {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;

    /**
     * For differentiating customers for example for membership bonus
     */
    @Column(name = "Asiakkuus")
    private int customerLevelIndex;

    /**
     * Empty constructor for hibernate
     */
    public Customer() {
    }

    /**
     * Creates a customer with a specified customer level
     * 
     * @param customerLevelIndex
     */
    public Customer(final int customerLevelIndex) {
        this.customerLevelIndex = customerLevelIndex;
    }

    /**
     * @return id of customer
     */
    public int getId() {
        return id;
    }

    /**
     * @param id sets id of customer
     */
    public void setId(final int id) {
        if (id < 0) {
            throw new RuntimeException("Customer can't have a negative ID");
        }
        this.id = id;
    }

    /**
     * @return customer level
     */
    public int getCustomerLevelIndex() {
        return this.customerLevelIndex;
    }

    /**
     * @return customer's data as a string
     */
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", customerLevelIndex=" + customerLevelIndex +
                '}';
    }
}
