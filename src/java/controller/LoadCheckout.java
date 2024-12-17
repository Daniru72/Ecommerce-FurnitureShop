
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.User_DTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.District;
import entity.Province;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "LoadCheckout", urlPatterns = {"/LoadCheckout"})
public class LoadCheckout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);
        
        HttpSession httpsession = req.getSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            
              if(httpsession.getAttribute("user") != null){
        // login
        
            User_DTO user_DTO = (User_DTO) httpsession.getAttribute("user");
            
            //get user from DB
            Criteria criteria1 =  session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
            User user =(User) criteria1.uniqueResult();
            
            //get user's last address from DB
            Criteria criteria2 = session.createCriteria(Address.class);
            criteria2.add(Restrictions.eq("user_id", user));
            criteria2.addOrder(Order.desc("id"));
            criteria2.setMaxResults(1); //get 1 result
            
            
             if(!criteria2.list().isEmpty()){
               //user has address
               
                Address address = (Address) criteria2.list().get(0);
               
                 //pack address in Json object
                  address.setUser_id(null);
                  jsonObject.add("address", gson.toJsonTree(address));
                  
                  
                    Criteria criteria3 = session.createCriteria(City.class);
                 criteria3.add(Restrictions.eq(String.valueOf("id"), user.getId()));
                 criteria3.setMaxResults(1);
                 City city =(City) criteria3.list().get(0);
                 
                 //pack city list
                 jsonObject.add("city", gson.toJsonTree(city));
                 
                  //get cart item from DB
                    Criteria criteria4 = session.createCriteria(Cart.class);
                    criteria4.add(Restrictions.eq("user", user));
                    List<Cart> cartList = criteria4.list();

                     //pack cart items in json object
                    for(Cart cart : cartList){
                        cart.setUser(null);
                        cart.getProduct().setUser(null);
                    }

                    jsonObject.add("cartList", gson.toJsonTree(cartList));

                   
                   jsonObject.addProperty("foundAddress", true);
                    
            
             }else{
             
                 jsonObject.addProperty("foundAddress", false);
                
             
             }
            
             jsonObject.addProperty("success", true);
        
        }else{
          //not login
          jsonObject.addProperty("message", "Not signed in");
        
        }
        
         resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(jsonObject));
              session.close();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }

   
    
    
}
