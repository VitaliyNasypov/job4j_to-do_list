package ru.job4j.job4jtodolist.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.LoggerFactory;
import ru.job4j.job4jtodolist.persistence.User;
import ru.job4j.job4jtodolist.service.Service;
import ru.job4j.job4jtodolist.service.ServiceHibernate;
import ru.job4j.job4jtodolist.service.ServiceMock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceHibernate.class, LoggerFactory.class})
public class ToDoListServletTest {

    @Test
    public void shouldReturnCorrectJsonItems() throws IOException {
        Service service = ServiceMock.instOf();
        PowerMockito.mockStatic(LoggerFactory.class);
        PowerMockito.mockStatic(ServiceHibernate.class);
        Mockito.when(ServiceHibernate.instOf()).thenReturn(service);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        HttpSession httpSession = Mockito.mock(HttpSession.class);
        BufferedReader br = Mockito.mock(BufferedReader.class);
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("");
        PrintWriter pwMock = new PrintWriter(stringWriter);
        User user = new User();
        user.setId(1);
        user.setName("Test");
        httpSession.setAttribute("user", user);
        Mockito.when(req.getSession()).thenReturn(httpSession);
        Mockito.when(req.getReader()).thenReturn(br);
        Mockito.when(br.readLine())
                .thenReturn("{action: \"ADD\", id: \"\", description: \"Add Task\", "
                        + "done: \"false\"}");
        Mockito.when(resp.getWriter()).thenReturn(pwMock);
        new ToDoListServlet().doPost(req, resp);
        Assert.assertTrue(stringWriter.toString().contains("\"id\":2"));
        Mockito.when(br.readLine())
                .thenReturn("{action: \"GET_ALL_TASKS\", id: \"\", "
                        + "description: \"\", done: \"\"}");
        Mockito.when(resp.getWriter()).thenReturn(pwMock);
        new ToDoListServlet().doPost(req, resp);
        Assert.assertTrue(stringWriter.toString().contains("Description"));
        Mockito.when(br.readLine())
                .thenReturn("{action: \"CHANGE_DONE\", id: \"1\", description: \"\", "
                        + "done: \"true\"}");
        Mockito.when(resp.getWriter()).thenReturn(pwMock);
        new ToDoListServlet().doPost(req, resp);
        Assert.assertTrue(stringWriter.toString().contains("true"));
        Mockito.when(br.readLine())
                .thenReturn("{action: \"DELETE\", id: \"2\", description: \"\", done: \"\"}");
        Mockito.when(resp.getWriter()).thenReturn(pwMock);
        new ToDoListServlet().doPost(req, resp);
        Assert.assertTrue(stringWriter.toString().contains("true"));
    }
}
