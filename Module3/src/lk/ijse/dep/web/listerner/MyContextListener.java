package lk.ijse.dep.web.listerner;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class MyContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        BasicDataSource bds= new BasicDataSource();

        bds.setUsername("root");
        bds.setPassword("1234");
        bds.setUrl("jdbc:mysql://localhost:3306/SDK");
        bds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        bds.setInitialSize(5);
        bds.setMaxTotal(5);
        System.out.println("Context is being begun");


        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("cp",bds);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Context is being destroyed");
        BasicDataSource cp= (BasicDataSource) sce.getServletContext().getAttribute("cp");
        try {
            cp.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
