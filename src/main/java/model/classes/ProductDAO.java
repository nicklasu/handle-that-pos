package model.classes;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ProductDAO {
    private SessionFactory sessionFactory = null;
    public ProductDAO() {

        try {

            sessionFactory = new Configuration().configure().buildSessionFactory();

        } catch (Exception e) {

            System.err.println("Istuntotehtaan luominen ei onnistunut.");

            e.printStackTrace();

            System.exit(-1);

        }

    }

    public Product getProduct(int id) {
        Transaction transaction = null;
        Product product = null;
        try (Session session = sessionFactory.getCurrentSession()) {

            transaction = session.beginTransaction();

            product = session.get(Product.class, id);

            transaction.commit();
        } catch (Exception e) {

            if (transaction != null) {

                transaction.rollback();
            }

        }

        return product;
    }
    public void addProduct(Product product) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            session.save(product);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void updateProduct(Product product){
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()){

           transaction = session.beginTransaction();

            session.saveOrUpdate(product);


            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
    public void deleteProduct(int id){
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()){

            transaction = session.beginTransaction();
            Product product = session.get(Product.class, id);
            if(product != null) {
                session.delete(product);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }


}
