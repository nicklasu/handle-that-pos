package model.DAOs;

import model.classes.HibernateUtil;
import model.classes.Profile;
import model.classes.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.sql.SQLException;

/**
 * Represents the hardware running the software
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class ProfileDAO {
    /**
     * This handles the connections to database
     */
    private SessionFactory sessionFactory = null;

    /**
     * Gets an instance of sessionfactory
     * @param name name of database to use, pos for main and postesti for the test database
     */
    public ProfileDAO(String name) {
        Configuration config = new Configuration();
        config.setProperty("hibernate.connection.url", "jdbc:mysql://10.114.32.12/" + name);
        try {
            sessionFactory = HibernateUtil.getSessionFactory();
        } catch (final Exception e) {
            System.out.println("Virhe istuntotehtaan luomisessa");
            e.printStackTrace();
        }

    }

    /**
     * Fetches user based on ID
     * 
     * @param user identifier for the user
     * @return user object
     */
    public Profile getAvatar(final User user) {
        Transaction transaction = null;
        Profile profile = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            final Query<Profile> query = session.createQuery("from Profile where id = :userId");
            query.setInteger("userId", user.getId());
            if (!query.list().isEmpty()) {
                profile = query.list().get(0);
            }
            transaction.commit();

        } catch (final Exception e) {
            if (transaction != null) {
                e.printStackTrace();
                final SQLException cause = (SQLException) e.getCause();
                System.out.println(cause.getMessage());
            }
        }
        return profile;
    }

    public void saveAvatar(final Profile profile) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(profile);
            transaction.commit();
        } catch (final Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
