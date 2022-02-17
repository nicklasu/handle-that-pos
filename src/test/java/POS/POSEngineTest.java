package POS;

import model.classes.POSEngine;
import org.junit.jupiter.api.*;

class POSEngineTest extends TestParent {
    private static POSEngine testEngine;

    @BeforeAll
    static void beforeAll() {
        System.out.println("POSEngine test...");
        testEngine = new POSEngine();
    }

    @BeforeEach
    void setUp() {
        testEngine.login("testuser", "123");
    }

    @Test
    void login() {
        Assertions.assertTrue(testEngine.login("testuser", "123"), "logging in to testuser doesn't work properly");
        Assertions.assertFalse(testEngine.login("testuser", "asd"), "logging in with wrong password works");
    }

    @Test
    void logout() {
        testEngine.logout();
        Assertions.assertNull(testEngine.getUser(), "problem with logging out");
    }

    @Test
    void getUser() {
        Assertions.assertEquals("User{id=1, username='testuser', password='$2a$12$QHOXCl8u2OzUpMWRM5oV3eB4rRHTvtyZhgEppjJSJKK2OFKAeN0va', fName='Testi', lName='User', activity=0}", testEngine.getUser().toString(), "error getting logged in user");
    }

    @Test
    void getTransaction() {
        testEngine.setTransaction(this.createTestTransaction(testEngine.getUser()));
        Assertions.assertEquals("Transaction{order=Order{productList=[Suola, Sokeri], totalPrice=300}, customer=Customer{id=3992, customerLevel=NONE}, paymentMethod=CASH, user=User{id=1, username='testuser', password='$2a$12$QHOXCl8u2OzUpMWRM5oV3eB4rRHTvtyZhgEppjJSJKK2OFKAeN0va', fName='Testi', lName='User', activity=0}}", testEngine.getTransaction().toString(), "Problem getting the transaction from engine");
    }

    @Test

    void scanProduct() {
        testEngine.scanProduct("12345678");
        testEngine.scanProduct("ASDFGHJK");
        Assertions.assertEquals(600, testEngine.getTransaction().getOrder().getTotalPrice(), "problems with scanning products and adding them to order");
    }

    @Test
    void confirmTransaction() {
        testEngine.setTransaction(this.createTestTransaction(testEngine.getUser()));
        testEngine.confirmTransaction();
        Assertions.assertNull(testEngine.getTransaction(), "Problem with confirming transaction");
    }
}
