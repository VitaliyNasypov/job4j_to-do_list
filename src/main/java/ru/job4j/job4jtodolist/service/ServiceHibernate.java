package ru.job4j.job4jtodolist.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.job4jtodolist.persistence.Item;

import java.util.List;
import java.util.function.Function;

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

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sessionFactory.openSession();
        final Transaction ts = session.beginTransaction();
        try {
            T result = command.apply(session);
            ts.commit();
            return result;
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Item add(Item item) {
        return tx(
                session -> {
                    session.save(item);
                    return item;
                }
        );
    }

    @Override
    public boolean update(int id, boolean done) {
        try {
            return tx(
                    session -> {
                        Item item = session.get(Item.class, id);
                        item.setDone(done);
                        session.update(item);
                        return true;
                    }
            );
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            return tx(
                    session -> {
                        Item item = new Item();
                        item.setId(id);
                        session.delete(item);
                        return true;
                    }
            );
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<Item> getAllTask() {
        return tx(
                session -> session.createQuery("from Item")
                        .list()
        );
    }
}
