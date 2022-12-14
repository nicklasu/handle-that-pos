package model.classes;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key for the join table mapping products to order in the database
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
@Embeddable
public class OrderProductId implements Serializable {

    /**
     * The order consisting of products
     */
    @Transient
    private Order order;

    /**
     * A single sales article
     */
    @Transient
    private Product product;

    /**
     * @return the set of products
     */
    @ManyToOne(cascade = CascadeType.ALL)
    public Order getOrder() {
        return order;
    }

    /**
     * sets the group of products
     * 
     * @param order set of products
     */
    public void setOrder(final Order order) {
        this.order = order;
    }

    /**
     * @return a single product
     */
    @ManyToOne(cascade = CascadeType.ALL)
    public Product getProduct() {
        return product;
    }

    /**
     * Sets a single product
     * 
     * @param product
     */
    public void setProduct(final Product product) {
        this.product = product;
    }

    /**
     * Overwrites the equals method
     * 
     * @param o object to be compared
     * @return true if same objects
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        final OrderProductId that = (OrderProductId) o;
        return Objects.equals(order, that.order) &&
                Objects.equals(product, that.product);
    }

    /**
     * Returns a hashcode for hibernate
     * 
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(order, product);
    }
}
