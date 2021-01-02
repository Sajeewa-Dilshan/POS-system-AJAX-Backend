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
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PlaceOrderServlet", urlPatterns = "/placeorders")
public class PlaceOrderServlet extends HttpServlet {



    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        resp.addHeader("Access-Control-Allow-Headers","Content-Type");
        resp.addHeader("Access-Control-Allow-Methods","POST,PUT,GET,DELETE");
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id= request.getParameter("id");

        String total= request.getParameter("total");

        response.setContentType("application/json");
         response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        BasicDataSource cp= (BasicDataSource) getServletContext().getAttribute("cp");

        try(Connection connection=cp.getConnection();
            PrintWriter out= response.getWriter();
        ) {


            Jsonb jsonb = JsonbBuilder.create();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            OrderedItem orderedItem=jsonb.fromJson(request.getReader(),OrderedItem.class);

            System.out.println(orderedItem.getItem());
            System.out.println(orderedItem.getQty());
            System.out.println(orderedItem.getTotal());

            PreparedStatement pstm1 = connection.prepareStatement("SELECT  * from Customer WHERE id=\""+id+"\"");
            ResultSet rst1=pstm1.executeQuery();
            System.out.println(1);
            if(!rst1.next()) {
                out.println(jsonb.toJson("Enter valid customer id"));
                System.out.println(2);
                return;
            }

            PreparedStatement pstm2 = connection.prepareStatement("SELECT oid FROM OrderPOS ORDER BY oid DESC LIMIT 1;");
            ResultSet rst2=pstm2.executeQuery();

            List<Double> unitPrice = new ArrayList<>();
            for (int i=0;i<orderedItem.getItem().size();i++) {
                PreparedStatement pstm6 = connection.prepareStatement("SELECT unitPrice  FROM Item WHERE  code=?");
                pstm6.setObject(1,orderedItem.getItem().get(i));
                System.out.println("size "+orderedItem.getItem().size());
                ResultSet rst=pstm6.executeQuery();

                while (rst.next()){
                    unitPrice.add(rst.getDouble(1));

                    System.out.println(rst.getDouble(1));
                }
            }
            if(rst2.next()){

                PreparedStatement pstm3 = connection.prepareStatement("SELECT oid FROM OrderPOS ORDER BY oid DESC LIMIT 1;");
                ResultSet rst3=pstm3.executeQuery();
                int lastId=0;
                while(rst3.next()) {
                    lastId = Integer.parseInt(rst3.getString(1).substring(2));
                }
                PreparedStatement pstm4 = connection.prepareStatement("Insert INTO OrderPOS VALUES (?,?,?,?);");


                pstm4.setObject(1,"OD"+ (String.format("%03d", lastId+1)));

                pstm4.setObject(2,dtf.format(now) );

                pstm4.setObject(3,id);

                pstm4.setObject(4,Double.parseDouble(total));

                pstm4.executeUpdate();

                PreparedStatement pstm5 =connection.prepareStatement("INSERT INTO OrderDetailsPOS VALUES(?,?,?,?) ");
                for (int i=0;i<orderedItem.getItem().size();i++) {
                    pstm5.setObject(1, "OD" + (String.format("%03d", lastId + 1)));
                    pstm5.setObject(2, orderedItem.getItem().get(i));
                    pstm5.setObject(3,Integer.parseInt(orderedItem.getQty().get(i)));
                    pstm5.setObject(4,unitPrice.get(i));
                    pstm5.executeUpdate();
                }
                System.out.println(3);
            }else{

                PreparedStatement pstm4 = connection.prepareStatement("Insert INTO OrderPOS VALUES (?,?,?,?);");
                pstm4.setObject(1,"OD001");

                pstm4.setObject(2,dtf.format(now) );

                pstm4.setObject(3,id);

                pstm4.setObject(4,Double.parseDouble(total));

                pstm4.executeUpdate();

                PreparedStatement pstm5 =connection.prepareStatement("INSERT INTO OrderDetailsPOS VALUES(?,?,?,?) ");
                for (int i=0;i<orderedItem.getItem().size();i++) {
                    pstm5.setObject(1, "OD001");
                    pstm5.setObject(2, orderedItem.getItem().get(i));
                    pstm5.setObject(3,Integer.parseInt(orderedItem.getQty().get(i)));
                    pstm5.setObject(4,unitPrice.get(i));
                    pstm5.executeUpdate();
                }
                System.out.println(4);

            }

            for (int i=0; i<orderedItem.getItem().size();i++) {
                PreparedStatement pstm6 = connection.prepareStatement("SELECT qtyOnHand from Item where code=?");
                pstm6.setObject(1, orderedItem.getItem().get(i));
                ResultSet rst6=pstm6.executeQuery();
                
                PreparedStatement pstm7 = connection.prepareStatement("UPDATE Item SET qtyOnHand=? where code=?");


            }



            out.println(jsonb.toJson("Saved Order"));
            System.out.println(5);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String code=request.getParameter("code");
        String qty=request.getParameter("qty");

        BasicDataSource cp= (BasicDataSource) getServletContext().getAttribute("cp");
        response.setContentType("application/json");

        response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        System.out.println("code is "+code);
        System.out.println("qty is "+qty);
        try(Connection connection = cp.getConnection();
            PrintWriter out= response.getWriter()
        ){
           PreparedStatement pstm=connection.prepareStatement("SELECT * FROM Item where code=?");
           pstm.setObject(1,code);
           ResultSet rst= pstm.executeQuery();

           Jsonb jsonb=JsonbBuilder.create();

            String description="";
           Integer qtyOnHand=0;
            Double unitPrice=0.0;
            Boolean correctId=false;


           while (rst.next()){

                description=rst.getString(2);
                unitPrice=rst.getDouble(3);
                qtyOnHand =rst.getInt(4);
                correctId=true;

           }


           if(!correctId) {
                out.println(jsonb.toJson("Enter a Valid Item Code"));

                return;
            }

            if(Integer.parseInt(qty)>qtyOnHand){
                out.println(jsonb.toJson("Enter a Valid Item Qty"));

                return;
            }


            out.println(jsonb.toJson(new Item(code,description,String.valueOf(unitPrice),String.valueOf(qty))));








        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
