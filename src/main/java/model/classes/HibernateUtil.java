package model.classes;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Helper singleton class for accessing database with hibernate
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class HibernateUtil {

    /**
     * Deals with creating the sessions to connect to database
     */
    private static SessionFactory factory;

    /**
     * private constructor for singleton
     */
    private HibernateUtil() {
    }

    /**
     * @return instance of SessionFactory
     */
    public static synchronized SessionFactory getSessionFactory(String file) {

        if (factory == null) {
            factory = new Configuration().configure(file).buildSessionFactory();
        }
        return factory;
    }
}
