package model.classes;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class TransactionDAO {
    private SessionFactory sessionFactory = null;

    public TransactionDAO() {
        try {
            sessionFactory = HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            System.out.println("Virhe istuntotehtaan luomisessa");
            e.printStackTrace();
        }
    }

    public void addTransaction(model.classes.Transaction transaction) {
        Transaction t = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            t = session.beginTransaction();
            session.save(transaction);
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                t.rollback();
            }
        }
    }

    public void removeTransaction(model.classes.Transaction transaction) {
        Transaction t = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            t = session.beginTransaction();
            session.remove(transaction);
            t.commit();
        } catch (Exception e) {
            if (transaction != null) {
                t.rollback();
            }
        }
    }

    public model.classes.Transaction getTransaction(model.classes.Transaction transaction) {
        Transaction t = null;
        model.classes.Transaction tr = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            t = session.beginTransaction();
            tr = session.get(model.classes.Transaction.class, transaction.getID());
            t.commit();
        } catch (Exception e) {
            if (transaction != null) {
                t.rollback();
            }
        }
        return tr;
    }

    public List<model.classes.Transaction> getTransactions(User user) {
        Transaction transaction = null;
        List<model.classes.Transaction> tr = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            int userId = user.getId();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from Transaction where user = :userId");
            query.setInteger("userId", userId);
            tr = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                System.out.println(e);
                transaction.rollback();
            }
        }
        return tr;
    }
}
