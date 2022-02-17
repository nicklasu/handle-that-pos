package model.classes;

import javax.persistence.*;

@Entity
@Table(name = "Asiakas")
public class Customer {

    @Id
    @GeneratedValue // voidaan käyttää ei generated valueta
    @Column(name = "ID")
    private int id;

    @Transient
    private CustomerLevel customerLevel;

    @Column(name = "Asiakkuus")
    private int customerLevelIndex;

    public Customer(){}

    public Customer(int id, CustomerLevel customerLevel) {
        this.id = id;
        this.customerLevel = customerLevel;
        this.customerLevelIndex = 1; //CustomerLevel.valueOf(customerLevel.name()).ordinal();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        if(id<0){
            throw new RuntimeException("Customer can't have a negative ID");
        }
        this.id = id;
    }

    public CustomerLevel getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(CustomerLevel customerLevel) {
        this.customerLevel = customerLevel;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", customerLevel=" + customerLevel +
                '}';
    }
}
