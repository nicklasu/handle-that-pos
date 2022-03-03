package model.classes;

import javax.persistence.*;

@Entity
@Table(name = "Asiakas")
public class Customer {

    @Id
    @GeneratedValue // voidaan käyttää ei generated valueta
    @Column(name = "ID")
    private int id;

    @Column(name = "Asiakkuus")
    private int customerLevelIndex;

    public Customer() {
    }

    public Customer(int id, int customerLevelIndex) {
        this.id = id;
        // this.customerLevel = customerLevel;
        this.customerLevelIndex = customerLevelIndex; //CustomerLevel.valueOf(customerLevel.name()).ordinal();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new RuntimeException("Customer can't have a negative ID");
        }
        this.id = id;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", customerLevelIndex=" + customerLevelIndex +
                '}';
    }
}
