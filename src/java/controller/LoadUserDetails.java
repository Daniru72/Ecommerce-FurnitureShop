
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Address;
import entity.City;
import entity.District;
import entity.Province;
import entity.User;
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
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "LoadUserDetails", urlPatterns = {"/LoadUserDetails"})
public class LoadUserDetails extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Gson gson = new Gson();
        
        JsonObject jsonObject =  new JsonObject();
        jsonObject.addProperty("success", false);
        
        try {
            
            if(req.getSession().getAttribute("user") != null){
        
                User_DTO user_DTO = (User_DTO) req.getSession().getAttribute("user");
                 
                Session session = HibernateUtil.getSessionFactory().openSession();
                
                 //get user from DB
            Criteria criteria1 =  session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
            User user =(User) criteria1.uniqueResult();
            
            Criteria criteria2 = session.createCriteria(Address.class);
            criteria2.add(Restrictions.eq("user_id", user));
            criteria2.addOrder(Order.desc("id"));
           criteria2.setMaxResults(1);
            
            if(!criteria2.list().isEmpty() ){
                //user has address
                
                //get User address
                Address address =(Address) criteria2.list().get(0);
                 
                  //pack address list
                  address.setUser_id(null);
                 jsonObject.add("address", gson.toJsonTree(address));
                 
                 Criteria criteria3 = session.createCriteria(City.class);
                 criteria3.add(Restrictions.eq(String.valueOf("id"), user.getId()));
                 criteria3.setMaxResults(1);
                 City city =(City) criteria3.list().get(0);
                 
                 //pack city list
                 jsonObject.add("city", gson.toJsonTree(city));
                 jsonObject.addProperty("AddressFound", true);
            
            }else{
              //user has not address
              
             //Get all cities
            Criteria criteria5 = session.createCriteria(City.class);
            criteria5.addOrder(Order.asc("name"));
            List<City> cityList = criteria5.list();
                
             //Get all Districts
              Criteria criteria6 = session.createCriteria(District.class);
             criteria6.addOrder(Order.asc("name"));
             List<District> districtsList = criteria6.list();   
             
               //Get all Province
             Criteria criteria7 = session.createCriteria(Province.class);
             criteria7.addOrder(Order.asc("name"));
             List<Province> provinceList = criteria7.list();
             
              //pack city list
            jsonObject.add("cityList", gson.toJsonTree(cityList));
            
             //pack District list
            jsonObject.add("districtsList", gson.toJsonTree(districtsList));
            
             //pack city list
            jsonObject.add("provinceList", gson.toJsonTree(provinceList));
                
            jsonObject.addProperty("AddressFound", false);
            
            }

                
           jsonObject.addProperty("success", true);
           jsonObject.add("content", gson.toJsonTree(user_DTO));
        
        }
             resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(jsonObject));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    
    

}
