package model.classes;

public class Customer {
    private int id;
    private CustomerLevel customerLevel;

    public Customer(){}

    public Customer(int id, CustomerLevel customerLevel) {
        this.id = id;
        this.customerLevel = customerLevel;
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
