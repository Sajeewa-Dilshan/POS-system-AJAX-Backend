package lk.ijse.dep.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CorsFilter",urlPatterns = "/*")
public class CorsFilter extends HttpFilter {


    //implements Filter

   /* public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletResponse resp1= (HttpServletResponse) resp;

        resp1.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        resp1.addHeader("Access-Control-Allow-Headers","Content-Type");

        System.out.println("incoming request");
        chain.doFilter(req, resp);
        System.out.println("going response");
    }

    public void init(FilterConfig config) throws ServletException {

    }*/

//
//    @Override
//    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        response.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
//        response.addHeader("Access-Control-Allow-Headers","Content-Type");
//
//        System.out.println("incoming request");
//        chain.doFilter(request, response);   }

}
