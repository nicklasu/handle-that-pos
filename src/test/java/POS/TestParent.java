package POS;

import model.classes.*;

public abstract class TestParent {
    public TestParent() {
    }

    protected Order createTestOrder() {
        Product[] testProducts = new Product[]{new Product("1a", "Suola", "Kananmunan päälle naminami", 200, 100), new Product("1b", "Sokeri", "Kahviin slurps", 100, 100)};
        Order testOrder = new Order(new Transaction(new User()));
        testOrder.addProductToOrder(testProducts[0]);
        testOrder.addProductToOrder(testProducts[1]);
        return testOrder;
    }

    protected Transaction createTestTransaction(User user) {
        Transaction testTransaction = new Transaction(user);
        Order testOrder = this.createTestOrder();
        testTransaction.setOrder(testOrder);
        Customer testCustomer = new Customer(3992, 0);
        testTransaction.setCustomer(testCustomer);
        testTransaction.setPaymentMethod(PaymentMethod.CASH);
        return testTransaction;
    }

}