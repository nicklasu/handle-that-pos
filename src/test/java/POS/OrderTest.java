package POS;

import model.classes.Order;
import model.classes.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;



class OrderTest {
     Order testOrder;
     double DELTA = 0.0001;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Ordertest...");
    }

    @BeforeEach
    void beforeEach() {
        Product[] testProducts = {
                new Product(0, "Suola", "Kananmunan päälle naminami", 200, 100),
                new Product(1,"Sokeri","Kahviin slurps", 200, 200)
        };
        testOrder = new Order();
        testOrder.addProductToOrder(testProducts[0]);
        testOrder.addProductToOrder(testProducts[1]);
    }

    @Test
    void getProductList() {
        ArrayList<Product> testProducts = new ArrayList<>();
        testProducts.add(new Product(0, "Suola", "Kananmunan päälle naminami", 200, 100));
        testProducts.add(new Product(1,"Sokeri","Kahviin slurps", 200, 200));

        assertEquals(testOrder.getProductList().toString(), testProducts.toString(), "Problem in getting an order");
    }

    @Test
    void getTotalPrice() {
        assertEquals(testOrder.getTotalPrice(),1.5F, DELTA, "Problem in checking total price of an order");
    }

    @Test
    void addProductToOrder() {
        testOrder.addProductToOrder(new Product(6, "Sinaappi", "Makkaran päälle jes", 200, 5));
        assertEquals(testOrder.getProductList().toString(), "[Suola, Sokeri, Sinaappi]", "Problem adding a product to an order");
    }

    @Test
    void removeProductFromOrder() {
        testOrder.removeProductFromOrder(new Product(0, "Suola", "Kananmunan päälle naminami", 200, 100));
        assertEquals(testOrder.getProductList().toString(), "[Sokeri]", "removing a product from order did not work");
    }

    @Test
    void CombinationTestForOrder1(){
        testOrder.addProductToOrder(new Product(6, "Sinaappi", "Makkaran päälle jes", 200, 5));
        testOrder.removeProductFromOrder(new Product(0, "Suola", "Kananmunan päälle naminami", 200, 100));
        assertEquals(testOrder.getProductList().toString(),"[Sokeri, Sinaappi]", "Listing order of products in an order has problems");
    }

    @Test
    void CombinationTestForOrder2(){
        testOrder.addProductToOrder(new Product(6, "Sinaappi", "Makkaran päälle jes", 200, 5));
        testOrder.addProductToOrder(new Product(6, "Sinaappi", "Makkaran päälle jes", 200, 5));
        testOrder.removeProductFromOrder(new Product(0, "Suola", "Kananmunan päälle naminami", 200, 100));
        testOrder.removeProductFromOrder(new Product(6, "Sinaappi", "Makkaran päälle jes", 200, 5));
        assertEquals(testOrder.getTotalPrice(), 3F, "Problem in getting total price of order after toying with it");
    }
}