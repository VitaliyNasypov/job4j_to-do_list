package ru.job4j.job4jtodolist.controller;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.job4j.job4jtodolist.persistence.Category;
import ru.job4j.job4jtodolist.persistence.Item;
import ru.job4j.job4jtodolist.persistence.User;
import ru.job4j.job4jtodolist.service.Service;
import ru.job4j.job4jtodolist.service.ServiceHibernate;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

@WebServlet("/todolist")
public class ToDoListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        BufferedReader br = req.getReader();
        String json = br.readLine();
        JSONObject jsonObject = new JSONObject(json);
        Service service = ServiceHibernate.instOf();
        String responseJson;
        HttpSession httpSession = req.getSession();
        User user = (User) httpSession.getAttribute("user");
        switch (jsonObject.getString("action")) {
            case "ADD":
                Item item = new Item();
                item.setDescription(jsonObject.getString("description"));
                item.setCreated(new Date(System.currentTimeMillis()));
                item.setDone(jsonObject.getBoolean("done"));
                item.setUser(user);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("checkbox"));
                int[] idCategories = new int[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    idCategories[i] = jsonArray.getInt(i);
                }
                item = service.add(item, idCategories);
                responseJson = new Gson().toJson(item);
                break;
            case "GET_ALL_TASKS":
                List<Item> itemList = service.getAllTask();
                itemList.forEach(i -> {
                    i.getUser().setPassword("");
                    i.getUser().setEmail("");
                });
                responseJson = new Gson().toJson(itemList);
                break;
            case "GET_ALL_CATEGORIES":
                Set<Category> categorySet = service.getAllCategories();
                responseJson = new Gson().toJson(categorySet);
                break;
            case "DELETE":
                boolean resultDelete = service.delete(jsonObject.getInt("id"));
                responseJson = new Gson().toJson(resultDelete);
                break;
            case "CHANGE_DONE":
                boolean resultUpdate = service.update(jsonObject.getInt("id"),
                        jsonObject.getBoolean("done"));
                responseJson = new Gson().toJson(resultUpdate);
                break;
            case "USER":
                responseJson = new Gson().toJson(user);
                break;
            default:
                responseJson = "{}";
        }
        resp.getWriter().print(responseJson);
        resp.getWriter().flush();
    }
}
