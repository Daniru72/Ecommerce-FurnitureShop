
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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "VerifyVerification", urlPatterns = {"/VerifyVerification"})
public class VerifyVerification extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        
        Response_DTO response_DTO = new Response_DTO();
        
        Gson gson = new Gson();
       JsonObject dto =  gson.fromJson(req.getReader(), JsonObject.class);
        String verification = dto.get("verification").getAsString();
        
        if(verification.isEmpty()){
            response_DTO.setContent("Please Enter Your Verification Code");
        
        }else{
        
          if(req.getSession().getAttribute("fgemail")!= null){
               
             String email = req.getSession().getAttribute("fgemail").toString();
             
              Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", email));
            criteria1.add(Restrictions.eq("verification_code", verification));
            
            if(!criteria1.list().isEmpty()){
            
              
               
               response_DTO.setSuccess(true);
            
            }else{
            
            response_DTO.setContent("Invalide Verification Code");
            }
          
          }else{
          
          response_DTO.setContent("Please Enter Your Email");
          }
        
        
        }
        
         resp.setContentType("application/json");
       resp.getWriter().write(gson.toJson(response_DTO));
       System.out.println(gson.toJson(response_DTO));
        
    }

  
    
    
    
}
