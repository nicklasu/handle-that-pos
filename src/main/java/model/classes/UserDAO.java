package model.classes;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDAO {
    private SessionFactory sessionFactory = null;
    public UserDAO() {

        try {

            sessionFactory = new Configuration().configure().buildSessionFactory();

        } catch (Exception e) {

            System.err.println("Istuntotehtaan luominen ei onnistunut.");

            e.printStackTrace();

            System.exit(-1);

        }

    }

    public User getUserById(int id) {
        Transaction transaction = null;
        User user = null;
        try (Session session = sessionFactory.getCurrentSession()) {

            transaction = session.beginTransaction();


            user = session.get(User.class, id);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {

                transaction.rollback();
            }

        }

        return user;
    }
    public User getUser(String username) {
        Transaction transaction = null;
        User user = null;
        try (Session session = sessionFactory.getCurrentSession()) {

            transaction = session.beginTransaction();
            Query query = session.createQuery("from User where username=:username");
            query.setParameter("username", username);
            List list = query.list();
            if(list.isEmpty()){
                System.out.println("Käyttäjää ei löytynyt tietokannasta.");
                return null;

            }

            user = (User) list.get(0);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {

                transaction.rollback();

            }

        }

        return user;
    }



    public List<User> getAllUsers() {
        Transaction transaction = null;
        List <User> users = null;
        try (Session session = sessionFactory.getCurrentSession()) {

            transaction = session.beginTransaction();


            users = session.createQuery("from User").list();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return users;
    }

    public void createUser(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            // save student object
            session.save(user);

            // commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

}
