package lk.ijse.dep.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MyServlet2",urlPatterns = "/servlet2",loadOnStartup = 0)
public class MyServlet2 extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("Servlet 2");
        System.out.println("------------");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(request.getContentType());
    }
}
