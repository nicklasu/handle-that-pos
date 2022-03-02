package POS;

import model.classes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest extends TestParent {
    private Transaction testTransaction;

    public TransactionTest() {
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Transaction test...");
    }

    @BeforeEach
    void setUp() {
        this.testTransaction = new Transaction(new User());
    }

    @Test
    void getAndSetOrder() {
        Order testOrder = this.createTestOrder();
        this.testTransaction.setOrder(testOrder);
        Assertions.assertEquals(this.testTransaction.getOrder().getProductList().toString(), "[Suola, Sokeri]", "Error in linking order to transaction");
    }

    @Test
    void getAndSetCustomer() {
        Customer testCustomer = new Customer(3992, CustomerLevel.NONE);
        this.testTransaction.setCustomer(testCustomer);
        Assertions.assertEquals(this.testTransaction.getCustomer().toString(), "Customer{id=3992, customerLevel=NONE}", "Error in handling customers with a transaction");
    }

    @Test
    void getAndSetPaymentMethod() {
        this.testTransaction.setPaymentMethod(PaymentMethod.CASH);
        Assertions.assertEquals(this.testTransaction.getPaymentMethod(), PaymentMethod.CASH, "Error handling basic paymentmethods in transaction");
    }
}