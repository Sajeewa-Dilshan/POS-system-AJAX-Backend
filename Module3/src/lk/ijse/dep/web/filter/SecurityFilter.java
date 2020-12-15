package lk.ijse.dep.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "SecurityFilter",urlPatterns = "/*")
public class SecurityFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        ///System.out.println("incoming request 2");
        chain.doFilter(req, resp);
     //   System.out.println("going response 2");
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
