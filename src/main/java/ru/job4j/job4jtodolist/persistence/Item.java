package ru.job4j.job4jtodolist.persistence;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ITEMS")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDateTime created;
    @Column(nullable = false)
    private boolean done;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Item() {
    }

    public Item(String description, LocalDateTime created, boolean done) {
        this.description = description;
        this.created = created;
        this.done = done;
    }

    public Item(int id, String description, LocalDateTime created, boolean done) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return id == item.id && done == item.done && Objects.equals(description, item.description)
                && Objects.equals(created, item.created) && Objects.equals(user, item.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, created, done, user);
    }
}
