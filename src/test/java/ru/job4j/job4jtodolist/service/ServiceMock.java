package ru.job4j.job4jtodolist.service;

import ru.job4j.job4jtodolist.persistence.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceMock implements Service {
    private static final ServiceMock INST = new ServiceMock();
    private static final AtomicInteger ITEM_ID = new AtomicInteger(1);
    private Map<Integer, Item> items = new ConcurrentHashMap<>();

    private ServiceMock() {
        items.put(1, new Item(1, "Description",
                LocalDateTime.now(), false));
    }

    public static Service instOf() {
        return INST;
    }

    @Override
    public Item add(Item item) {
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
}
