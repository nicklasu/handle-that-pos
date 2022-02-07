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
            // start the transaction
            transaction = session.beginTransaction();

            // save student object
            session.save(product);

            // commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }


}
