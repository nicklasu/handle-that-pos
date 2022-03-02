package POS;


import model.classes.*;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DAOTests extends TestParent{
    public DAOTests() {
    }

    @BeforeAll
    public void beforeAll() {
        System.out.println("DAO tests...");
    }

    @BeforeEach
    public void beforeEach() {

    }

    @Test
    public void productDAO() {
        Product a = new Product("1a", "Runebergin torttu", "Bergin Rune nautti näitä joka päivä aamiaiseksi", 200, 1);
        ProductDAO pd = new ProductDAO();
        Assertions.assertNull(pd.getProduct(a.getId()), "Problem with getting a non-existent product with dao");
        pd.addProduct(a);
        Assertions.assertEquals(a.toString(), pd.getProduct("1a").toString(), "Problem with adding/getting a product with dao");
        a.setPrice(1);
        pd.updateProduct(a);
        Assertions.assertEquals(1, pd.getProduct(a.getId()).getPrice(), "Problem with updating a product with dao");
        pd.deleteProduct(a.getId());
        Assertions.assertNull(pd.getProduct(a.getId()), "Problem with removing a product with dao");
    }

    @Disabled
    @Test
    public void transactionDAO(){
        User u = new User("junit", "tester", "JUN", "123", 1);
        TransactionDAO td = new TransactionDAO();
        Transaction t = super.createTestTransaction(u);
        Assertions.assertNull(td.getTransaction(t), "Error getting a non-existent transaction with DAO");
        td.addTransaction(t);
        Assertions.assertEquals(t, td.getTransaction(t), "Error finding a transaction with dao");
        td.removeTransaction(t);
        Assertions.assertNull(td.getTransaction(t), "Error removing a transaction with dao");
    }
}
