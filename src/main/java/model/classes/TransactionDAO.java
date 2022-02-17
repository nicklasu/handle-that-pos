package model.classes;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class TransactionDAO {

    private SessionFactory sessionFactory = null;

    public TransactionDAO(){
        try {
            sessionFactory = HibernateUtil.getSessionFactory();
        }
        catch (Exception e){
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
            if (transaction != null) {
                t.rollback();
            }
        }
    }
}
