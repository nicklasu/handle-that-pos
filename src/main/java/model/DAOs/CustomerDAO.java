package model.DAOs;

import model.classes.Customer;
import model.classes.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Data access object for customers
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and Samu Luoma
 */
public class CustomerDAO {
    /**
     * This handles the connections to database
     */
    private SessionFactory sessionFactory = null;

    /**
     * Gets an instance of sessionfactory
     */
    public CustomerDAO() {
        try {
            sessionFactory = HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            System.out.println("Virhe istuntotehtaan luomisessa");
            e.printStackTrace();
        }
    }

    /**
     * Gets a customer based on identifier number
     * @param id the identifier number
     * @return customer with that ID
     */
    public Customer getCustomer(int id) {
        Transaction transaction = null;
        Customer customer = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            customer = session.get(Customer.class, id);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return customer;
    }

    /**
     * Adds a customer to the database
     * @param c the customer to be added
     */
    public void addCustomer(Customer c) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.save(c);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    /**
     * Deletes a customer from the database
     * @param c customer to be deleted
     */
    public void deleteCustomer(Customer c) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.delete(c);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
