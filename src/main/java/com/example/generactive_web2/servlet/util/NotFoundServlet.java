package com.example.generactive_web2.servlet.util;




import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * All the requests not handled by other Servlets will be handled
 * by this Servlet. URL-pattern /* just covers all cases.
 */
@WebServlet(name = "NotFoundServlet", value = "/*")
public class NotFoundServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.getWriter().println("NOT_FOUND");
    }
}
