package POS;

import model.classes.Customer;
import org.junit.jupiter.api.*;

/**
 * This class tests the Customer class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerTest extends TestParent {
    Customer customer;

    public CustomerTest() {
    }

    /**
     * Before all tests, print a line to indicate the start of the test
     */
    @BeforeAll
    public void beforeAll() {
        System.out.println("Customer test...");
    }

    /**
     * Test for creating a new customer with customer level index 0.
     */
    @BeforeEach
    public void setUp() {
        this.customer = new Customer(0);
    }

    /**
     * Test of setId method, of class Customer. Test if the id is set correctly.
     */
    @Test
    public void setId() {
        customer.setId(1);
        Assertions.assertEquals(1, this.customer.getId(), "Setting ID does not work");
    }

    /**
     * Test of getId method, of class Customer. Test if the id is correct.
     */
    @Test
    public void getId() {
        customer.setId(0);
        Assertions.assertEquals(0, this.customer.getId(), "Getting ID does not work");
    }

    /**
     * Test of getCustomerLevelIndex method, of class Customer. Test if the customer level index is correct.
     */
    @Test
    public void getCustomerLevelIndex() {
        Assertions.assertEquals(0, this.customer.getCustomerLevelIndex(), "getting CustomerLevelIndex does not work");
    }

    /**
     * Test of toStringTest method, of class Customer. Test if the toString method returns the correct string.
     */
    @Test
    public void toStringTest() {
        System.out.println(customer.toString());
        Assertions.assertEquals("Customer{id=0, customerLevelIndex=0}", this.customer.toString(), "toString() does not work");
    }

}
