
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "ResetPassword", urlPatterns = {"/ResetPassword"})
public class ResetPassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        
        Response_DTO response_DTO = new Response_DTO();
        
        Gson gson = new Gson();
       JsonObject dto =  gson.fromJson(req.getReader(), JsonObject.class);
       
        String newPassword = dto.get("newPassword").getAsString();
        String confirmPassword = dto.get("confirmPassword").getAsString();
        
        if(newPassword.isEmpty()){
          response_DTO.setContent("Please Enter New Password");
        }else if(confirmPassword.isEmpty()){
          response_DTO.setContent("Please Confirm Password");
        }else if(!newPassword.equals(confirmPassword)){
          response_DTO.setContent("Password Does not Match! Try Again");
        }else{
        
          if(req.getSession().getAttribute("fgemail")!= null){
          
             String email = req.getSession().getAttribute("fgemail").toString();
             
             Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", email));
            
            if(!criteria1.list().isEmpty()){
            
               User user = (User) criteria1.list().get(0);
               
               user.setPassword(newPassword);
               
               session.update(user);
               session.beginTransaction().commit();
               
               response_DTO.setSuccess(true);
                response_DTO.setContent("Your Password Updated");
                
            }else{
            
            response_DTO.setContent("No User Account Registered from your Email");
            
            }
            
          
          }else{
          
          response_DTO.setContent("No Email Address");
          
          }
        
        }
        
        resp.setContentType("application/json");
         resp.getWriter().write(gson.toJson(response_DTO));
         System.out.println(gson.toJson(response_DTO));
        
    }

    
    
    
}
