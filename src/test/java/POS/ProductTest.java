package POS;

import model.classes.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ProductTest {

    Product test;

    @BeforeEach
    void setUp() {
        test = new Product(1, "Runebergin torttu", "Bergin Rune nautti näitä joka päivä aamiaiseksi", 200, 100);
    }

    @Test
    void getId() {
        assertEquals(test.getId(), 1, "Getting ID does not work");
    }

    @Test
    void setId() {
        test.setId(4);
        assertEquals(test.getId(), 4, "Setting ID does not work");
    }

    @Test
    void getName() {
        assertEquals(test.getName(), "Runebergin torttu", "Getting name does not work");
    }

    @Test
    void setName() {
        test.setName("Testinimi");
        assertEquals(test.getName(), "Testinimi", "Setting name does not work");
    }

    @Test
    void getDescription() {
        assertEquals(test.getDescription(), "Bergin Rune nautti näitä joka päivä aamiaiseksi", "Getting description does not work");
    }

    @Test
    void setDescription() {
        test.setDescription("Test description");
        assertEquals(test.getDescription(),"Test description", "Setting description does not work");
    }

    @Test
    void getPrice() {
        assertEquals(test.getPrice(), 200, "Getting price does not work");
    }

    @Test
    void setPrice() {
        test.setPrice(900);
        assertEquals(test.getPrice(), 900, "Setting price does not work");
    }

    @Test
    void getStock() {
        assertEquals(test.getStock(),100, "Getting stock does not work");
    }

    @Test
    void setStock() {
        test.setStock(3);
        assertEquals(test.getStock(),3,"Setting stock does not work");
    }

    @Test
    void testToString() {
        assertEquals(test.toString(),"Runebergin torttu", "ToString problem");
    }
}