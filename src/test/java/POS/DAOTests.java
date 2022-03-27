package POS;


import model.DAOs.*;
import model.classes.*;
import org.junit.jupiter.api.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DAOTests extends TestParent {
    public DAOTests() {
    }

    @BeforeAll
    public void beforeAll() {
        System.out.println("DAO tests...");
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
        Customer c = new Customer(1);
        Date date = new Date();
        POSEngine posE = new POSEngine();
        UserDAO ud = new UserDAO();
        String name = UUID.randomUUID().toString();
        User u = new User("junit", "tester", name, "123", 1);
        TransactionDAO td = new TransactionDAO();

        cd.addCustomer(c);
        Transaction t = createTestTransaction(u);
        t.setTimestamp(new Timestamp(date.getTime()));
        t.setPos(posE);
        posE.setTransaction(t);
        posE.confirmTransaction(false);

        Assertions.assertEquals(t.getId(), td.getTransaction(t).getId(), "Error finding a transaction with dao");
        td.removeTransaction(t);
        cd.deleteCustomer(c);
        ud.deleteUser(u);
        Assertions.assertNull(td.getTransaction(t), "Error removing a transaction with dao");
    }

    @Test
    public void userDAO(){
        UserDAO ud = new UserDAO();
        String name = UUID.randomUUID().toString();
        Assertions.assertNull(ud.getUser(name), "Finding a nonexistant user should return null");
        User u = new User("junit", "tester", name, "123", 1);
        ud.createUser(u);
        Assertions.assertEquals(u.toString(),ud.getUser(name).toString(), "Finding a user fails");
        u.setlName("test2");
        ud.updateUser(u);
        Assertions.assertEquals("test2",ud.getUser(name).getlName(), "Updating user does not work");
        ud.deleteUser(u);
        Assertions.assertNull(ud.getUser(name), "Deleting user does not work");
    }

    @Test
    public void customerDAO(){
        CustomerDAO cd = new CustomerDAO();
        Customer c = new Customer(1);
        Assertions.assertNull(cd.getCustomer(c.getId()));
        cd.addCustomer(c);
        Assertions.assertEquals(c.toString(),cd.getCustomer(c.getId()).toString(), "Finding added customer has problems");
        cd.deleteCustomer(c);
        Assertions.assertNull(cd.getCustomer(c.getId()),"Deleting a customer has problems");
    }

    @Test
    public void PrivilegeDAO(){
        PrivilegeDAO pd = new PrivilegeDAO();
        String name = UUID.randomUUID().toString();
        java.sql.Date d1 = new java.sql.Date(new Date().getTime());
        java.sql.Date d2 = new java.sql.Date(3000, Calendar.JANUARY, 28);
        User u = new User("junit", "tester", name, "123", 1);
        UserDAO ud = new UserDAO();
        ud.createUser(u);
        Privilege p = new Privilege(u, d1, d2, PrivilegeLevel.MANAGER);

        Assertions.assertEquals(0, pd.getPrivileges(u).size(),"problem getting a nonexistant privilege");
        pd.addPrivilege(p);
        Assertions.assertEquals(name,pd.getPrivileges(u).get(0).getUser().getUsername(), "Error finding an added privilege");
        pd.deletePrivilege(p);
        ud.deleteUser(u);
    }

}
