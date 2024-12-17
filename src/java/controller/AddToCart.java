package controller;

import com.google.gson.Gson;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.Product;
import entity.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "AddToCart", urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        Response_DTO response_DTO = new Response_DTO();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {

            String id = req.getParameter("id");
            String qty = req.getParameter("qty");

            if (!Validation.isInteger(id)) {
                //Product not Found
                  response_DTO.setContent("Product not Found");
            } else if (!Validation.isInteger(qty)) {
                //Invalide Quantity
                response_DTO.setContent("Invalide Quantity");
            }else{

                int productId = Integer.parseInt(id);
                int productQty = Integer.parseInt(qty);

               if(productQty <= 0){
                   //Qty must be grater than 0
                   response_DTO.setContent("Quantity must be grater than 0");
               }else{
               
               
                    Product product = (Product) session.get(Product.class, productId);

                if (product != null) {

                    if (req.getSession().getAttribute("user") != null) {
                        //  user login
                        // DB Cart
                        User_DTO user_DTO = (User_DTO) req.getSession().getAttribute("user");
                        Criteria criteria1 = session.createCriteria(User.class);
                        criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
                       User user = (User) criteria1.uniqueResult();
                        
                        //check in DB cart
                        Criteria criteria2 = session.createCriteria(Cart.class);
                        criteria2.add(Restrictions.eq("user", user));
                        criteria2.add(Restrictions.eq("product", product));
                        
                        if(criteria2.list().isEmpty()){
                           //Item not found in cart
                            
                           if(productQty <= product.getQty()){
                             //add product into cart
                             
                             Cart cart = new Cart();
                             cart.setProduct(product);
                             cart.setQty(productQty);
                             cart.setUser(user);
                             session.save(cart);
                             transaction.commit();
                             
                             response_DTO.setSuccess(true);
                             response_DTO.setContent("Product Added to the Cart");
                             
                           }else{
                             //quantity not available
                            response_DTO.setContent("Quantity not Available");
                           }
                           
                           
                        }else{
                           //Item already found in cart
                              Cart cartItem =(Cart) criteria2.uniqueResult();
                       
                                if((cartItem.getQty() + productQty) <= product.getQty()){
                                
                                    cartItem.setQty(cartItem.getQty()+productQty);
                                    session.update(cartItem);
                                    transaction.commit();
                                    
                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("Cart Item Updated");
                                    
                                }else{
                                 //can't update your cart. Quantity not available
                                  response_DTO.setContent("Can't Update your Cart. Quantity not Available");
                                }
                        
                        
                        }
                        

                    } else {

                        // user not login
                        // Session Cart
                        
                        HttpSession httpSession = req.getSession();
                        
                        if(httpSession.getAttribute("sessionCart") != null){
                           //session cart found
                           
                           ArrayList<Cart_DTO> sessionCart =(ArrayList<Cart_DTO>) httpSession.getAttribute("sessionCart");
                           Cart_DTO foundCart_DTO = null;
                           
                           for(Cart_DTO cart_DTO : sessionCart){
                               
                               if(cart_DTO.getProduct().getId()==product.getId()){
                                   foundCart_DTO = cart_DTO;
                                   break;
                               }
                           }
                           
                           if(foundCart_DTO != null){
                             //product found
                               if((foundCart_DTO.getQty() + productQty) <= product.getQty()){
                                  //update Quantity
                                   foundCart_DTO.setQty(foundCart_DTO.getQty() + productQty);
                                   
                                   response_DTO.setSuccess(true);
                                    response_DTO.setContent("Cart Item Updated");
                                  
                               }else{
                                 //quantity not available
                                   response_DTO.setContent("Quantity not Available");
                               }
                             
                           }else{
                             //product not found
                               if(productQty<=product.getQty()){
                                   //add to session cart
                                   Cart_DTO cart_DTO = new Cart_DTO();
                                   cart_DTO.setProduct(product);
                                   cart_DTO.setQty(productQty);
                                   sessionCart.add(cart_DTO);
                                   
                                   response_DTO.setSuccess(true);
                               response_DTO.setContent("Product Added to the Cart");
                               
                               }else{
                                   //quantity not available
                                    response_DTO.setContent("Quantity not Available");
                               }
                           }
                        
                        }else{
                        //session cart not found
                        
                           if(productQty <= product.getQty()){
                            //create session cart and add to session cart
                            
                               ArrayList<Cart_DTO> sessionCart = new ArrayList<>();
                               
                               Cart_DTO cart_DTO = new Cart_DTO();
                               cart_DTO.setProduct(product);
                               cart_DTO.setQty(productQty);
                               sessionCart.add(cart_DTO);
                               
                               httpSession.setAttribute("sessionCart", sessionCart);
                               
                               response_DTO.setSuccess(true);
                               response_DTO.setContent("Product Added to the Cart");
                               
                           }else{
                           //quantity not available
                           response_DTO.setContent("Quantity not Available");
                           
                           }
                        
                        }
                        
                        
                    }

                } else {

                    //Product not Found
                    response_DTO.setContent("Product not Found");
                }
               
               
               
               }

            }

        } catch (Exception e) {
            e.printStackTrace();
            response_DTO.setContent("Unable to Process Your Request");
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(response_DTO));
        session.close();
    }

}
