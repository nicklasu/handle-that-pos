package POS;

import model.classes.Customer;
import model.classes.CustomerLevel;
import model.classes.POSEngine;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class POSEngineTest extends TestParent {
    private POSEngine testEngine;

    public POSEngineTest() {
    }

    @BeforeAll
    public void beforeAll() {
        System.out.println("POSEngine test...");
        testEngine = new POSEngine();
    }

    @BeforeEach
    public void setUp() {
        testEngine.login("testuser", "123");
    }

    @Test
    public void login() {
        Assertions.assertEquals(testEngine.login("testuser", "123"), 0, "logging in to testuser doesn't work properly");
        Assertions.assertEquals(testEngine.login("testuser", "asd"), 1, "logging in with wrong password works");
    }

    @Test
    public void logout() {
        testEngine.logout();
        Assertions.assertNull(testEngine.getUser(), "problem with logging out");
    }

    @Test
    public void getUser() {
        Assertions.assertEquals("User{id=1, username='testuser', password='$2a$12$QHOXCl8u2OzUpMWRM5oV3eB4rRHTvtyZhgEppjJSJKK2OFKAeN0va', fName='Testi', lName='User', activity=1}", testEngine.getUser().toString(), "error getting logged in user");
    }

    @Test
    public void getTransaction() {
        testEngine.setTransaction(this.createTestTransaction(testEngine.getUser()));
        Assertions.assertEquals("Transaction{order=Order{productList=[Suola, Sokeri], totalPrice=300}, customer=Customer{id=0, customerLevelIndex=0}, paymentMethod=CASH, user=User{id=1, username='testuser', password='$2a$12$QHOXCl8u2OzUpMWRM5oV3eB4rRHTvtyZhgEppjJSJKK2OFKAeN0va', fName='Testi', lName='User', activity=1}}", testEngine.getTransaction().toString(), "Problem getting the transaction from engine");
    }

    @Test

    public void scanProduct() {
        testEngine.scanProduct("12345678");
        testEngine.scanProduct("ASDFGHJK");
        Assertions.assertEquals(1500, testEngine.getTransaction().getOrder().getTotalPrice(), "problems with scanning products and adding them to order");
    }

    @Test
    public void confirmTransaction() {
        testEngine.setTransaction(createTestTransaction(testEngine.getUser()));
        testEngine.confirmTransaction(false, new Customer(1));
        Assertions.assertNull(testEngine.getTransaction(), "Problem with confirming transaction");
    }
}
