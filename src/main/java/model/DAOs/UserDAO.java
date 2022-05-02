package model.DAOs;

import model.classes.HibernateUtil;
import model.classes.User;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Data access object for users
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class UserDAO {
    /**
     * This handles the connections to database
     */
    private SessionFactory sessionFactory = null;

    /**
     * Gets an instance of sessionfactory
     * @param name name of database to use, pos for main and postesti for the test database
     */
    public UserDAO(String name) {
        String fileName = "hibernate.cfg.xml";
        if(name.equals("postesti")){
            fileName = "test.cfg.xml";
        }
        try {
            sessionFactory = HibernateUtil.getSessionFactory(fileName);
        } catch (final Exception e) {
            System.out.println("Virhe istuntotehtaan luomisessa");
            e.printStackTrace();
        }

    }

    /**
     * Fetches user based on ID
     * 
     * @param id identifier for the user
     * @return user object
     */
    public User getUserById(final int id) {
        Transaction transaction = null;
        User user = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            user = session.get(User.class, id);
            transaction.commit();
        } catch (final Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return user;
    }

    /**
     * Fetches user based on username
     * 
     * @param username username for the user
     * @return user object
     */
    public User getUser(final String username) {
        Transaction transaction = null;
        User user = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            final Query<User> query = session.createQuery("from User where username=:username");
            query.setParameter("username", username);
            final List<User> list = query.list();
            System.out.println(list);
            if (list.isEmpty()) {
                System.out.println("Käyttäjää ei löytynyt tietokannasta.");
                return null;
            }
            user = list.get(0);
            transaction.commit();
        } catch (final Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return user;
    }

    /**
     * Returns total value of sales by a specified user
     * @param u User object
     * @return int price of products in all transactions tied to parameter User
     */
    public int getSalesValueOfUser(User u) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            int toBeReturned = 0;
            transaction = session.beginTransaction();
            String sql = "SELECT Hinta FROM Tuote " +
                    "INNER JOIN Sisältyy ON Tuote.ID = Sisältyy.TuoteID " +
                    "INNER JOIN Tilaus ON Tilaus.ID = Sisältyy.TilausID " +
                    "INNER JOIN Maksutapahtuma ON Maksutapahtuma.KäyttäjäID = " +
                    "'" + u.getId()+ "'";
            SQLQuery query = session.createSQLQuery(sql);
            List<Object[]> rows = query.list();


            for(final Object o : rows) {
                toBeReturned += (Integer) o;
            }
            return toBeReturned;
        } catch (final Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return 0;
    }

    /**
     * Gets all users from the database
     * 
     * @return lits of users
     */
    public List<User> getAllUsers() {
        Transaction transaction = null;
        List<User> users = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            users = session.createQuery("from User").list();
            transaction.commit();
        } catch (final Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return users;
    }

    /**
     * Creates a new user in the database
     * 
     * @param user user to be created
     */
    public void createUser(final User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (final Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    /**
     * Deletes a user from the database
     * 
     * @param user user to be deleted
     */
    public void deleteUser(final User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
        } catch (final Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    /**
     * Updates an user in the database
     * 
     * @param user user to be modified
     */
    public void updateUser(final User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(user);
            transaction.commit();
        } catch (final Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
