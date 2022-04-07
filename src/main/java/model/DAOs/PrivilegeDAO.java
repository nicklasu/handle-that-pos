package model.DAOs;

import model.classes.HibernateUtil;
import model.classes.Privilege;
import model.classes.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Data access object for Privileges
 * 
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class PrivilegeDAO {
    /**
     * This handles the connections to database
     */
    private SessionFactory sessionFactory = null;

    /**
     * Gets an instance of sessionfactory
     */
    public PrivilegeDAO() {
        try {
            sessionFactory = HibernateUtil.getSessionFactory();
        } catch (final Exception e) {
            System.out.println("Virhe istuntotehtaan luomisessa");
            e.printStackTrace();
        }
    }

    /**
     * Fetches all privileges associated with a certain user from the database
     * 
     * @param user user whose privileges are to be fetched
     * @return list of privilege objects
     */
    public List<Privilege> getPrivileges(final User user) {
        Transaction transaction = null;
        List<Privilege> privileges = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            final int userId = user.getId();
            transaction = session.beginTransaction();
            final Query<Privilege> query = session.createQuery("from Privilege where user = :userId");
            query.setInteger("userId", userId);
            privileges = query.list();
            transaction.commit();
        } catch (final Exception e) {
            if (transaction != null) {
                e.printStackTrace();
                transaction.rollback();
            }
        }
        return privileges;
    }

    /**
     * Adds a privilege to the database
     * 
     * @param privilege privilege object
     */
    public void addPrivilege(final Privilege privilege) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.save(privilege);
            transaction.commit();
        } catch (final Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    /**
     * Adds multiple privileges to the database
     * 
     * @param privileges list of privileges
     */
    public void addPrivileges(final List<Privilege> privileges) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            for (final Privilege p : privileges) {
                session.save(p);
            }
            transaction.commit();
        } catch (final Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    /**
     * Removes a privilege entry from the database
     * 
     * @param privilege the privilege to be deleted
     */
    public void deletePrivilege(final Privilege privilege) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.delete(privilege);
            transaction.commit();
        } catch (final Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    /**
     * Deletes a set of privileges from the database
     * 
     * @param privileges the set of privileges to be deleted
     */
    public void deletePrivileges(final List<Privilege> privileges) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            for (final Privilege p : privileges) {
                session.delete(p);
            }
            transaction.commit();
        } catch (final Exception e) {
            if (transaction != null) {
                e.printStackTrace();
                transaction.rollback();
            }
        }
    }

    /**
     * Updates a set of privileges in the database
     * 
     * @param privileges a list of privileges to be modified
     */
    public void updatePrivileges(final List<Privilege> privileges) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            for (final Privilege p : privileges) {
                session.saveOrUpdate(p);
            }
            transaction.commit();
        } catch (final Exception e) {
            if (transaction != null) {
                e.printStackTrace();
                transaction.rollback();
            }
        }
    }
}
