package POS;

import model.classes.Product;
import model.classes.User;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTest extends TestParent {
    User test;

    public UserTest() {
    }

    @BeforeAll
    public void beforeAll() {
        System.out.println("User test...");
    }

    @BeforeEach
    public void setUp() {
        this.test = new User(1, "testi", "kayttaja", "testkayt", "salis", 1);
    }

    @Test
    public void getId() {
        Assertions.assertEquals(1, this.test.getId(), "Getting ID does not work");
    }

    @Test
    public void setId() {
        this.test.setId(2);
        Assertions.assertEquals(2, this.test.getId(), "Setting ID does not work");
    }

    @Test
    public void getUsername() {
        Assertions.assertEquals("testkayt", this.test.getUsername(), "Getting username does not work");
    }

    @Test
    public void setUsername() {
        this.test.setUsername("setUsername");
        Assertions.assertEquals("setUsername", this.test.getUsername(), "Setting username does not work");
    }

    @Test
    public void getPassword() {
        Assertions.assertEquals("salis", this.test.getPassword(), "Getting password does not work");
    }

    @Test
    public void setPassword() {
        this.test.setPassword("salasana");
        Assertions.assertEquals("salasana", this.test.getPassword(), "Setting password does not work");
    }

    @Test
    public void getfName() {
        Assertions.assertEquals("testi", this.test.getfName(), "Getting first name does not work");
    }

    @Test
    public void setfName() {
        this.test.setfName("testaaja");
        Assertions.assertEquals("testaaja", this.test.getfName(), "Setting first name does not work");
    }

    @Test
    public void getlName() {
        Assertions.assertEquals("kayttaja", this.test.getlName(), "Getting last name does not work");
    }

    @Test
    public void setlName() {
        this.test.setlName("kayttis");
        Assertions.assertEquals("kayttis", this.test.getlName(), "Setting last name does not work");
    }

    @Test
    public void getActivity() {
        Assertions.assertEquals(1, this.test.getActivity(), "Getting activity does not work");
    }

    @Test
    public void setActivity() {
        this.test.setActivity(0);
        Assertions.assertEquals(0, this.test.getActivity(), "Setting activity does not work");
    }

    @Test
    public void toStringTest() {
        Assertions.assertEquals("User{id=1, username='testkayt', password='salis', fName='testi', lName='kayttaja', activity=1}", this.test.toString(), "User toString method does not work");
    }

    @Test
    public void getFullName() {
        Assertions.assertEquals("testi kayttaja", this.test.getFullName(), "Getting full name does not work");
    }

}
