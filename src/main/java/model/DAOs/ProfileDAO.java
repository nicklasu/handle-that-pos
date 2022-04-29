package model.DAOs;

import model.classes.HibernateUtil;
import model.classes.Profile;
import model.classes.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;

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
     * @param user identifier for the user
     * @return profile object
     */
    public Profile getAvatar(final User user) {
        Transaction transaction = null;
        Profile profile = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            final Query<Profile> query = session.createQuery("from Profile where id = :userId");
            query.setParameter("userId", user.getId(), IntegerType.INSTANCE);
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

    /**
     * Saves profile to database
     * @param profile Profile
     */
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
