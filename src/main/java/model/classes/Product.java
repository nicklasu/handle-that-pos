package model.classes;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a sales article
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
@Entity
@Table(name = "Tuote")
public class Product {

    /**
     * Unique identifier for the product in string format so different kinds of
     * barcodes work
     */
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    private String id;

    /**
     * Name of item e.g "Dark roast coffee"
     */
    @Column(name = "Nimi")
    private String name;

    /**
     * Longer description of the product
     */
    @Column(name = "Kuvaus")
    private String description;

    /**
     * Price of the product represented as cents as an INT to avoid rounding errors
     */
    @Column(name = "Hinta")
    private int price;

    /**
     * Amount of item in the stock
     */
    @Column(name = "Varastomäärä")
    private int stock;

    /**
     * Used for mapping products to an order
     */
    @OneToMany(mappedBy = "primaryKey.product", cascade = CascadeType.ALL)
    private Set<OrderProduct> orderProducts = new HashSet<>();

    /**
     * Empty constructor required by hibernate
     */
    public Product() {

    }

    /**
     * Constructor for a Product object
     * 
     * @param id          identifier for the product
     * @param name        name of the product
     * @param description description of the product
     * @param price       price of the product
     * @param stock       amount of product in stock
     */
    public Product(final String id, final String name, final String description, final int price, final int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Gets a product to order mapping
     * 
     * @return the set
     */
    public Set<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    /**
     * @param orderProducts The order to product mapping
     */
    public void setOrderProducts(final Set<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    /**
     * Adds a connection to the order to product map
     * 
     * @param orderProduct the connection
     */
    public void addOrderProduct(final OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
    }

    /**
     * @return identifier of product
     */
    public String getId() {
        return id;
    }

    /**
     * @param id identifier of product
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @return name of product
     */
    public String getName() {
        return name;
    }

    /**
     * @param name name of product
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return description of product
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description description of product
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return price of product
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param price price of product
     */
    public void setPrice(final int price) {
        this.price = price;
    }

    /**
     * @return stock of product
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock stock of product
     */
    public void setStock(final int stock) {
        this.stock = stock;
    }

    /**
     * @return string format of the name of the product
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Overwritten compare method
     * 
     * @param o object to be compared
     * @return true if same object
     */
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        final Product product = (Product) o;
        return this.id.equals(product.id);
    }
}
