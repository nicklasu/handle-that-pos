package POS;

import model.classes.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ProductTest {

    Product test;

    @BeforeEach
    void setUp() {
        test = new Product(1,"asd","asd", 1.0F, 100);
    }

    @Test
    void getId() {
        assertEquals(test.getId(), 1);
    }

    /*
    @Test
    void setId() {
    }

    @Test
    void getName() {
    }

    @Test
    void setName() {
    }

    @Test
    void getDescription() {
    }

    @Test
    void setDescription() {
    }

    @Test
    void getPrice() {
    }

    @Test
    void setPrice() {
    }

    @Test
    void getStock() {
    }

    @Test
    void setStock() {
    }

    @Test
    void testToString() {
    }*/
}