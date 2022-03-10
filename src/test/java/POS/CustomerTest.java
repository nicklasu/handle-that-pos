package POS;

import model.classes.Customer;
import model.classes.User;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerTest extends TestParent {
    Customer customer;

    public CustomerTest() {
    }

    @BeforeAll
    public void beforeAll() {
        System.out.println("Customer test...");
    }

    @BeforeEach
    public void setUp() {
        this.customer = new Customer(0);
    }

    @Test
    public void setId() {
        customer.setId(1);
        Assertions.assertEquals(1, this.customer.getId(), "Setting ID does not work");
    }

    @Test
    public void getId() {
        customer.setId(0);
        Assertions.assertEquals(0, this.customer.getId(), "Getting ID does not work");
    }

    @Test
    public void getCustomerLevelIndex() {
        Assertions.assertEquals(0, this.customer.getCustomerLevelIndex(), "getting CustomerLevelIndex does not work");
    }

    @Test
    public void toStringTest() {
        System.out.println(customer.toString());
        Assertions.assertEquals("Customer{id=0, customerLevelIndex=0}", this.customer.toString(), "toString() does not work");
    }

}
