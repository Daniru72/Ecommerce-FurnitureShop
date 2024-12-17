
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import model.Mail;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "ForgotPassword", urlPatterns = {"/ForgotPassword"})
public class ForgotPassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
         Response_DTO response_DTO = new Response_DTO();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        User_DTO user_dto = gson.fromJson(req.getReader(), User_DTO.class);
        
        
        if(user_dto.getEmail().isEmpty()){
            response_DTO.setContent("EnterEmail");
        }else{
        
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_dto.getEmail()));
            
            if(!criteria1.list().isEmpty()){
            
               User user =(User) criteria1.list().get(0);
               
                //generate verification code
                int code = (int) (Math.random() * 100000);
                
                user.setVerification_code(String.valueOf(code));
                
                  //send verification code
                Thread sendMailThread = new Thread(){
                    @Override
                    public void run() {
                        Mail.sendMail(user.getEmail(),"Smart Trade Verification Code", "<h1>Your Verification Code: "+user.getVerification_code()+"</h1>");
                    }
                  
                    
                
                };
               sendMailThread.start();
                
                 session.update(user);
                  session.beginTransaction().commit();
                  
                   req.getSession().setAttribute("fgemail", user.getEmail());
                response_DTO.setSuccess(true);
                response_DTO.setContent("Verification send Compleate");
            
            }else{
            
              response_DTO.setContent("Invalide Email! Please try again");
            
            
            }
            
        
        }
        
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));
        
    }

   

    
   
}
