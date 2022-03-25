package POS;

import model.classes.Customer;
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

    @AfterEach
    public void logoutAfterEach() {
        testEngine.logout();
    }



    @Test
    public void login() {
        Assertions.assertEquals(testEngine.login("testuser", "123"), 1, "logging in to testuser doesn't work properly!");
        Assertions.assertEquals(testEngine.login("testuser", "asd"), 0, "logging in with wrong password works!");
    }

    @Test
    public void logout() {
        testEngine.logout();
        Assertions.assertNull(testEngine.getUser(), "problem with logging out");
    }

    @Test
    public void getUser() {
        Assertions.assertEquals("Testi", testEngine.getUser().getfName(), "error getting logged in user");
    }

    @Test
    public void getTransaction() {
        testEngine.setTransaction(this.createTestTransaction(testEngine.getUser()));
        Assertions.assertEquals("Testi", testEngine.getTransaction().getUser().getfName(), "Problem getting the transaction from engine");
    }

    @Test

    public void scanProduct() {
        testEngine.scanProduct("1a");
        testEngine.scanProduct("1b");
        Assertions.assertEquals(300, testEngine.getTransaction().getOrder().getTotalPrice(), "problems with scanning products and adding them to order");
    }

    @Test
    public void confirmTransaction() {
        testEngine.setTransaction(createTestTransaction(testEngine.getUser()));
        testEngine.confirmTransaction(false);
        Assertions.assertNull(testEngine.getTransaction(), "Problem with confirming transaction");
    }
}
