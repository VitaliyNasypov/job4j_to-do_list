package ru.job4j.job4jtodolist.controller;

import com.google.gson.Gson;
import org.json.JSONObject;
import ru.job4j.job4jtodolist.persistence.Item;
import ru.job4j.job4jtodolist.service.Service;
import ru.job4j.job4jtodolist.service.ServiceHibernate;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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
        switch (jsonObject.getString("action")) {
            case "ADD":
                Item item = service.add(new Item(jsonObject.getString("description"),
                        LocalDateTime.now(), jsonObject.getBoolean("done")));
                responseJson = new Gson().toJson(item);
                break;
            case "GET_ALL_TASKS":
                List<Item> itemList = service.getAllTask();
                responseJson = new Gson().toJson(itemList);
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
            default:
                responseJson = "{}";
        }
        resp.getWriter().print(responseJson);
        resp.getWriter().flush();
    }
}
