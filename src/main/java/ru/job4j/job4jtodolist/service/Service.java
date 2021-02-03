package ru.job4j.job4jtodolist.service;

import ru.job4j.job4jtodolist.persistence.Category;
import ru.job4j.job4jtodolist.persistence.Item;
import ru.job4j.job4jtodolist.persistence.User;

import java.util.List;
import java.util.Set;

public interface Service {
    Item add(Item item, int[] idCategories);

    User add(User user);

    boolean isUserCreated(User user);

    boolean update(int id, boolean done);

    boolean delete(int id);

    User findUser(User user);

    List<Item> getAllTask();

    Set<Category> getAllCategories();
}
