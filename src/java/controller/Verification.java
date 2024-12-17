
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
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


@WebServlet(name = "Verification", urlPatterns = {"/Verification"})
public class Verification extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        
        Response_DTO response_DTO = new Response_DTO();
        
        Gson gson = new Gson();
       JsonObject jsonObject = gson.fromJson(req.getReader(), JsonObject.class);
       String verification = jsonObject.get("verification").getAsString();
       
       if(req.getSession().getAttribute("email") != null){
       
          String email = req.getSession().getAttribute("email").toString();
          
           Session session =  HibernateUtil.getSessionFactory().openSession();
           Criteria criteria1 = session.createCriteria(User.class);
           criteria1.add(Restrictions.eq("email", email));
           criteria1.add(Restrictions.eq("verification_code", verification));
           
           if(!criteria1.list().isEmpty()){
           
              User user = (User)criteria1.list().get(0);
              user.setVerification_code("Verified");
              
              session.update(user);
              session.beginTransaction().commit();
              
               User_DTO user_TDO = new User_DTO();
               user_TDO.setFirst_name(user.getFname());
               user_TDO.setLast_name(user.getLname());
               user_TDO.setEmail(email);
               req.getSession().removeAttribute("email");
               req.getSession().setAttribute("user", user_TDO);
               
               response_DTO.setSuccess(true);
               response_DTO.setContent("Verification Success");
           
           }else{
           
               response_DTO.setContent("Invalide Verification Code");
           
           }
          
          
          
       
       }else{
       
           response_DTO.setContent("Verification Unavailable! Please Sign In.");
       
       }
       
       resp.setContentType("application/json");
       resp.getWriter().write(gson.toJson(response_DTO));
       System.out.println(gson.toJson(response_DTO));
       
        
    }

    
    
}
