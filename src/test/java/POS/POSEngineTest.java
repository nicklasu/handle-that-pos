package POS;

import model.classes.POSEngine;
import model.classes.Privilege;
import model.classes.PrivilegeLevel;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
        Assertions.assertEquals("Test", testEngine.getUser().getfName(), "error getting logged in user");
    }

    @Test
    public void getTransaction() {
        testEngine.setTransaction(this.createTestTransaction(testEngine.getUser()));
        Assertions.assertEquals("Test", testEngine.getTransaction().getUser().getfName(), "Problem getting the transaction from engine");
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


    @Test
    public void privileges(){
        List<Integer> luvat = testEngine.getVerifiedPrivileges();
        Assertions.assertEquals(2, luvat.get(0), "Getting privilegelevel has a problem");
        Assertions.assertEquals("testuser",testEngine.getPrivileges().get(0).getUser().getUsername(),"getting privileges has a problem");
        Assertions.assertEquals(2,testEngine.getPrivilegeIndexes().get(0),"Getting privilege indexes has a problem");

        java.sql.Date date = new Date(123);
        java.sql.Date date2 = new Date(1234);
        List<Privilege> a = new ArrayList<>();
        a.add(new Privilege(testEngine.getUser(), date, date, PrivilegeLevel.ADMIN));
        a.add(new Privilege(testEngine.getUser(), date2, date2, PrivilegeLevel.MANAGER));
        testEngine.setPrivileges(a);
        Assertions.assertEquals(2,testEngine.getPrivileges().get(1).getPrivilegeLevelIndex(),"problem with adding privileges");
    }
}
