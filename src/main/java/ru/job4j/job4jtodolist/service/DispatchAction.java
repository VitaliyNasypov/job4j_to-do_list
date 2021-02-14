package ru.job4j.job4jtodolist.service;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.job4j.job4jtodolist.persistence.Item;
import ru.job4j.job4jtodolist.persistence.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DispatchAction {
    private final Map<Function<JSONObject, Boolean>, Function<JSONObject, String>> dispatch =
            new HashMap<>();

    public DispatchAction init() {
        this.dispatch.put(
                jsonObject -> jsonObject.getString("action").equals("ADD"),
                jsonObject -> {
                    Item item = new Item();
                    item.setDescription(jsonObject.getString("description"));
                    item.setCreated(new Date(System.currentTimeMillis()));
                    item.setDone(jsonObject.getBoolean("done"));
                    item.setUser((User) jsonObject.get("user"));
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("checkbox"));
                    int[] idCategories = IntStream.range(0, jsonArray.length())
                            .map(jsonArray::getInt).toArray();
                    item = ServiceHibernate.instOf().add(item, idCategories);
                    return new Gson().toJson(item);
                }
        );
        this.dispatch.put(
                jsonObject -> jsonObject.getString("action").equals("GET_ALL_TASKS"),
                jsonObject ->
                        new Gson().toJson(ServiceHibernate.instOf().getAllTask().stream()
                                .parallel().peek(i -> {
                                    i.getUser().setPassword("");
                                    i.getUser().setEmail("");
                                }).collect(Collectors.toList())));
        this.dispatch.put(
                jsonObject -> jsonObject.getString("action")
                        .equals("GET_ALL_CATEGORIES"),
                jsonObject -> new Gson().toJson(ServiceHibernate.instOf()
                        .getAllCategories()));
        this.dispatch.put(
                jsonObject -> jsonObject.getString("action").equals("DELETE"),
                jsonObject -> new Gson().toJson(ServiceHibernate.instOf()
                        .delete(jsonObject.getInt("id"))));
        this.dispatch.put(
                jsonObject -> jsonObject.getString("action").equals("CHANGE_DONE"),
                jsonObject -> new Gson().toJson(ServiceHibernate.instOf()
                        .update(jsonObject.getInt("id"),
                                jsonObject.getBoolean("done"))));
        this.dispatch.put(
                jsonObject -> jsonObject.getString("action").equals("USER"),
                jsonObject -> new Gson().toJson(jsonObject.get("user")));
        return this;
    }

    public void load(Function<JSONObject, Boolean> predict, Function<JSONObject, String> handle) {
        this.dispatch.put(predict, handle);
    }

    public String action(JSONObject jsonObject) {
        for (Function<JSONObject, Boolean> predict : this.dispatch.keySet()) {
            if (predict.apply(jsonObject)) {
                return this.dispatch.get(predict).apply(jsonObject);
            }
        }
        throw new IllegalStateException("Could not found a handle for action");
    }
}
