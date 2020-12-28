package lk.ijse.dep.web.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.dep.web.model.Item;
import lk.ijse.dep.web.model.OrderDetail;
import lk.ijse.dep.web.model.OrderedItem;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.basic.BasicArrowButton;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "PlaceOrderServlet", urlPatterns = "/placeorders")
public class PlaceOrderServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        resp.addHeader("Access-Control-Allow-Headers","Content-Type");
        resp.addHeader("Access-Control-Allow-Methods","POST,PUT,GET,DELETE");
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String code= request.getParameter("code");
        String id= request.getParameter("id");

        response.setContentType("application/json");
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        BasicDataSource cp= (BasicDataSource) getServletContext().getAttribute("cp");

        try(Connection connection=cp.getConnection();) {
            Jsonb jsonb = JsonbBuilder.create();
            OrderedItem orderedItem=jsonb.fromJson(request.getReader(),OrderedItem.class);


       /*     if(code ==null || id==null){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }*/

            System.out.println("dsbdsfgdf");

            System.out.println(orderedItem.getItems().size());
            orderedItem.getItems().forEach((k,v)->{
                System.out.println("key " +k +"value "+v);
            });




        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String code=request.getParameter("code");
        String qty=request.getParameter("qty");

        BasicDataSource cp= (BasicDataSource) getServletContext().getAttribute("cp");
        response.setContentType("application/json");


        System.out.println("code is "+code);
        System.out.println("qty is "+qty);
        try(Connection connection = cp.getConnection();
            PrintWriter out= response.getWriter()
        ){
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Item WHERE code=\""+code+"\"");
            Jsonb jsonb=JsonbBuilder.create();
            ResultSet rst= pstm.executeQuery();

            while(rst.next()){
                String description =rst.getString(2);
                Double unitPrice =rst.getDouble(3);
                Integer qtyOnHand =rst.getInt(4);
                if(Integer.parseInt(qty)>qtyOnHand){
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                out.println(jsonb.toJson(new Item(code,description,String.valueOf(unitPrice),String.valueOf(qtyOnHand))));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
