package POS;

import model.classes.User;
import org.junit.jupiter.api.*;

/**
 * Test class to test User class of model
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTest extends TestParent {
    User test;

    public UserTest() {
    }

    @BeforeAll
    public void beforeAll() {
        System.out.println("User test...");
    }

    /**
     * Creates test user before each test
     */
    @BeforeEach
    public void setUp() {
        this.test = new User(1, "testi", "kayttaja", "testkayt", "salis", 1);
    }

    /**
     * Test for User's getId()
     */
    @Test
    public void getId() {
        Assertions.assertEquals(1, this.test.getId(), "Getting ID does not work");
    }

    /**
     * Test for User's setId()
     */
    @Test
    public void setId() {
        this.test.setId(2);
        Assertions.assertEquals(2, this.test.getId(), "Setting ID does not work");
    }

    /**
     * Test for User's getUsername()
     */
    @Test
    public void getUsername() {
        Assertions.assertEquals("testkayt", this.test.getUsername(), "Getting username does not work");
    }

    /**
     * Test for User's setUsername()
     */
    @Test
    public void setUsername() {
        this.test.setUsername("setUsername");
        Assertions.assertEquals("setUsername", this.test.getUsername(), "Setting username does not work");
    }

    /**
     * Test for User's getPassword()
     */
    @Test
    public void getPassword() {
        Assertions.assertEquals("salis", this.test.getPassword(), "Getting password does not work");
    }

    /**
     * Test for User's setPassword()
     */
    @Test
    public void setPassword() {
        this.test.setPassword("salasana");
        Assertions.assertEquals("salasana", this.test.getPassword(), "Setting password does not work");
    }

    /**
     * Test for User's getfName()
     */
    @Test
    public void getfName() {
        Assertions.assertEquals("testi", this.test.getfName(), "Getting first name does not work");
    }

    /**
     * Test for User's setfName()
     */
    @Test
    public void setfName() {
        this.test.setfName("testaaja");
        Assertions.assertEquals("testaaja", this.test.getfName(), "Setting first name does not work");
    }

    /**
     * Test for User's getlName()
     */
    @Test
    public void getlName() {
        Assertions.assertEquals("kayttaja", this.test.getlName(), "Getting last name does not work");
    }

    /**
     * Test for User's setlName()
     */
    @Test
    public void setlName() {
        this.test.setlName("kayttis");
        Assertions.assertEquals("kayttis", this.test.getlName(), "Setting last name does not work");
    }

    /**
     * Test for User's getActivity()
     */
    @Test
    public void getActivity() {
        Assertions.assertEquals(1, this.test.getActivity(), "Getting activity does not work");
    }

    /**
     * Test for User's setActivity()
     */
    @Test
    public void setActivity() {
        this.test.setActivity(0);
        Assertions.assertEquals(0, this.test.getActivity(), "Setting activity does not work");
    }

    /**
     * Test for User's toString()
     */
    @Test
    public void toStringTest() {
        Assertions.assertEquals("User{id=1, username='testkayt', password='salis', fName='testi', lName='kayttaja', activity=1}", this.test.toString(), "User toString method does not work");
    }

    /**
     * Test for User's getFullName()
     */
    @Test
    public void getFullName() {
        Assertions.assertEquals("testi kayttaja", this.test.getFullName(), "Getting full name does not work");
    }

}
