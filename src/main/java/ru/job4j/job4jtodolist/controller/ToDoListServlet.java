package ru.job4j.job4jtodolist.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        JsonElement jsonParser = JsonParser.parseString(json);
        JsonObject jsonObject = jsonParser.getAsJsonObject();
        Service service = ServiceHibernate.instOf();
        String responseJson;
        switch (jsonObject.get("action").getAsString()) {
            case "ADD":
                Item item = service.add(new Item(jsonObject.get("description").getAsString(),
                        LocalDateTime.now(), jsonObject.get("done").getAsBoolean()));
                responseJson = new Gson().toJson(item);
                break;
            case "GET_ALL_TASKS":
                List<Item> itemList = service.getAllTask();
                responseJson = new Gson().toJson(itemList);
                break;
            case "DELETE":
                boolean resultDelete = service.delete(jsonObject.get("id").getAsInt());
                responseJson = new Gson().toJson(resultDelete);
                break;
            case "CHANGE_DONE":
                boolean resultUpdate = service.update(jsonObject.get("id").getAsInt(),
                        jsonObject.get("done").getAsBoolean());
                responseJson = new Gson().toJson(resultUpdate);
                break;
            default:
                responseJson = "{}";
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(responseJson);
        resp.getWriter().flush();
    }
}
