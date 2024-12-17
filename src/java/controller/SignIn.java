
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        User_DTO user_dto = gson.fromJson(req.getReader(), User_DTO.class);
        
        
        if(user_dto.getEmail().isEmpty()){
            
            response_DTO.setContent("Please Enter your Valide Email");
        
        }else if(user_dto.getPassword().isEmpty()){
        
            response_DTO.setContent("Please Enter Your Password");
            
        }else{
        
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_dto.getEmail()));
            criteria1.add(Restrictions.eq("password", user_dto.getPassword()));
            
            if(!criteria1.list().isEmpty()){
            
              User user = (User) criteria1.list().get(0);
              
              if(!user.getVerification_code().equals("Verified")){
//              Unverified
                    req.getSession().setAttribute("email", user_dto.getEmail());
                    response_DTO.setContent("Unverified");
              
              }else{
//              Verified
                    user_dto.setFirst_name(user.getFname());
                    user_dto.setLast_name(user.getLname());
                    user_dto.setEmail(user.getEmail());
                    
                    user_dto.setPassword(null);
                    
                    req.getSession().setAttribute("user", user_dto);
                    
                    //Transfer Session Cart to DB Cart
                    if(req.getSession().getAttribute("sessionCart") != null){
                        //session cart found
                        ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) req.getSession().getAttribute("sessionCart"); //get user session cart
                        
                       Criteria criteria3 = session.createCriteria(Cart.class);
                       criteria3.add(Restrictions.eq("user", user));
                        List<Cart> dbCart = criteria3.list();  //get user db cart
                        
                        if(dbCart.isEmpty()){
                            //DB cart is Empty
                            //Add all session cart items into DB cart
                            
                            for(Cart_DTO cart_DTO : sessionCart){
                                Cart cart = new Cart();
                                cart.setProduct(cart_DTO.getProduct());
                                cart.setQty(cart_DTO.getQty());
                                cart.setUser(user);
                                session.save(cart);
                            }
                        }else{
                           //Found items in DB cart
                           
                           for(Cart_DTO cart_DTO :sessionCart){
                               
                               boolean isFoundDBCart = false;
                               
                               for(Cart cart : dbCart){
                                 if(cart_DTO.getProduct().getId() == cart.getProduct().getId()){
                                     //Same item found in Session cart and DB cart
                                       isFoundDBCart = true;
                                       
                                       if((cart_DTO.getQty()+cart.getQty()) <= cart.getProduct().getQty()){
                                           //qty available
                                           cart.setQty(cart_DTO.getQty()+cart.getQty());
                                           session.update(cart);
                                       }else{
                                           //qty not available
                                          //set max available Qty
                                          cart.setQty(cart.getProduct().getQty());
                                          session.update(cart);
                                       }
                                 }
                                 
                               }
                               
                               if(!isFoundDBCart){
                                 //not found in DB cart
                                 Cart cart = new Cart();
                                 cart.setProduct(cart_DTO.getProduct());
                                 cart.setQty(cart_DTO.getQty());
                                 cart.setUser(user);
                                 session.save(cart);
                               }
                           
                           }
                           
                           
                        }
                        
                        req.getSession().removeAttribute("sessionCart");
                        session.beginTransaction().commit();
                    
                    }
                    
                    response_DTO.setSuccess(true);
                    response_DTO.setContent("Sign In Success");
              
              }
            
            }else{
            
            response_DTO.setContent("Invalide Details! Please try again");
            
            }
        
        
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));
        
        
    }

    
  
}
