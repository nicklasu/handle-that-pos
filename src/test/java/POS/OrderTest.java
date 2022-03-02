package POS;

import model.classes.Order;
import model.classes.Product;
import model.classes.Transaction;
import model.classes.User;
import org.junit.jupiter.api.*;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderTest extends TestParent {
    private Order testOrder;

    public OrderTest() {
    }

    @BeforeAll
    public void beforeAll() {
        System.out.println("Order test...");
    }

    @BeforeEach
    public void beforeEach() {
        Product[] testProducts = new Product[]{new Product("0", "Suola", "Kananmunan päälle naminami", 200, 100), new Product("0", "Sokeri", "Kahviin slurps", 100, 100)};
        this.testOrder = new Order(new Transaction(new User()));
        this.testOrder.addProductToOrder(testProducts[0]);
        this.testOrder.addProductToOrder(testProducts[1]);
    }

    @Test
    public void getProductList() {
        Assertions.assertEquals("[Suola, Sokeri]", this.testOrder.getProductList().toString(), "Problem in getting an order");
    }

    @Test
    public void getTotalPrice() {
        Assertions.assertEquals(300, this.testOrder.getTotalPrice(), "Problem in checking total price of an order");
    }

    @Test
    public void addProductToOrder() {
        this.testOrder.addProductToOrder(new Product("6", "Sinaappi", "Makkaran päälle jes", 300, 5));
        Assertions.assertEquals("[Suola, Sokeri, Sinaappi]", this.testOrder.getProductList().toString(), "Problem adding a product to an order");
    }

    @Test
    public void removeProductFromOrder() {
        this.testOrder.removeProductFromOrder(new Product("0", "Suola", "Kananmunan päälle naminami", 200, 100));
        Assertions.assertEquals("[Sokeri]", this.testOrder.getProductList().toString(), "removing a product from order did not work");
    }

    @Test
    public void CombinationTestForOrder1() {
        this.testOrder.addProductToOrder(new Product("6", "Sinaappi", "Makkaran päälle jes", 300, 5));
        this.testOrder.removeProductFromOrder(new Product("0", "Suola", "Kananmunan päälle naminami", 200, 100));
        Assertions.assertEquals("[Sokeri, Sinaappi]", this.testOrder.getProductList().toString(), "Listing order of products in an order has problems");
    }

    @Test
    public void CombinationTestForOrder2() {
        this.testOrder.addProductToOrder(new Product("6", "Sinaappi", "Makkaran päälle jes", 300, 5));
        this.testOrder.addProductToOrder(new Product("6", "Sinaappi", "Makkaran päälle jes", 300, 5));
        this.testOrder.removeProductFromOrder(new Product("0", "Suola", "Kananmunan päälle naminami", 200, 100));
        this.testOrder.removeProductFromOrder(new Product("6", "Sinaappi", "Makkaran päälle jes", 300, 5));
        Assertions.assertEquals(400, this.testOrder.getTotalPrice(), "Problem in getting total price of order after toying with it");
    }

    @Test
    public void PriceOfLots() {
        Product[] testProducts = new Product[]{new Product("0b", "Suola", "Kananmunan päälle naminami", 200, 100)};
        this.testOrder = new Order(new Transaction(new User()));

        for (int i = 0; i < 100; ++i) {
            this.testOrder.addProductToOrder(testProducts[0]);
        }

        Assertions.assertEquals(20000, this.testOrder.getTotalPrice(), "Error handling price with large quantities");
    }

    @Disabled
    @Test
    public void GetEmptyOrder() {
        Order emptyOrder = new Order(new Transaction(new User()));
        Objects.requireNonNull(emptyOrder);
        RuntimeException poikkeus = (RuntimeException) Assertions.assertThrows(RuntimeException.class, emptyOrder::getProductList);
        Assertions.assertEquals("No products in the order", poikkeus.getMessage());
    }
}
