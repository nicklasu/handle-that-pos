package model.DAOs;

import model.classes.HibernateUtil;
import model.classes.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Data access object for products
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and Samu Luoma
 */
public class ProductDAO {
    /**
     * This handles the connections to database
     */
    private SessionFactory sessionFactory = null;

    /**
     * Gets an instance of sessionfactory
     */
    public ProductDAO() {
        try {
            sessionFactory = HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            System.out.println("Virhe istuntotehtaan luomisessa");
            e.printStackTrace();
        }
    }

    /**
     * Gets a product from the database based on an id
     * @param id identifier in string format
     * @return a product if one is found
     */
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

    /**
     * Fetches all the products in the database
     * @return list of all the products
     */
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

    /**
     * Adds a product to the database
     * @param product Product to be added
     * @return true if succesfull
     */
    public boolean addProduct(Product product) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.save(product);
            System.out.println(transaction);
            transaction.commit();
            return true;
        } catch (PersistenceException p) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return false;
    }

    /**
     * Updates a product in the database
     * @param product Product to be updated
     * @return true if succesfull
     */
    public boolean updateProduct(Product product) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
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

    /**
     * Deletes a product from the database
     * @param id identifier of the product
     * @return true if succesfull
     */
    public boolean deleteProduct(String id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            Product product = session.get(Product.class, id);
            if (product != null) {
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