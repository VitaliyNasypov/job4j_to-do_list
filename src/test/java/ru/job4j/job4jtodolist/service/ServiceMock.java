package ru.job4j.job4jtodolist.service;

import ru.job4j.job4jtodolist.persistence.Category;
import ru.job4j.job4jtodolist.persistence.Item;
import ru.job4j.job4jtodolist.persistence.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceMock implements Service {
    private static final ServiceMock INST = new ServiceMock();
    private static final AtomicInteger ITEM_ID = new AtomicInteger(1);
    private Map<Integer, Item> items = new ConcurrentHashMap<>();

    private ServiceMock() {
        Item item = new Item();
        item.setId(1);
        item.setDescription("Description");
        item.setCreated(new Date(System.currentTimeMillis()));
        item.setDone(false);
        items.put(1, item);
    }

    public static Service instOf() {
        return INST;
    }

    @Override
    public Item add(Item item, int[] idCategories) {
        int id = ITEM_ID.incrementAndGet();
        item.setId(id);
        items.put(id, item);
        return item;
    }

    @Override
    public boolean update(int id, boolean done) {
        Item result = items.computeIfPresent(id, (key, value) -> {
            value.setDone(done);
            return value;
        });
        return result != null;
    }

    @Override
    public boolean delete(int id) {
        return items.remove(id) != null;
    }

    @Override
    public List<Item> getAllTask() {
        return new ArrayList<>(items.values());
    }

    @Override
    public User add(User user) {
        return null;
    }

    @Override
    public boolean isUserCreated(User user) {
        return false;
    }

    @Override
    public User findUser(User user) {
        return null;
    }

    @Override
    public Set<Category> getAllCategories() {
        return null;
    }
}
