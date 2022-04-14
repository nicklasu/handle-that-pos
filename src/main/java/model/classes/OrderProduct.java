package model.classes;

import javax.persistence.*;

/**
 * Object representing a join table mapping products to orders in the database
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
@Entity
@Table(name = "Sisältyy")
@AssociationOverride(name = "primaryKey.product", joinColumns = @JoinColumn(name = "TuoteID"))
@AssociationOverride(name = "primaryKey.order", joinColumns = @JoinColumn(name = "TilausID"))
public class OrderProduct {
    /**
     * Order identifier for mapping products to
     */
    private OrderProductId primaryKey = new OrderProductId();

    /**
     * Amount of a product in the order
     */
    private int amount = 1;

    /**
     * @return order identifier
     */
    @EmbeddedId
    public OrderProductId getPrimaryKey() {
        return primaryKey;
    }

    /**
     * Sets order identifier
     * 
     * @param primaryKey order identifier
     */
    public void setPrimaryKey(final OrderProductId primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the order
     */
    @Transient
    public Order getOrder() {
        return getPrimaryKey().getOrder();
    }

    /**
     * Sets order
     * 
     * @param order the order
     */
    public void setOrder(final Order order) {
        getPrimaryKey().setOrder(order);
    }

    /**
     * @return a product
     */
    @Transient
    public Product getProduct() {
        return getPrimaryKey().getProduct();
    }

    /**
     * Sets a product for a identifier
     * 
     * @param product
     */
    public void setProduct(final Product product) {
        getPrimaryKey().setProduct(product);
    }

    /**
     * @return amount of a product
     */
    @Column(name = "Määrä")
    public int getAmount() {
        return amount;
    }

    /**
     * Sets amount of a product
     * 
     * @param amount
     */
    public void setAmount(final int amount) {
        this.amount = amount;
    }
}
