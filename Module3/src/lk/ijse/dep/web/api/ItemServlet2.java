package lk.ijse.dep.web.api;

import jakarta.json.Json;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.dep.web.model.Item;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.Printable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "ItemServlet2",urlPatterns = "/items2")
public class ItemServlet2 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BasicDataSource cp= (BasicDataSource) request.getServletContext().getAttribute("cp");
       response.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        response.setContentType("application/json");

        try(PrintWriter out= response.getWriter();
            Connection connection=cp.getConnection();){

            PreparedStatement stm= connection.prepareStatement("SELECT * From Item");
            ResultSet rst =stm.executeQuery();

            ArrayList<Item> itemList=new ArrayList<>();

            while(rst.next()){
                String code=rst.getString(1);
                String description=rst.getString(2);
                Integer qtyOnHand=rst.getInt(3);
                Double unitPrice=rst.getDouble( 4);
                itemList.add(new Item(code,description,String.valueOf(unitPrice),String.valueOf(qtyOnHand)));
            }

            Jsonb jsonb = JsonbBuilder.create();
            out.println(jsonb.toJson(itemList));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
