package lk.ijse.dep.web.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import lk.ijse.dep.web.model.Customer;
import lk.ijse.dep.web.model.Item;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ItemsServlet",urlPatterns = "/items")
public class ItemsServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        resp.addHeader("Access-Control-Allow-Headers","Content-Type");
        resp.addHeader("Access-Control-Allow-Methods","POST,PUT,GET,DELETE");


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        // resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        String code =req.getParameter("code");

        if(code==null || code.matches("C\\d[3]")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("1");
            return;
        }

        BasicDataSource cp= (BasicDataSource) getServletContext().getAttribute("cp");
        try(   Connection connection=cp.getConnection();) {
            Jsonb jsonb = JsonbBuilder.create();
            Item item=jsonb.fromJson(req.getReader(),Item.class);

            if (item.getCode() != null || item.getDescription() == null || item.getUnitPrice() == null||item.getQtyOnHand()==null) {
                System.out.println("2");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                return;
            }

            if (item.getDescription().trim().isEmpty()) {

                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                System.out.println("3");
                return;
            }
            PreparedStatement pstm =connection.prepareStatement("SELECT * From Item WHERE code=?");
            pstm.setObject(1,code);
            if(!pstm.executeQuery().next()){
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                System.out.println("4");
                return;
            }


            PreparedStatement pstm2 =connection.prepareStatement("UPDATE Item SET description=?,unitPrice=?,qtyOnHand=? WHERE code=?");

            pstm2.setObject(1,item.getDescription());
            pstm2.setObject(2,Double.parseDouble(item.getUnitPrice()));
            pstm2.setObject(3,Double.parseDouble(item.getQtyOnHand()));
            pstm2.setObject(4,code);
            pstm2.executeUpdate();


        } catch (SQLException throwables) {
            System.out.println("5");
            throwables.printStackTrace();
        }catch(JsonbException e){
            System.out.println("6");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println(req.getParameter("code"));
        resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        String code=req.getParameter("code");
        if(code==null || code.matches("I\\d[3]")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println(1);
            return;
        }

        BasicDataSource cp= (BasicDataSource) getServletContext().getAttribute("cp");
        try( Connection connection=cp.getConnection();) {
            PreparedStatement pstm=connection.prepareStatement("SELECT  * from Item where code=?");

            pstm.setObject(1,code);

            if(!pstm.executeQuery().next()){
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                System.out.println(2);

                return;
            }

            pstm=connection.prepareStatement("DELETE from Item where code=?");

            pstm.setObject(1,code);

            boolean success=pstm.executeUpdate()>0;

            if(success){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                System.out.println(3);
            }else{
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                System.out.println(4);
            }


        } catch (SQLIntegrityConstraintViolationException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println(5);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(6);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ergargawgarg");
        response.setContentType("application/json");
        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");



        try(Connection connection=cp.getConnection();){
            Jsonb jsonb =JsonbBuilder.create();
            Item item =jsonb.fromJson(request.getReader(),Item.class);

            if (item.getCode() == null || item.getDescription() == null || item.getUnitPrice() == null|| item.getQtyOnHand()==null
            ) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                System.out.println(1);

                return;
            }

            if (!item.getCode().matches("I\\d{3}") || item.getDescription().trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                System.out.println(2);

                return;
            }


            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Item VALUES (?,?,?,?)");
            pstm.setString(1, item.getCode());
            pstm.setString(2, item.getDescription());
            pstm.setString(3, item.getUnitPrice());
            pstm.setString(4, item.getQtyOnHand());
            boolean success = pstm.executeUpdate() > 0;

            if (success) {
                response.getWriter().println(jsonb.toJson(true));
                response.setStatus(HttpServletResponse.SC_CREATED);
                System.out.println(3);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(4);
        }
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code= req.getParameter("id");
        System.out.println(req.getParameter("id"));

        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
        resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        System.out.println(cp.getNumActive());
        System.out.println(cp.getNumIdle());
        resp.setContentType("application/json");



        try (PrintWriter out = resp.getWriter()) {

            try {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Connection connection = cp.getConnection();
                PreparedStatement stm = connection.prepareStatement("SELECT  *FROM Item "+((code==null)?"":"WHERE id=?"));

                if(code!=null){
                    stm.setObject(1,code);
                }
                ResultSet rst = stm.executeQuery();




                List<Item> itemList=new ArrayList<>();


                 while (rst.next()) {
                    code = rst.getString(1);
                    String description = rst.getString(2);
                    String unitPrice = rst.getString(3);
                    String qtyOnHand = rst.getString(3);
                    itemList.add(new Item(code,description,unitPrice,qtyOnHand));
                 }

                 if(code!=null && itemList.isEmpty()){
                     resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }else{
                    Jsonb jsonb = JsonbBuilder.create();
                    out.println(jsonb.toJson(itemList));
                }


                connection.close();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


   /* protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
          response.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
          cp.getNumIdle();
          cp.getNumActive();
          response.setContentType("text/html");

          try(PrintWriter out =response.getWriter()){

              out.println("<div>");
              out.println("<h1>Sajeewa Dilshan Items</h1>");

              try {
                  Class.forName("com.mysql.cj.jdbc.Driver");
                  Connection connection=cp.getConnection();
                  Statement stm=connection.createStatement();
                  ResultSet rst = stm.executeQuery("SELECT * FROM Item");

                  out.println("<table style='border-collapse: collapse; border:1px solid black;' >");
                  out.println("<tr>" +
                          "<th>code</th>" +
                          "<th>Description</th>" +
                          "<th>Unit Price</th>" +
                          "<th>Qty On hand</th>" +
                          "</tr>");
                  out.println("<tbody>");


                  while(rst.next()){
                      String code=rst.getString(1);
                      String description=rst.getString(2);
                      Integer unitPrice=rst.getInt(3);
                      Integer qtyOnHand=rst.getInt(4);
                     out.println("<tr><td>"+code+"</td>"+
                              "<td>"+description+"</td>"+
                              "<td>"+unitPrice+"</td>"+
                              "<td>"+qtyOnHand+"</td></tr>");
                  }

                  connection.close();
                  out.println("</tbody></table>");
                  out.println("</div>");




              } catch (ClassNotFoundException | SQLException e) {
                  e.printStackTrace();
              }
          }
    }*/
}
