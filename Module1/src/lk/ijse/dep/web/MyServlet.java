package lk.ijse.dep.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "MyServlet",urlPatterns = "/hello")
public class MyServlet extends HttpServlet {

    public MyServlet(){
        System.out.println("just a object");

    }

    @Override
    public void init() throws ServletException {
        System.out.println("Now servlet");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
response.setContentType("text/html");
        System.out.println("Now servlet is on run mode");
        try(PrintWriter out = response.getWriter()){
            out.println("<h1>Hello servlet</h1>");

        }
    }

    @Override
    public void destroy() {
        System.out.println("Destroy");
    }
}
