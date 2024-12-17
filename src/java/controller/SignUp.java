
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
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
         Response_DTO response_DTO = new Response_DTO();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        User_DTO user_dto = gson.fromJson(req.getReader(), User_DTO.class);

        if (user_dto.getFirst_name().isEmpty()) {
            response_DTO.setContent("Please Enter Your First Name");
        } else if (user_dto.getLast_name().isEmpty()) {
            response_DTO.setContent("Please Enter Your Last Name");
        } else if (user_dto.getEmail().isEmpty()) {
            response_DTO.setContent("Please Enter Valide Email");
        } else if (!Validation.isEmailValid(user_dto.getEmail())) {
            response_DTO.setContent("Please Enter Your Email");
        } else if (user_dto.getPassword().isEmpty()) {
            response_DTO.setContent("Please Enter Your Password");
        } else if (!Validation.isEmailValid(user_dto.getPassword())) {
            response_DTO.setContent("Password must include at least one uppercase letter,"
                    + " number, special character and be at least eight character long");
        } else {

            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_dto.getEmail()));

            if (!criteria1.list().isEmpty()) {

                response_DTO.setContent("User with this Email already exists");

            } else {

                //generate verification code
                int code = (int) (Math.random() * 100000);

                User user = new User();
                user.setEmail(user_dto.getEmail());
                user.setFname(user_dto.getFirst_name());
                user.setLname(user_dto.getLast_name());
                user.setPassword(user_dto.getPassword());
                user.setVerification_code(String.valueOf(code));

                //send verification code
                Thread sendMailThread = new Thread(){
                    @Override
                    public void run() {
                        Mail.sendMail(user.getEmail(),"Smart Trade Verification", "<h1>Your Verification Code: "+user.getVerification_code()+"</h1>");
                    }
                  
                    
                
                };
//               sendMailThread.start();
                

                
                session.save(user);
                session.beginTransaction().commit();
                
                 req.getSession().setAttribute("email", user_dto.getEmail());
                response_DTO.setSuccess(true);
                response_DTO.setContent("Registration Compleate");
                
            }

            session.close();

        }

        resp.setContentType("application/json");
         resp.getWriter().write(gson.toJson(response_DTO));
         System.out.println(gson.toJson(response_DTO));
        
        
        
    }

    
   
}
