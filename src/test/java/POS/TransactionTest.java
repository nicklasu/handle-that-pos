package POS;

import model.classes.*;
import model.classes.Order;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionTest extends TestParent {
    private Transaction testTransaction;

    public TransactionTest() {
    }

    @BeforeAll
    public void beforeAll() {
        System.out.println("Transaction test...");
    }

    @BeforeEach
    public void setUp() {
        this.testTransaction = new Transaction(new User());
    }

    @Test
    public void getAndSetOrder() {
        Order testOrder = this.createTestOrder();
        this.testTransaction.setOrder(testOrder);
        Assertions.assertEquals(this.testTransaction.getOrder().getProductList().toString(), "[Suola, Sokeri]", "Error in linking order to transaction");
    }

    @Test
    public void getAndSetCustomer() {
        Customer testCustomer = new Customer(3992, CustomerLevel.NONE);
        this.testTransaction.setCustomer(testCustomer);
        Assertions.assertEquals(this.testTransaction.getCustomer().toString(), "Customer{id=3992, customerLevel=NONE}", "Error in handling customers with a transaction");
    }

    @Test
    public void getAndSetPaymentMethod() {
        this.testTransaction.setPaymentMethod(PaymentMethod.CASH);
        Assertions.assertEquals(this.testTransaction.getPaymentMethod(), PaymentMethod.CASH, "Error handling basic paymentmethods in transaction");
    }
}