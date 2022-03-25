package model.classes;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Helper class for accessing database with hibernate
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and Samu Luoma
 */
public class HibernateUtil {

    /**
     * Deals with creating the sessions to connect to database
     */
    public static SessionFactory factory;

    /**
     * Empty constructor required for hibernate
     */
    private HibernateUtil() {
    }

    /**
     * @return instance of SessionFactory
     */
    public static synchronized SessionFactory getSessionFactory() {

        if (factory == null) {
            factory = new Configuration().configure("hibernate.cfg.xml").
                    buildSessionFactory();
        }
        return factory;
    }
}
