package POS;

import model.classes.Product;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductTest extends TestParent {
    Product test;

    public ProductTest() {
    }

    @BeforeAll
    public void beforeAll() {
        System.out.println("Product test...");
    }

    @BeforeEach
    public void setUp() {
        this.test = new Product("1a", "Runebergin torttu", "Bergin Rune nautti näitä joka päivä aamiaiseksi", 200, 100);
    }

    @Test
    public void getId() {
        Assertions.assertEquals("1a", this.test.getId(), "Getting ID does not work");
    }

    @Test
    public void setId() {
        this.test.setId("4ab2");
        Assertions.assertEquals("4ab2", this.test.getId(), "Setting ID does not work");
    }

    @Test
    public void getName() {
        Assertions.assertEquals("Runebergin torttu", this.test.getName(), "Getting name does not work");
    }

    @Test
    public void setName() {
        this.test.setName("Testinimi");
        Assertions.assertEquals("Testinimi", this.test.getName(), "Setting name does not work");
    }

    @Test
    public void getDescription() {
        Assertions.assertEquals("Bergin Rune nautti näitä joka päivä aamiaiseksi", this.test.getDescription(), "Getting description does not work");
    }

    @Test
    public void setDescription() {
        this.test.setDescription("Test description");
        Assertions.assertEquals("Test description", this.test.getDescription(), "Setting description does not work");
    }

    @Test
    public void getPrice() {
        Assertions.assertEquals(200, this.test.getPrice(), "Getting price does not work");
    }

    @Test
    public void setPrice() {
        this.test.setPrice(900);
        Assertions.assertEquals(900, this.test.getPrice(), "Setting price does not work");
    }

    @Test
    public void getStock() {
        Assertions.assertEquals(100, this.test.getStock(), "Getting stock does not work");
    }

    @Test
    public void setStock() {
        this.test.setStock(3);
        Assertions.assertEquals(3, this.test.getStock(), "Setting stock does not work");
    }

    @Test
    public void testToString() {
        Assertions.assertEquals("Runebergin torttu", this.test.toString(), "ToString problem");
    }

    @Test
    public void testNegativePrice() {
        RuntimeException poikkeus = (RuntimeException) Assertions.assertThrows(RuntimeException.class, () -> {
            this.test.setPrice(-1);
        });
        Assertions.assertEquals("Negative price for a product is not accepted", poikkeus.getMessage());
    }

    @Test
    public void testNegativeStock() {
        RuntimeException poikkeus = (RuntimeException) Assertions.assertThrows(RuntimeException.class, () -> {
            this.test.setStock(-1);
        });
        Assertions.assertEquals("Negative stock for a product is not accepted", poikkeus.getMessage());
    }
}
