package model.DAOs;

import model.classes.HibernateUtil;
import model.classes.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Data access object for transactions
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and Samu Luoma
 */
public class TransactionDAO {
    /**
     * This handles the connections to database
     */
    private SessionFactory sessionFactory = null;

    /**
     * Gets an instance of sessionfactory
     */
    public TransactionDAO() {
        try {
            sessionFactory = HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            System.out.println("Virhe istuntotehtaan luomisessa");
            e.printStackTrace();
        }
    }

    /**
     * Adds a new transaction to the database
     * @param transaction transaction to be added
     */
    public void addTransaction(model.classes.Transaction transaction) {
        Transaction t = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            t = session.beginTransaction();
            session.save(transaction);
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                t.rollback();
            }
        }
    }

    /**
     * removes a transaction from the database
     * @param transaction transaction to be removed
     */
    public void removeTransaction(model.classes.Transaction transaction) {
        Transaction t = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            t = session.beginTransaction();
            session.remove(transaction);
            t.commit();
        } catch (Exception e) {
            if (transaction != null) {
                t.rollback();
            }
        }
    }

    /**
     * Fetches a transaction from the database
     * @param transaction transaction to be fetched using the id of it
     * @return a transaction
     */
    public model.classes.Transaction getTransaction(model.classes.Transaction transaction) {
        Transaction t = null;
        model.classes.Transaction tr = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            t = session.beginTransaction();
            tr = session.get(model.classes.Transaction.class, transaction.getId());
            t.commit();
        } catch (Exception e) {
            if (transaction != null) {
                t.rollback();
            }
        }
        return tr;
    }

    /**
     * Fetches all transactions associated with a user from the database
     * @param user the user whose transactions are to be fetched
     * @return list of transacitions
     */
    public List<model.classes.Transaction> getTransactions(User user) {
        Transaction transaction = null;
        List<model.classes.Transaction> tr = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            int userId = user.getId();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from Transaction where user = :userId");
            query.setInteger("userId", userId);
            tr = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                System.out.println(e);
                transaction.rollback();
            }
        }
        return tr;
    }
}
