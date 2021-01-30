package ru.job4j.job4jtodolist.service;

import ru.job4j.job4jtodolist.persistence.Item;
import ru.job4j.job4jtodolist.persistence.User;

import java.util.List;

public interface Service {
    Item add(Item item);

    User add(User user);

    boolean update(int id, boolean done);

    boolean delete(int id);

    boolean isUserCreated(User user);

    User findUser(User user);

    List<Item> getAllTask();
}
