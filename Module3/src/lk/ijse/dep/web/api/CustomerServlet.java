package lk.ijse.dep.web.api;

import jakarta.json.Json;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import lk.ijse.dep.web.model.Customer;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CustomerServlet", urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {



    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        resp.addHeader("Access-Control-Allow-Headers","Content-Type");
        resp.addHeader("Access-Control-Allow-Methods","POST,PUT,GET,DELETE");


    }



    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getParameter("id"));
       resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        String id=req.getParameter("id");
        if(id==null || id.matches("C\\d[3]")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println(1);
            return;
        }

        BasicDataSource cp= (BasicDataSource) getServletContext().getAttribute("cp");
        try( Connection connection=cp.getConnection();) {
            PreparedStatement pstm=connection.prepareStatement("SELECT  * from Customer where id=?");

            pstm.setObject(1,id);

            if(!pstm.executeQuery().next()){
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                System.out.println(2);

                return;
            }


            pstm=connection.prepareStatement("DELETE from Customer where id=?");

            pstm.setObject(1,id);

            boolean success=pstm.executeUpdate()>0;

            if(success){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                System.out.println(3);
            }else{
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                System.out.println(44);
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
      //  BufferedReader br = request.getReader();

      //  String line= null;
      //  String json="";

       /* while((line=br.readLine())!=null){
            json+=line;
        }
System.out.println(json);*/


        response.setContentType("application/json");

        response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");


        //Customer customer=jsonb.fromJson(json, Customer.class);


        BasicDataSource cp= (BasicDataSource) getServletContext().getAttribute("cp");

        try ( Connection connection=cp.getConnection();) {
            Jsonb jsonb = JsonbBuilder.create();
            Customer customer = jsonb.fromJson(request.getReader(), Customer.class);
            System.out.println(customer.getAddress());



            if (customer.getId() == null || customer.getName() == null || customer.getAddress() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                return;
            }

            if (!customer.getId().matches("C\\d{3}") || customer.getName().trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                return;
            }


            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Customer (id,name,address) VALUES (?,?,?)");
            pstm.setString(1, customer.getId());
            pstm.setString(2, customer.getName());
            pstm.setString(3, customer.getAddress());
            boolean success = pstm.executeUpdate() > 0;

            if (success) {
                response.getWriter().println(jsonb.toJson(true));
                response.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }catch (SQLIntegrityConstraintViolationException e){

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }catch(JsonbException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();


        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id= request.getParameter("id");
        String d= request.getParameter("d");
        System.out.println("id "+id+"d "+d);

        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        System.out.println(cp.getNumActive());
        System.out.println(cp.getNumIdle());
        response.setContentType("application/json");
       // response.setContentType("text/html");


        try (PrintWriter out = response.getWriter()) {
            //  out.println(getServletContext().getAttribute("abc"));
         /*   out.println("<div>");
            out.println("<h1>Sajeewa Module 3</h1>");*/

            try {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                //Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/SDK","root","1234");
                Connection connection = cp.getConnection();
                PreparedStatement stm = connection.prepareStatement("SELECT  *FROM Customer "+((id==null)?"":"WHERE id=?"));

                if(id!=null){
                    stm.setObject(1,id);
                }
                ResultSet rst = stm.executeQuery();



               /* out.println("<div>");
                out.println("<h1>Sajeewa Module 3</h1>")*/;
                //out.println("<table style='border-collapse: collapse; border:1px solid black;' >");

                //out.println("<customers >");

               // String json="[";

                List<Customer> customerList=new ArrayList<>();


             /*   out.println("<tr><th>Id</th>" +
                        "<th>Name</th></tr>");
                out.println("<tbody>");*/
                while (rst.next()) {
                    id = rst.getString(1);
                    String name = rst.getString(2);
                    String address = rst.getString(3);
                 /*   out.println("<tr>" + "<td>" + id + "</td>"
                            + "<td>" + name + "</td></tr>");*/

                    /*out.println("<customer>"+
                            "<id>"+id+"</id>"+
                            "<name>"+name+"</name>"+
                            "<address>"+address+"</address>"+
                            "</customer>");*/

                 /*   json+=("{"+
                            "\"id\":\""+id+"\","+
                            "\"name\":\""+name+"\","+
                            "\"address\":\""+address+"\""+"},"

                    );*/

                    customerList.add(new Customer(id,name,address));

                }

                //json=json.substring(0,json.length()-1);
//                out.println("</tbody></table>");
//                out.println("</div>");
               // out.println("</customers>");
                //json=json+"]";
               // out.println(json);

                if(id!=null && customerList.isEmpty()){
                    //response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }else{
                    Jsonb jsonb = JsonbBuilder.create();
                    out.println(jsonb.toJson(customerList));
                }


              connection.close();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       // resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        String id =req.getParameter("id");

        if(id==null || id.matches("C\\d[3]")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("1");
            return;
        }

          BasicDataSource cp= (BasicDataSource) getServletContext().getAttribute("cp");
        try(   Connection connection=cp.getConnection();) {
            Jsonb jsonb = JsonbBuilder.create();
            Customer customer=jsonb.fromJson(req.getReader(),Customer.class);

            if (customer.getId() != null || customer.getName() == null || customer.getAddress() == null) {
                System.out.println("2");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                return;
            }

            if (customer.getName().trim().isEmpty()) {

                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                System.out.println("3");
                return;
            }
            PreparedStatement pstm =connection.prepareStatement("SELECT * From Customer WHERE id=?");
            pstm.setObject(1,id);
            if(!pstm.executeQuery().next()){
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                System.out.println("4");
                return;
            }


           PreparedStatement pstm2 =connection.prepareStatement("UPDATE Customer SET name=?,address=? WHERE id=?");

        pstm2.setObject(1,customer.getName());
        pstm2.setObject(2,customer.getAddress());
        pstm2.setObject(3,id);
            pstm2.executeUpdate();


        } catch (SQLException throwables) {
            System.out.println("5");
            throwables.printStackTrace();
        }catch(JsonbException e){
            System.out.println("6");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }


    }
}
