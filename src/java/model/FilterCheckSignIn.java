
package model;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebFilter(urlPatterns = {"/user-acount.html"})
public class FilterCheckSignIn implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
        
    }

    
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        if(req.getSession().getAttribute("user") != null){
        
            chain.doFilter(request, response);
        
        }else{
        
            resp.sendRedirect("user-login.html");
        
        }
        
    }

    @Override
    public void destroy() {
        
        
    }
    
    
    
    
}
