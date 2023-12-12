package database.dao;


import database.hibernate.HibernateSessionFactory;
import database.models.Connection;
import database.models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Data Access Object for {@link User} and {@link Connection}.
 */
public class UserDAO implements DAO<User, Integer>{
    private final HibernateSessionFactory sessionFactory;
    public UserDAO(HibernateSessionFactory hsf){
        this.sessionFactory = hsf;
    }
    @Override
    public void create(User user) {
        Session session = sessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.persist(user);
        tx1.commit();
        session.close();
    }
    @Override
    public User read(Integer id) {
        return sessionFactory.getSessionFactory().openSession().get(User.class, id);
    }
    @Override
    public void update(User user) {
        Session session = sessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.merge(user);
        tx1.commit();
        session.close();
    }
    @Override
    public void delete(User user) {
        Session session = sessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.remove(user);
        tx1.commit();
        session.close();
    }
}
