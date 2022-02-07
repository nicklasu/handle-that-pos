package model.classes;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product{
public Product(){

}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int     id;

    @Column(name="name")
    private String  name;
    @Column(name="desc")
    private String  description;
    @Column(name="price")
    private float   price;
    @Column(name="stock")
    private int     stock;


    public Product(int id, String name, String description, float price, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public int      getId() { return id; }

    public void     setId(int id) { this.id = id; }

    public String   getName() {
        return name;
    }

    public void     setName(String name) {
        this.name = name;
    }

    public String   getDescription() {
        return description;
    }

    public void     setDescription(String description) {
        this.description = description;
    }

    public float    getPrice() {
        return price;
    }

    public void     setPrice(float price) {
        this.price = price;
    }

    public int      getStock() {
        return stock;
    }

    public void     setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
