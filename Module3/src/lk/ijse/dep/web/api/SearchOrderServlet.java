package lk.ijse.dep.web.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.dep.web.model.OrderDetail;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchOrderServlet",urlPatterns = "/searchOrders")
public class SearchOrderServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        resp.addHeader("Access-Control-Allow-Headers","Content-Type");
        resp.addHeader("Access-Control-Allow-Methods","POST,PUT,GET,DELETE");
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BasicDataSource cp= (BasicDataSource) getServletContext().getAttribute("cp");

        String id= request.getParameter("id");

        response.setContentType("application/json");
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        try(Connection connection = cp.getConnection();
            PrintWriter out = response.getWriter();
        ){
            Boolean success=false;

            Jsonb jsonb = JsonbBuilder.create();
            System.out.println(id);
            PreparedStatement pstm1= connection.prepareStatement("SELECT  * FROM OrderDetailsPOS where OrderId=?");
            pstm1.setObject(1,id);
            ResultSet rst1=pstm1.executeQuery();

            ArrayList<String> itemCode= new ArrayList<>();
            ArrayList<String> des= new ArrayList<>();
            ArrayList<Integer> qty= new ArrayList<>();
            ArrayList<Double> unitPrice= new ArrayList<>();

            PreparedStatement pstm3=connection.prepareStatement("SELECT description FROM Item where code=?");

            while(rst1.next()){
                    itemCode.add(rst1.getString(2));
                    pstm3.setObject(1,rst1.getString(2));
                   ResultSet rst3= pstm3.executeQuery();
                   while (rst3.next()){
                       des.add(rst3.getString(1));

                   }
                    qty.add(rst1.getInt(3));

                    unitPrice.add(rst1.getDouble(4));
            }

            PreparedStatement pstm2= connection.prepareStatement("SELECT  * FROM OrderPOS where oid=?");
            pstm2.setObject(1,id);
            ResultSet rst2= pstm2.executeQuery();

            String date="";
            String cusId="";
            Double totalPrice=0.0;

            System.out.println(1);

            while(rst2.next()){
                System.out.println(2);
                date=rst2.getString(2);
                cusId=rst2.getString(3);
                totalPrice=rst2.getDouble(4);
                success=true;
            }

            if(!success){
                out.println(jsonb.toJson("Enter a valid order Id"));
                System.out.println("unsucccess");
                return;
            }


            out.println(jsonb.toJson(new OrderDetail(cusId,itemCode,des,qty,unitPrice,totalPrice,date)));


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BasicDataSource cp= (BasicDataSource) getServletContext().getAttribute("cp");

        String ordId= req.getParameter("ordId");

        System.out.println(ordId);
        resp.setContentType("application/json");

        resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        try(Connection connection = cp.getConnection();
            PrintWriter out = resp.getWriter();
        ){
            Jsonb jsonb = JsonbBuilder.create();

            PreparedStatement pstm1= connection.prepareStatement("DELETE from OrderDetailsPOS where OrderId=?");
            pstm1.setObject(1,ordId);

            Integer rst1= pstm1.executeUpdate();
            System.out.println("rst1 "+rst1);
            if(rst1==0){
                out.println(jsonb.toJson("unsuccess"));
                return;
            }

            PreparedStatement pstm2= connection.prepareStatement("DELETE from OrderPOS where oid=?");
            pstm2.setObject(1,ordId);
            Integer rst2=pstm2.executeUpdate();
            System.out.println("rst1 "+rst2);
            if(rst2==0){
                out.println(jsonb.toJson("unsuccess"));
                return;
            }


            out.println(jsonb.toJson("deleted"));


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
