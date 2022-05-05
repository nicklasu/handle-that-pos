package POS;

import model.classes.LoginStatus;
import model.classes.POSEngine;
import model.classes.Privilege;
import model.classes.PrivilegeLevel;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for POSEngine class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class POSEngineTest extends TestParent {
    private POSEngine testEngine;

    public POSEngineTest() {
    }

    /**
     * Creates new instance of POSEngine class before all tests
     */
    @BeforeAll
    public void beforeAll() {
        System.out.println("POSEngine test...");
        testEngine = new POSEngine("testiengine");
    }

    /**
     * Login before each test
     */
    @BeforeEach
    public void setUp() {
        testEngine.login("testuser", "123");
    }

    /**
     * Logout after each test
     */
    @AfterEach
    public void logoutAfterEach() {
        testEngine.logout();
    }

    /**
     * Test for POSEngine's login()
     */
    @Test
    public void login() {
        Assertions.assertEquals(LoginStatus.SUCCESS, testEngine.login("testuser", "123"),
                "logging in to testuser doesn't work properly!");
        Assertions.assertEquals(LoginStatus.WRONG_CREDENTIALS, testEngine.login("testuser", "asd"), "logging in with wrong password works!");
    }

    /**
     * Test for POSEngine's logout()
     */
    @Test
    public void logout() {
        testEngine.logout();
        Assertions.assertNull(testEngine.getUser(), "problem with logging out");
    }

    /**
     * Test for POSEngine's getUser()
     */
    @Test
    public void getUser() {
        Assertions.assertEquals("tester", testEngine.getUser().getfName(), "error getting logged in user");
    }

    /**
     * Test for POSEngine's getTransaction()
     */
    @Test
    public void getTransaction() {
        testEngine.setTransaction(this.createTestTransaction(testEngine.getUser()));
        Assertions.assertEquals("tester", testEngine.getTransaction().getUser().getfName(),
                "Problem getting the transaction from engine");
    }

    /**
     * Test for POSEngine's scanProduct()
     */
    @Test
    public void scanProduct() {
        testEngine.scanProduct("1a");
        testEngine.scanProduct("1b");
        Assertions.assertEquals(300, testEngine.getTransaction().getOrder().getTotalPrice(),
                "problems with scanning products and adding them to order");
    }

    /**
     * Test for POSEngine's confirmTransaction()
     */
    @Test
    public void confirmTransaction() {
        testEngine.setTransaction(createTestTransaction(testEngine.getUser()));
        testEngine.confirmTransaction(false);
        Assertions.assertNull(testEngine.getTransaction(), "Problem with confirming transaction");
    }

    /**
     * Test for POSEngine's privileges()
     * Tests if getting and setting privileges for user works
     */
    @Test
    public void privileges() {
        final List<Integer> luvat = testEngine.getVerifiedPrivileges();
        Assertions.assertEquals(3, luvat.get(0), "Getting privilegelevel has a problem");
        Assertions.assertEquals("testuser", testEngine.getPrivileges().get(0).getUser().getUsername(),
                "getting privileges has a problem");
        Assertions.assertEquals(3, testEngine.getPrivilegeIndexes().get(0), "Getting privilege indexes has a problem");

        final java.sql.Date date = new Date(123);
        final java.sql.Date date2 = new Date(1234);
        final List<Privilege> a = new ArrayList<>();
        a.add(new Privilege(testEngine.getUser(), date, date, PrivilegeLevel.ADMIN));
        a.add(new Privilege(testEngine.getUser(), date2, date2, PrivilegeLevel.MANAGER));
        testEngine.setPrivileges(a);
        Assertions.assertEquals(2, testEngine.getPrivileges().get(1).getPrivilegeLevelIndex(),
                "problem with adding privileges");
    }
}
