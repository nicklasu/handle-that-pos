package model.classes;

import javax.persistence.*;

@Entity
@Table(name = "Sisältyy")
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.product", joinColumns = @JoinColumn(name = "TuoteID")),
        @AssociationOverride(name = "primaryKey.order", joinColumns = @JoinColumn(name = "TilausID"))})
public class OrderProduct {
    private OrderProductId primaryKey = new OrderProductId();

    private int amount = 1;

    @EmbeddedId
    public OrderProductId getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(OrderProductId primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Transient
    public Order getOrder() {
        return getPrimaryKey().getOrder();
    }

    public void setOrder(Order order) {
        getPrimaryKey().setOrder(order);
    }

    @Transient
    public Product getProduct() {
        return getPrimaryKey().getProduct();
    }

    public void setProduct(Product product) {
        getPrimaryKey().setProduct(product);
    }

    @Column(name = "Määrä")
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
