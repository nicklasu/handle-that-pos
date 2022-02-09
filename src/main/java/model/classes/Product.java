package model.classes;

import javax.persistence.*;

@Entity
@Table(name = "Tuote")
public class Product{
public Product(){

}
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    private int id;

    @Column(name="Nimi")
    private String  name;
    @Column(name="Kuvaus")
    private String description;
    @Column(name="Hinta")
    private int price;
    @Column(name="Varastomäärä")
    private int stock;


    public Product(int id, String name, String description, int price, int stock) {
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

    public int    getPrice() {
        return price;
    }

    public void     setPrice(int price) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (o.getClass() != this.getClass()) { return false; }

        final Product product = (Product) o;

        return this.id == product.id;
    }
}
