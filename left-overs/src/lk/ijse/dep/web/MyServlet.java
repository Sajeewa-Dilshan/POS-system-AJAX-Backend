package lk.ijse.dep.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MyServlet",urlPatterns = "/hello/*",loadOnStartup = 1)
public class MyServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("Started");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Context Path " +request.getContextPath());
        System.out.println("Servlet Path " +request.getServletPath());
        System.out.println("Path Info " +request.getPathInfo());
        System.out.println("Path Translated " +request.getPathTranslated());
        System.out.println("Query String " +request.getQueryString());
        System.out.println("URI " +request.getRequestURI());
        System.out.println("URL " +request.getRequestURL());
        System.out.println("Real Path " +request.getServletContext().getRealPath(""));
        response.getWriter().println("<h1>Hello</h1>");
    }
}
