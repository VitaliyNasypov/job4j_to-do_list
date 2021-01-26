package ru.job4j.job4jtodolist.service;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.job4jtodolist.persistence.Item;

import java.util.List;

public class ServiceHibernate implements Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHibernate.class.getName());
    private static SessionFactory sessionFactory;

    private ServiceHibernate() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private static final class Lazy {
        private static final Service INST = new ServiceHibernate();
    }

    public static Service instOf() {
        return Lazy.INST;
    }

    @Override
    public Item add(Item item) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
        }
        return item;
    }

    @Override
    public boolean update(int id, boolean done) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Item item = session.get(Item.class, id);
            item.setDone(done);
            session.update(item);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            LOGGER.warn(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Item item = new Item();
            item.setId(id);
            session.delete(item);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            LOGGER.warn(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<Item> getAllTask() {
        List<Item> result;
        try (Session session = sessionFactory.openSession()) {
            result = session.createQuery("from ru.job4j.job4jtodolist.persistence.Item").list();
        }
        return result;
    }
}
