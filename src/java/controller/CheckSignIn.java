
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Category;
import entity.Model;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;


@WebServlet(name = "CheckSignIn", urlPatterns = {"/CheckSignIn"})
public class CheckSignIn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        
         
         

        
        try {
            
            if(req.getSession().getAttribute("user") != null){
          //already signIn
        
            User_DTO user_DTO = (User_DTO) req.getSession().getAttribute("user");
            response_DTO.setSuccess(true);
            response_DTO.setContent(user_DTO);
                System.out.println("user success");
          
        }else{
            //not sign in
            response_DTO.setContent("Not Sign In");
            System.out.println("user not success");
            }
            

        
        
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(response_DTO));
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    
    
    
}
