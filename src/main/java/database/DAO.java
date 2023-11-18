package database;


import database.models.Connection;
import database.models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Data Access Object for {@link User} and {@link Connection}.
 */
public class DAO {
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
    public void updateUser(User user) {
        Session session = sessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.merge(user);
        tx1.commit();
        session.close();
    }
    public void deleteUser(User user) {
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
    /**
     * Get list of users, who already filled profiles.
     * @return list of users
     */
    public List<User> getProfileFilledUsers(){
        List<User> users = (List<User>)  sessionFactory.getSessionFactory().openSession().createQuery("From User WHERE profileFilled").list();
        return users;
    }
    public void createConnection(Connection connection) {
        Session session = sessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.persist(connection);
        tx1.commit();
        session.close();
    }
    public void deleteConnection(Connection connection) {
        Session session = sessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.remove(connection);
        tx1.commit();
        session.close();
    }
    /**
     * Ger list with connections of given user with other users
     * @return connections list
     */
    public List<Connection> getConnectionsWith(String id) {
        Session session = sessionFactory.getSessionFactory().openSession();
        Query<Connection> query = session.createQuery("From Connection where userID = :paramid");
        query.setParameter("paramid", id);
        return (List<Connection>) query.list();
    }
}
