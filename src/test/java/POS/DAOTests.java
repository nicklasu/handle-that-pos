package POS;


import model.classes.*;
import org.junit.jupiter.api.*;

import java.sql.Timestamp;
import java.util.Date;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DAOTests extends TestParent {
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
        Product a = new Product("testID", "Runebergin torttu", "Bergin Rune nautti näitä joka päivä aamiaiseksi", 200, 1);
        ProductDAO pd = new ProductDAO();
        Assertions.assertNull(pd.getProduct(a.getId()), "Problem with getting a non-existent product with dao");
        pd.addProduct(a);
        Assertions.assertEquals(a.toString(), pd.getProduct("testID").toString(), "Problem with adding/getting a product with dao");
        a.setPrice(1);
        pd.updateProduct(a);
        Assertions.assertEquals(1, pd.getProduct(a.getId()).getPrice(), "Problem with updating a product with dao");
        pd.deleteProduct(a.getId());
        Assertions.assertNull(pd.getProduct(a.getId()), "Problem with removing a product with dao");
    }


    @Test
    public void transactionDAO() {
        CustomerDAO cd = new CustomerDAO();
        Customer c = new Customer(222,1);
        Date date = new Date();
        POSEngine posE = new POSEngine();
        UserDAO ud = new UserDAO();
        User u = new User("junit", "tester", "JUN", "123", 1);
        TransactionDAO td = new TransactionDAO();

        cd.addCustomer(c);
        Transaction t = createTestTransaction(u);
        t.setTimestamp(new Timestamp(date.getTime()));
        t.setPos(posE);
        posE.setTransaction(t);
        posE.confirmTransaction(false,c);

        Assertions.assertEquals(t.getID(), td.getTransaction(t).getID(), "Error finding a transaction with dao");
        td.removeTransaction(t);
        cd.deleteCustomer(c);
        ud.deleteUser(u);
        Assertions.assertNull(td.getTransaction(t), "Error removing a transaction with dao");
    }

    @Disabled
    @Test
    public void userDAO(){

    }
}
