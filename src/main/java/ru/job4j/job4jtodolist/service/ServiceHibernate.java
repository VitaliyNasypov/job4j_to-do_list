package ru.job4j.job4jtodolist.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.job4jtodolist.persistence.Category;
import ru.job4j.job4jtodolist.persistence.Item;
import ru.job4j.job4jtodolist.persistence.User;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;
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
    public Item add(Item item, int[] idCategories) {
        return tx(
                session -> {
                    for (int id : idCategories) {
                        Category category = session.find(Category.class, id);
                        item.getCategories().add(category);
                    }
                    session.save(item);
                    return item;
                }
        );
    }

    @Override
    public User add(User user) {
        user.setPassword(new PasswordHash()
                .generatePasswordHash(user.getPassword(), user.getEmail(),
                        10000, 512, "PBKDF2WithHmacSHA512"));
        User finalUser = user;
        user = tx(
                session -> {
                    session.save(finalUser);
                    return finalUser;
                }
        );
        user.setEmail("");
        user.setPassword("");
        return user;
    }

    @Override
    public boolean update(int id, boolean done) {
        try {
            return tx(
                    session -> {
                        Item item = session.find(Item.class, id);
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
                        Item item = session.find(Item.class, id);
                        item.setId(id);
                        session.remove(item);
                        return true;
                    }
            );
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean isUserCreated(User user) {
        try {
            tx(session -> {
                Query query = session.createQuery("from User where email = :email");
                query.setParameter("email", user.getEmail());
                return query.getSingleResult();
            });
            return true;
        } catch (NoResultException e) {
            LOGGER.info(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public User findUser(User user) {
        try {
            User userResult = (User) tx(
                    session -> {
                        Query query = session.createQuery("from User where email = :email");
                        query.setParameter("email", user.getEmail());
                        return query.getSingleResult();
                    });
            if (new PasswordHash().validatePassword(user.getPassword(),
                    userResult.getPassword())) {
                userResult.setPassword("");
                userResult.setEmail("");
                return userResult;
            }
        } catch (NoResultException e) {
            LOGGER.info(e.getMessage(), e);
        }
        return new User();
    }

    @Override
    public List<Item> getAllTask() {
        return tx(
                session -> session.createQuery("select distinct i from Item i "
                        + "join fetch i.categories").list());
    }

    @Override
    public Set<Category> getAllCategories() {
        return tx(
                session ->
                        Set.copyOf(session.createQuery("from Category").list()));
    }
}