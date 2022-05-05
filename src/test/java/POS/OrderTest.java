package POS;

import model.classes.*;
import model.classes.Order;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderTest extends TestParent {
    private Order testOrder;

    public OrderTest() {
    }

    @BeforeAll
    public void beforeAll() {
        System.out.println("Order test...");
    }

    /***
     * Initializes testProducts array (order) and fills it with products objects.
     */
    @BeforeEach
    public void beforeEach() {
        final Product[] testProducts = new Product[] {
                new Product("0", "Suola", "Kananmunan päälle naminami", 200, 100),
                new Product("1", "Sokeri", "Kahviin slurps", 100, 100) };
        this.testOrder = new Order(new Transaction(new User()));
        this.testOrder.addProductToOrder(testProducts[0]);
        this.testOrder.addProductToOrder(testProducts[1]);
    }

    /***
     * Tests id getter or setter for the order.
     */
    @Test
    public void getAndSetTests() {
        this.testOrder.setId(1);
        Assertions.assertEquals(1, this.testOrder.getId(), "Id getter or setter for order has a problem");
        this.testOrder.setTransaction(new Transaction(new User("TEST", "TEST", "TEST", "TEST", 1)));
        Assertions.assertEquals("TEST", this.testOrder.getTransaction().getUser().getfName(),
                "Error setting or getting a transaction from order");
        this.testOrder.setTotalPrice(2);
        Assertions.assertEquals(2, this.testOrder.getTotalPrice());
        // this.testOrder.setOrderProducts(); //TODO setorderproduct testit
        // Assertions.assertEquals();
    }

    /***
     * Tests comparing order with equals.
     */
    @Test
    public void comparing() {
        Assertions.assertFalse(this.testOrder.equals(new Order()), "Error comparing orders with equals");
    }

    /***
     * Tests getting an order.
     */
    @Test
    public void getProductList() {
        Assertions.assertEquals("[Suola, Sokeri]", this.testOrder.getProductList().toString(),
                "Problem in getting an order");
    }

    /***
     * Tests total price of an order.
     */
    @Test
    public void getTotalPrice() {
        Assertions.assertEquals(300, this.testOrder.getTotalPrice(), "Problem in checking total price of an order");
    }

    /***
     * Tests that a new product can be added to an order.
     */
    @Test
    public void addProductToOrder() {
        this.testOrder.addProductToOrder(new Product("6", "Sinaappi", "Makkaran päälle jes", 300, 5));
        Assertions.assertEquals("[Suola, Sokeri, Sinaappi]", this.testOrder.getProductList().toString(),
                "Problem adding a product to an order");
    }

    /***
     * Tests that a product can be removed from an order.
     */
    @Test
    public void removeProductFromOrder() {
        this.testOrder.removeProductFromOrder(new Product("0", "Suola", "Kananmunan päälle naminami", 200, 100));
        Assertions.assertEquals("[Sokeri]", this.testOrder.getProductList().toString(),
                "removing a product from order did not work");
    }

    /***
     * Tests listing order of products in an order.
     */
    @Test
    public void CombinationTestForOrder1() {
        this.testOrder.addProductToOrder(new Product("6", "Sinaappi", "Makkaran päälle jes", 300, 5));
        this.testOrder.removeProductFromOrder(new Product("0", "Suola", "Kananmunan päälle naminami", 200, 100));
        Assertions.assertEquals("[Sokeri, Sinaappi]", this.testOrder.getProductList().toString(),
                "Listing order of products in an order has problems");
    }

    /***
     * Tests getting total price of order after toying with it.
     */
    @Test
    public void CombinationTestForOrder2() {
        this.testOrder.addProductToOrder(new Product("6", "Sinaappi", "Makkaran päälle jes", 300, 5));
        this.testOrder.addProductToOrder(new Product("6", "Sinaappi", "Makkaran päälle jes", 300, 5));
        this.testOrder.removeProductFromOrder(new Product("0", "Suola", "Kananmunan päälle naminami", 200, 100));
        this.testOrder.removeProductFromOrder(new Product("6", "Sinaappi", "Makkaran päälle jes", 300, 5));
        Assertions.assertEquals(400, this.testOrder.getTotalPrice(),
                "Problem in getting total price of order after toying with it");
    }

    /***
     * Tests calculating price with large quantities of products in the order.
     */
    @Test
    public void PriceOfLots() {
        final Product[] testProducts = new Product[] {
                new Product("0b", "Suola", "Kananmunan päälle naminami", 200, 100) };
        this.testOrder = new Order(new Transaction(new User()));

        for (int i = 0; i < 100; ++i) {
            this.testOrder.addProductToOrder(testProducts[0]);
        }

        Assertions.assertEquals(20000, this.testOrder.getTotalPrice(), "Error handling price with large quantities");
    }

    /***
     * Tests getting an empty order.
     */
    @Test
    public void GetEmptyOrder() {
        final Order emptyOrder = new Order(new Transaction(new User()));
        Assertions.assertEquals(0, emptyOrder.getProductList().size(), "Error getting an empty order");
    }
}
