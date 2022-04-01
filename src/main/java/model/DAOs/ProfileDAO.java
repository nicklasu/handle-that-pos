package model.DAOs;

import model.classes.HibernateUtil;
import model.classes.Profile;
import model.classes.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;

public class ProfileDAO {
    /**
     * This handles the connections to database
     */
    private SessionFactory sessionFactory = null;

    /**
     * Gets an instance of sessionfactory
     */
    public ProfileDAO() {
        try {
            sessionFactory = HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            System.out.println("Virhe istuntotehtaan luomisessa");
            e.printStackTrace();
        }

    }

    /**
     * Fetches user based on ID
     * @param user identifier for the user
     * @return user object
     */
    public Profile getAvatar(User user) {
        Transaction transaction = null;
        Profile profile = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from Profile where id = :userId");
            query.setInteger("userId", user.getId());
            if(query.list().size() > 0) {
                profile = (Profile) query.list().get(0);
            }
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                e.printStackTrace();
                SQLException cause = (SQLException) e.getCause();
                System.out.println(cause.getMessage());
            }
        }
        return profile;
    }

    public void saveAvatar(Profile profile){
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(profile);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
