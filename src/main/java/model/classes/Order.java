package model.classes;

import model.interfaces.IOrder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Tilaus")
public class Order implements IOrder {
    private boolean bonusCustomer = false;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MaksutapahtumaID", referencedColumnName = "ID")
    private Transaction transaction;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "primaryKey.order", cascade = CascadeType.ALL)
    private Set<OrderProduct> orderProducts = new HashSet<>();

    @Transient
    private List<Product> productList = new ArrayList<>();

    @Transient
    private int totalPrice = 0;

    public Order() {
    }

    public Order(Transaction transaction) {
        this.transaction = transaction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Set<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(Set<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
    }

    @Override
    public List<Product> getProductList() {
        return this.productList;
    }

    @Override
    public int getTotalPrice() {
        if (transaction.getCustomer() != null) {
            return (int) (totalPrice * 0.95);
        }
        return totalPrice;
    }

    @Override
    public boolean addProductToOrder(Product product) {
        if (product != null) {
            Product productToAdd = product;
            try {
                if (!productList.contains(product)) {
                    OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setOrder(this);
                    orderProduct.setProduct(product);
                    addOrderProduct(orderProduct);
                } else {
                    for (OrderProduct op : orderProducts) {
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
            } catch (Exception e) {
                System.out.println("Error adding a product to the order " + e);
            }
        }
        return false;
    }

    @Override
    public boolean removeProductFromOrder(Product product) {
        if (this.productList.contains(product)) {
            this.productList.remove(product);
            OrderProduct orderProductToRemove = null;

            for (OrderProduct op : orderProducts) {
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
        } else return false;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "productList=" + productList +
                ", totalPrice=" + totalPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
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