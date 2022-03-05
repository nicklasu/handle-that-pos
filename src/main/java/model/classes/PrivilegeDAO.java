package model.classes;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class PrivilegeDAO {
    private SessionFactory sessionFactory = null;

    public PrivilegeDAO() {
        try {
            sessionFactory = HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            System.out.println("Virhe istuntotehtaan luomisessa");
            e.printStackTrace();
        }
    }

    public List<Privilege> getPrivileges(User user) {
        Transaction transaction = null;
        List<Privilege> privileges = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            int userId = user.getId();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from Privilege where user = :userId");
            query.setInteger("userId", userId);
            privileges = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                System.out.println(e);
                transaction.rollback();
            }
        }
        return privileges;
    }

    public void addPrivilege(Privilege privilege) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            session.save(privilege);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
