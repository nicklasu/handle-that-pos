package POS;

import model.classes.*;
import model.classes.Order;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionTest extends TestParent {
    private Transaction testTransaction;
    private POSEngine pos;

    public TransactionTest() {
    }

    @BeforeAll
    public void beforeAll() {
        System.out.println("Transaction test...");
        pos = new POSEngine();
        pos.login("testuser", "123");
    }

    @BeforeEach
    public void setUp() {
        this.testTransaction = new Transaction(pos.getUser());
    }

    @Test
    public void getAndSetOrder() {
        final Order testOrder = this.createTestOrder(pos.getUser(), testTransaction);
        this.testTransaction.setOrder(testOrder);
        Assertions.assertEquals("[Suola, Sokeri]", this.testTransaction.getOrder().getProductList().toString(),
                "Error in linking order to transaction");
    }

    @Test
    public void getAndSetCustomer() {
        final Customer testCustomer = new Customer(0);
        this.testTransaction.setCustomer(testCustomer);
        Assertions.assertEquals("Customer{id=0, customerLevelIndex=0}", this.testTransaction.getCustomer().toString(),
                "Error in handling customers with a transaction");
    }

    @Test
    public void getAndSetPaymentMethod() {
        this.testTransaction.setPaymentMethod(PaymentMethod.CASH);
        Assertions.assertEquals(this.testTransaction.getPaymentMethod(), PaymentMethod.CASH,
                "Error handling basic paymentmethods in transaction");
    }
}