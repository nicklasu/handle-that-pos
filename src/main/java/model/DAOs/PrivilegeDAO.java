package model.DAOs;

import model.classes.HibernateUtil;
import model.classes.Privilege;
import model.classes.User;
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
                e.printStackTrace();
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

    public void addPrivileges(List<Privilege> privileges) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            for(Privilege p : privileges) {
                session.save(p);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void deletePrivilege(Privilege privilege) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.delete(privilege);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void deletePrivileges(List<Privilege> privileges) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            for(Privilege p : privileges){
                session.delete(p);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                e.printStackTrace();
                transaction.rollback();
            }
        }
    }


    public void updatePrivileges(List<Privilege> privileges){
            Transaction transaction = null;
            try (Session session = sessionFactory.getCurrentSession()) {
                transaction = session.beginTransaction();
                for(Privilege p : privileges) {
                    session.saveOrUpdate(p);
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    e.printStackTrace();
                    transaction.rollback();
                }
        }
    }
}
