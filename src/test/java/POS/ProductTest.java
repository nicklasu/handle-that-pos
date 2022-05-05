package POS;

import model.classes.Product;
import org.junit.jupiter.api.*;

/**
 * Tests the Product class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductTest extends TestParent {
    Product test;


    @BeforeAll
    public void beforeAll() {
        System.out.println("Product test...");
    }

    /**
     * Sets up the Runebergin torttu every time before the tests.
     */
    @BeforeEach
    public void setUp() {
        this.test = new Product("1a", "Runebergin torttu", "Bergin Rune nautti näitä joka päivä aamiaiseksi", 200, 100);
    }

    /**
     * Tests the getId getter of Product.
     */
    @Test
    public void getId() {
        Assertions.assertEquals("1a", this.test.getId(), "Getting ID does not work");
    }

    /**
     * Tests the setId setter of Product.
     */
    @Test
    public void setId() {
        this.test.setId("4ab2");
        Assertions.assertEquals("4ab2", this.test.getId(), "Setting ID does not work");
    }

    /**
     * Tests the getName getter of Product.
     */
    @Test
    public void getName() {
        Assertions.assertEquals("Runebergin torttu", this.test.getName(), "Getting name does not work");
    }

    /**
     * Tests the setName setter of Product.
     */
    @Test
    public void setName() {
        this.test.setName("Testinimi");
        Assertions.assertEquals("Testinimi", this.test.getName(), "Setting name does not work");
    }

    /**
     * Tests the getDescription getter of Product.
     */
    @Test
    public void getDescription() {
        Assertions.assertEquals("Bergin Rune nautti näitä joka päivä aamiaiseksi", this.test.getDescription(), "Getting description does not work");
    }

    /**
     * Tests the setDescription setter of Product.
     */
    @Test
    public void setDescription() {
        this.test.setDescription("Test description");
        Assertions.assertEquals("Test description", this.test.getDescription(), "Setting description does not work");
    }

    /**
     * Tests the getPrice getter of Product.
     */
    @Test
    public void getPrice() {
        Assertions.assertEquals(200, this.test.getPrice(), "Getting price does not work");
    }

    /**
     * Tests the setPrice setter of Product.
     */
    @Test
    public void setPrice() {
        this.test.setPrice(900);
        Assertions.assertEquals(900, this.test.getPrice(), "Setting price does not work");
    }

    /**
     * Tests getStock of getter of Product.
     */
    @Test
    public void getStock() {
        Assertions.assertEquals(100, this.test.getStock(), "Getting stock does not work");
    }

    /**
     * Tests setStock setter of Product.
     */
    @Test
    public void setStock() {
        this.test.setStock(3);
        Assertions.assertEquals(3, this.test.getStock(), "Setting stock does not work");
    }

    /**
     * Tests toString of Product.
     */
    @Test
    public void testToString() {
        Assertions.assertEquals("Runebergin torttu", this.test.toString(), "ToString problem");
    }

    /**
     * Tests so that negative prices can't be set.
     */
    @Test
    public void testNegativePrice() {
        this.test.setPrice(-1);
        Assertions.assertEquals(-1, this.test.getPrice(), "Returning negative price does not work");
    }

    /**
     * Tests so that stock can't be set to negative.
     */
    @Test
    public void testNegativeStock() {
        this.test.setStock(-1);
        Assertions.assertEquals(-1, this.test.getStock(), "Negative stock not accurate");
    }
}
