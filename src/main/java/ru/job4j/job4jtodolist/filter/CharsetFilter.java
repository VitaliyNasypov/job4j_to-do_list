package ru.job4j.job4jtodolist.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter({"/todolist", "/login", "/signup"})
public class CharsetFilter implements Filter {

    public void init(FilterConfig config) {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        next.doFilter(request, response);
    }

    public void destroy() {
    }
}
