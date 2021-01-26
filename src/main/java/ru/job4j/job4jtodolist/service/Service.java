package ru.job4j.job4jtodolist.service;

import ru.job4j.job4jtodolist.persistence.Item;

import java.util.List;

public interface Service {
    Item add(Item item);

    boolean update(int id, boolean done);

    boolean delete(int id);

    List<Item> getAllTask();
}
