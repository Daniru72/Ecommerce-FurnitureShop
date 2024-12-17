
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Address;
import entity.City;
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


@WebServlet(name = "InsertAddress", urlPatterns = {"/InsertAddress"})
public class InsertAddress extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Gson gson = new Gson();
        
        JsonObject requestJsonObject = gson.fromJson(req.getReader(), JsonObject.class);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);
        
        String email = requestJsonObject.get("email").getAsString();
        String mobile = requestJsonObject.get("mobile").getAsString();
        String address1 = requestJsonObject.get("address1").getAsString();
        String address2 = requestJsonObject.get("address2").getAsString();
       
        Integer province = requestJsonObject.get("provinceValue").getAsInt();
        Integer districte = requestJsonObject.get("districtValue").getAsInt();
        Integer city = requestJsonObject.get("cityValue").getAsInt();
        
         String postalCode = requestJsonObject.get("postal").getAsString();
         
         
         
          if (mobile.isEmpty()) {
                responseJsonObject.addProperty("message", "Invalide Mobile Number");
            } else if (address1.isEmpty()) {
                responseJsonObject.addProperty("message", "Invalide Address1");
            } else if (address2.isEmpty()) {
                responseJsonObject.addProperty("message", "Invalide Address2");
//            } else if (!Validation.isInteger(province)) {
//                responseJsonObject.addProperty("message", "Invalide Province");
//            } else if (!Validation.isInteger(districte)) {
//                responseJsonObject.addProperty("message", "Invalide Districte");
//            } else if (!Validation.isInteger(city)) {
//                responseJsonObject.addProperty("message", "Invalide City");
//            } else if (postalCode.isEmpty()) {
                responseJsonObject.addProperty("message", "Invalide Zipcode");
            }else if (postalCode.length() != 5) {
                responseJsonObject.addProperty("message", "Invalide Zipcode");
            } else {
            
            
              Criteria criteria1 = session.createCriteria(User.class);
              criteria1.add(Restrictions.eq("email", email));
              
               if(!criteria1.list().isEmpty()){
               
                 User user =(User) criteria1.list().get(0);
                 
                  Criteria criteria2 = session.createCriteria(City.class);
              criteria2.add(Restrictions.eq("id", city));
               criteria2.setMaxResults(1);
              City cityname = (City) criteria2.list().get(0);
                 
               
                  Address address = new Address();
                  address.setUser_id(user);
                  address.setLine1(address1);
                  address.setLine2(address2);
                  address.setPostal_code(postalCode);
                  address.setMobile(mobile);
                  address.setCity_id(cityname);
                  
                   session.save(address);
                session.beginTransaction().commit();
                
                 responseJsonObject.addProperty("success", true);
                 responseJsonObject.addProperty("message", "Update Success");
               }
            
                
            
            }
          
          session.close();
          resp.setContentType("application/json");
          resp.getWriter().write(gson.toJson(responseJsonObject));
        
    }

   
    
}
