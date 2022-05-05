package POS;

import model.classes.*;

/**
 * Abstract class for test classes to extend
 */
public abstract class TestParent {
    public TestParent() {
    }

    /**
     * Creates test instance of Order class
     *
     * @param user User
     * @param t Transaction
     * @return Order
     */
    protected Order createTestOrder(User user, Transaction t) {
        Product[] testProducts = new Product[]{new Product("1a", "Suola", "Kananmunan päälle naminami", 200, 100), new Product("1b", "Sokeri", "Kahviin slurps", 100, 100)};
        Order testOrder = new Order(t);
        testOrder.addProductToOrder(testProducts[0]);
        testOrder.addProductToOrder(testProducts[1]);
        return testOrder;
    }

    /**
     * Creates test instance of Transaction class
     *
     * @param user User
     * @return Transaction
     */
    protected Transaction createTestTransaction(User user) {
        Transaction testTransaction = new Transaction(user);
        Order testOrder = this.createTestOrder(user, testTransaction);
        testTransaction.setOrder(testOrder);
        Customer testCustomer = new Customer(0);
        testTransaction.setCustomer(testCustomer);
        testTransaction.setPaymentMethod(PaymentMethod.CASH);
        return testTransaction;
    }

}