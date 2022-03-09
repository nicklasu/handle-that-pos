package model.classes;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import javax.persistence.PersistenceException;
import java.util.List;

public class ProductDAO {
    private SessionFactory sessionFactory = null;

    public ProductDAO(){
        try {
            sessionFactory = HibernateUtil.getSessionFactory();
        }
        catch (Exception e){
            System.out.println("Virhe istuntotehtaan luomisessa");
            e.printStackTrace();
        }
    }

    public Product getProduct(String id) {
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

    public List<Product> getAllProducts() {
        Transaction transaction = null;
        List<Product> products = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            products = session.createQuery("from Product").list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return products;
    }

    public boolean addProduct(Product product) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.save(product);
            System.out.println(transaction);
            transaction.commit();
            return true;
        }
        catch (PersistenceException p){
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
         catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return false;
    }

    public boolean updateProduct(Product product){
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()){
           transaction = session.beginTransaction();
            session.saveOrUpdate(product);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }

    public boolean deleteProduct(String id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()){
            transaction = session.beginTransaction();
            Product product = session.get(Product.class, id);
            if(product != null) {
                session.delete(product);
                transaction.commit();
                System.out.println("Found product");
                return true;
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        System.out.println("product not found ");
        return false;
    }
}
