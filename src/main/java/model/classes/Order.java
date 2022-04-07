package model.classes;

import model.interfaces.IOrder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a set of products
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
@Entity
@Table(name = "Tilaus")
public class Order implements IOrder {


    /**
     * Identifier for the order
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    /**
     * Parent object of Order
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MaksutapahtumaID", referencedColumnName = "ID")
    private Transaction transaction;

    /**
     * Maps products to order, used for database purposes
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "primaryKey.order", cascade = CascadeType.ALL)
    private Set<OrderProduct> orderProducts = new HashSet<>();

    /**
     * Maps products to order, used for user interface purposes
     */
    @Transient
    private final List<Product> productList = new ArrayList<>();

    /**
     * Total price of products in the order
     */
    @Transient
    private int totalPrice = 0;

    /**
     * Empty constructor for hibernate
     */
    public Order() {
    }

    /**
     * Creates the order as a child of a parent object
     * 
     * @param transaction the parent object
     */
    public Order(final Transaction transaction) {
        this.transaction = transaction;
    }

    /**
     * @return identifier of the order
     */
    public int getId() {
        return id;
    }

    /**
     * @param id sets identifier of the order
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * @return the parent object
     */
    public Transaction getTransaction() {
        return transaction;
    }

    /**
     * @param transaction sets the parent object
     */
    public void setTransaction(final Transaction transaction) {
        this.transaction = transaction;
    }

    /**
     * Gets the product to order mapping (database version)
     * 
     * @return
     */
    public Set<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    /**
     * Sets the product to order mapping (database version)
     * 
     * @param orderProducts
     */
    public void setOrderProducts(final Set<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    /**
     * Adds a product to current order
     * 
     * @param orderProduct the product
     */
    public void addOrderProduct(final OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
    }

    /**
     * Gets the product to order mapping
     * 
     * @return list of products
     */
    @Override
    public List<Product> getProductList() {
        return this.productList;
    }

    /**
     * Fetches the total price of products in the order
     * 
     * @return price
     */
    @Override
    public int getTotalPrice() {
        if (transaction.getCustomer() != null) {
            return (int) (totalPrice * 0.95);
        }
        return totalPrice;
    }

    /**
     * Adds a product to the order, creates an order if one does not exist
     * 
     * @param product Scanned product the product to add
     * @return returns true if successful
     */
    @Override
    public boolean addProductToOrder(final Product product) {
        if (product != null) {
            Product productToAdd = product;
            try {
                if (!productList.contains(product)) {
                    final OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setOrder(this);
                    orderProduct.setProduct(product);
                    addOrderProduct(orderProduct);
                } else {
                    for (final OrderProduct op : orderProducts) {
                        if (op.getProduct().equals(product)) {
                            op.setAmount(op.getAmount() + 1);
                            productToAdd = op.getProduct();
                        }
                    }
                }
                productList.add(productToAdd);
                totalPrice += productToAdd.getPrice();
                productToAdd.setStock(productToAdd.getStock() - 1);
                return true;
            } catch (final Exception e) {
                System.out.println("Error adding a product to the order " + e);
            }
        }
        return false;
    }

    /**
     * removes a product from the current order
     * 
     * @param product the product to be deleted
     * @return true if succesful
     */
    @Override
    public boolean removeProductFromOrder(final Product product) {
        if (this.productList.contains(product)) {
            this.productList.remove(product);
            OrderProduct orderProductToRemove = null;

            for (final OrderProduct op : orderProducts) {
                if (op.getProduct().equals(product)) {
                    if (op.getAmount() > 1) {
                        op.setAmount(op.getAmount() - 1);
                    } else {
                        orderProductToRemove = op;
                    }
                }
            }
            if (orderProductToRemove != null) {
                orderProducts.remove(orderProductToRemove);
            }
            this.totalPrice -= product.getPrice();
            return true;
        } else
            return false;
    }

    /**
     * Setter for totalprice
     * 
     * @param totalPrice price of all products in the order
     */
    public void setTotalPrice(final int totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return displays the order as a string
     */
    @Override
    public String toString() {
        return "Order{" +
                "productList=" + productList +
                ", totalPrice=" + totalPrice +
                '}';
    }

    /**
     * Overwritten equals method for comparing productlists
     * 
     * @param o input object
     * @return true if same
     */
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        final Order order = (Order) o;
        return this.productList == order.productList;
    }
}