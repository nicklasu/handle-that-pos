package POS;

import model.classes.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    private Order testOrder;
    private Transaction testTransaction;
    private User testUser;

    private void createTestOrder(){
        Product[] testProducts = {new Product(0, "Suola", "Kananmunan päälle naminami", 200, 100), new Product(1, "Sokeri", "Kahviin slurps", 100, 100)};
        testOrder = new Order();
        testOrder.addProductToOrder(testProducts[0]);
        testOrder.addProductToOrder(testProducts[1]);
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Transaction test...");
    }

    @BeforeEach
    void setUp() {
        testUser = new User(1, "testi", "käyttäjä", "testuser", "salis", 1);
        testTransaction = new Transaction(testUser);
    }

    @Test
    void getAndSetOrder() {
        createTestOrder();
        testTransaction.setOrder(testOrder);
        assertEquals(testTransaction.getOrder().getProductList().toString(), "[Suola, Sokeri]", "Error in linking order to transaction");
    }

    @Test
    void getAndSetCustomer() {
        Customer testCustomer = new Customer(3992,CustomerLevel.NONE);
        testTransaction.setCustomer(testCustomer);
        assertEquals(testTransaction.getCustomer().toString(),"Customer{id=3992, customerLevel=NONE}", "Error in handling customers with a transaction");
    }


    @Test
    void getAndSetPaymentMethod() {
        testTransaction.setPaymentMethod(PaymentMethod.CASH);
        assertEquals(testTransaction.getPaymentMethod(), PaymentMethod.CASH, "Error handling basic paymentmethods in transaction");
    }
}