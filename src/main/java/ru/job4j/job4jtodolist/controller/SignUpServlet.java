package ru.job4j.job4jtodolist.controller;

import ru.job4j.job4jtodolist.persistence.User;
import ru.job4j.job4jtodolist.service.ServiceHibernate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = new User();
        user.setName(req.getParameter("name"));
        user.setEmail(req.getParameter("email"));
        user.setPassword(req.getParameter("password"));
        if (ServiceHibernate.instOf().isUserCreated(user)) {
            req.setAttribute("error", "User exists");
            req.getRequestDispatcher("signup.jsp").forward(req, resp);
        } else {
            ServiceHibernate.instOf().add(user);
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
