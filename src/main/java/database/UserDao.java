package database;


import mainBot.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;


public class UserDao {
    private final HibernateSessionFactory sessionFactory = new HibernateSessionFactory();
    public User getUser(String id) {
        return sessionFactory.getSessionFactory().openSession().get(User.class, id);
    }

    public void createUser(User user) {
        Session session = sessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.persist(user);
        tx1.commit();
        session.close();
    }

    public void update(User user) {
        Session session = sessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.merge(user);
        tx1.commit();
        session.close();
    }

    public void delete(User user) {
        Session session = sessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.remove(user);
        tx1.commit();
        session.close();
    }

    public List<User> getAllUsers() {
        List<User> users = (List<User>)  sessionFactory.getSessionFactory().openSession().createQuery("From User").list();
        return users;
    }
    public List<User> getProfileFilledUsers(){
        List<User> users = (List<User>)  sessionFactory.getSessionFactory().openSession().createQuery("From User WHERE profileFilled").list();
        return users;
    }
}
