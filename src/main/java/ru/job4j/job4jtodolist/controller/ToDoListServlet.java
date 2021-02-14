package ru.job4j.job4jtodolist.controller;

import org.json.JSONObject;
import ru.job4j.job4jtodolist.persistence.User;
import ru.job4j.job4jtodolist.service.DispatchAction;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/todolist")
public class ToDoListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        BufferedReader br = req.getReader();
        JSONObject jsonObject = new JSONObject(br.readLine());
        HttpSession httpSession = req.getSession();
        User user = (User) httpSession.getAttribute("user");
        jsonObject.put("user", user);
        String responseJson = new DispatchAction().init().action(jsonObject);
        resp.getWriter().print(responseJson);
        resp.getWriter().flush();
    }
}
