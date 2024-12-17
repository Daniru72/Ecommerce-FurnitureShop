package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.User_DTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.District;
import entity.OrderItem;
import entity.OrderStatus;
import entity.Product;
import entity.Province;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.PayHere;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "CheckOut", urlPatterns = {"/CheckOut"})
public class CheckOut extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();

        JsonObject requestJsonObject = gson.fromJson(req.getReader(), JsonObject.class);

        Session session = HibernateUtil.getSessionFactory().openSession();

        HttpSession httpSession = req.getSession();

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);

        String mobile = requestJsonObject.get("mobile").getAsString();
        String address1 = requestJsonObject.get("address1").getAsString();
        String address2 = requestJsonObject.get("address2").getAsString();
        String province = requestJsonObject.get("province").getAsString();
        String districte = requestJsonObject.get("districte").getAsString();
        String city = requestJsonObject.get("city").getAsString();
        String zipcode = requestJsonObject.get("zipcode").getAsString();

        if (httpSession.getAttribute("user") != null) {
            //user sign in

            User_DTO user_DTO = (User_DTO) httpSession.getAttribute("user");

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
            User user = (User) criteria1.uniqueResult();

            if (mobile.isEmpty()) {
                responseJsonObject.addProperty("message", "Invalide Mobile Number");
            } else if (address1.isEmpty()) {
                responseJsonObject.addProperty("message", "Invalide Address1");
            } else if (address2.isEmpty()) {
                responseJsonObject.addProperty("message", "Invalide Address2");
            } else if (!Validation.isInteger(province)) {
                responseJsonObject.addProperty("message", "Invalide Province");
            } else if (!Validation.isInteger(districte)) {
                responseJsonObject.addProperty("message", "Invalide Districte");
            } else if (!Validation.isInteger(city)) {
                responseJsonObject.addProperty("message", "Invalide City");
            } else if (zipcode.isEmpty()) {
                responseJsonObject.addProperty("message", "Invalide Zipcode");
            }else if (zipcode.length() != 5) {
                responseJsonObject.addProperty("message", "Invalide Zipcode");
            } else {

                Criteria criteria2 = session.createCriteria(Address.class);
                criteria2.add(Restrictions.eq("user_id", user));
                criteria2.addOrder(Order.desc("id"));
                criteria2.setMaxResults(1);

                if (criteria2.list().isEmpty()) {
                    //address not found 
                    responseJsonObject.addProperty("message", "Invalide Address Details. Pleace Check Your Account Details");
                } else {
                    //address found

                 Address addressDB = (Address)  criteria2.list().get(0);
                 
                    try {
                        
                         //create order in DB
                 entity.Orders order = new entity.Orders();
                 order.setAddress(addressDB);
                 order.setDate_time(new Date());
                 order.setUser(user);
                 
                int order_id =(int) session.save(order);
                 
                 //get cart items
                 Criteria criteria4 = session.createCriteria(Cart.class);
                 criteria4.add(Restrictions.eq("user", user));
                    List<Cart> cartList = criteria4.list();
                    
                   //get order status from DB (payment pending)
                  OrderStatus order_status =(OrderStatus)  session.get(OrderStatus.class, 5);
                   
                   double amount = 0;
                   String items = "";
                 //create order item in DB 
                 for(Cart cartItem : cartList){
                     
                     //calculate amount
                     amount+=cartItem.getQty()*cartItem.getProduct().getPrice();
                     if(addressDB.getCity_id().getId()==1){
                           amount+= 1000;
                     }else{
                           amount+= 2500;
                     }
                     //calculate amount
                     
                     //Get item details
                     items+=cartItem.getProduct().getTitle()+" x"+cartItem.getQty()+" ";
                     //Get item details
                     
                     Product product = cartItem.getProduct();
                     
                     OrderItem orderItem = new OrderItem();
                     orderItem.setOrder(order);
                     orderItem.setOrder_status(order_status);
                     orderItem.setProduct(product);
                     orderItem.setQty(cartItem.getQty());
                     session.save(orderItem);
                     
                     //update product qty in DB
                     product.setQty(product.getQty() - cartItem.getQty());
                     session.update(product);
                     
                     //Delete cart items from Db
                     session.delete(cartItem);
                     
                 }
                 
                 session.beginTransaction().commit();
                 
                 //Start: Set Payment Date
                 
                 String merchant_id ="1227325";
                 String formatedAmount = new DecimalFormat("0.00").format(amount);
                 String currency ="LKR";
                 String merchanSecret ="Mjg3MjQ1OTUzMDQwMzQ0Njc3NTU0NDMzNDE2MDkxNjczNzEyNTI=";
                 String merchanSecretMd5Hash = PayHere.generateMD5(merchanSecret);
                 
                   JsonObject payhere = new JsonObject();
                   payhere.addProperty("merchant_id", merchant_id);
                   
                   payhere.addProperty("return_url", "");
                   payhere.addProperty("cancel_url", "");
                   payhere.addProperty("notify_url", "");  //**
                   
                   payhere.addProperty("first_name", user.getFname());
                   payhere.addProperty("last_name", user.getLname());
                   payhere.addProperty("email", user.getEmail());
                   payhere.addProperty("phone", "");
                   payhere.addProperty("address", "");
                   payhere.addProperty("city", "");
                   payhere.addProperty("country", "");
                   payhere.addProperty("order_id", String.valueOf(order_id));
                   payhere.addProperty("items", "");
                   payhere.addProperty("currency", currency);
                   payhere.addProperty("amount", formatedAmount);
                   payhere.addProperty("sandbox", true);
                   
                   
                   //Generate MD5 Hash
                   String md5Hash = PayHere.generateMD5(merchant_id+order_id+formatedAmount+currency+merchanSecretMd5Hash);
                   payhere.addProperty("hash", md5Hash);
                   
                   
                 //End: Set Payment Date
                 
                 
                 responseJsonObject.addProperty("success", true);
                  responseJsonObject.addProperty("message", "Checkout Complete");
                  
                        Gson gson1 = new Gson();
                  responseJsonObject.add("payhereJson", gson1.toJsonTree(payhere));
                        
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                 
                 
                 
                    
                }

            }

        } else {
            //user not sign in
            responseJsonObject.addProperty("message", "USer not signed in");

        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJsonObject));

    }

}
