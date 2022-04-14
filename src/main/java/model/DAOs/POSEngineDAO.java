package model.DAOs;

import model.classes.HibernateUtil;
import model.classes.POSEngine;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
/**
 * Data access object for POSEngine, the device info running the software
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class POSEngineDAO {
    /**
     * This handles the connections to database
     */
    private SessionFactory sessionFactory = null;

    /**
     * Gets an instance of sessionfactory
     * @param name name of database to use, pos for main and postesti for the test database
     */
    public POSEngineDAO(String name) {
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
     * Adds a POSEngine to the database if it does not exist there yet
     *
     * @param pos the engine to be added
     */
    public void addID(final POSEngine pos) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(pos);
            transaction.commit();
        } catch (final Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
