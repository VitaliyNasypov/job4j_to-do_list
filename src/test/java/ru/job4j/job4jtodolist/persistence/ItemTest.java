package ru.job4j.job4jtodolist.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ItemTest {

    @Test
    public void shouldEqualsTwoItem() {
        Item first = new Item();
        first.setId(1);
        first.setDone(true);
        Item second = new Item();
        second.setId(1);
        second.setDone(true);
        Assertions.assertEquals(first, second);
    }
}
