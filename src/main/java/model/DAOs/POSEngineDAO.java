package model.DAOs;

import model.classes.HibernateUtil;
import model.classes.POSEngine;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Data access object for POSEngine, the device info running the software
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and Samu Luoma
 */
public class POSEngineDAO {
    /**
     * This handles the connections to database
     */
    private SessionFactory sessionFactory = null;

    /**
     * Gets an instance of sessionfactory
     */
    public POSEngineDAO() {
        try {
            sessionFactory = HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            System.out.println("Virhe istuntotehtaan luomisessa");
            e.printStackTrace();
        }
    }

    /**
     * Adds a POSEngine to the database if it does not exist there yet
     * @param pos the engine to be added
     */
    public void addID(POSEngine pos) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(pos);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
