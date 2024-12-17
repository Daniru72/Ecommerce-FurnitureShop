
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Model;
import entity.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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


@WebServlet(name = "LoadSingleProduct", urlPatterns = {"/LoadSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Response_DTO response_DTO = new Response_DTO();
        
        try {
            
           String productId = req.getParameter("id");
           
            if(Validation.isInteger(productId)){
               
              Product product =(Product)  session.get(Product.class, Integer.parseInt(productId));
              
              if(product != null){
              
                   product.getUser().setPassword(null);
              product.getUser().setVerification_code(null);
              product.getUser().setEmail(null);
           
              
                Criteria critera1 = session.createCriteria(Model.class);
                critera1.add(Restrictions.eq("category", product.getModel().getCategory()));
                List<Model>modelList = critera1.list();
                
                 Criteria critera2 = session.createCriteria(Product.class);
                critera2.add(Restrictions.in("model", modelList));
                critera2.add(Restrictions.ne("id", product.getId())); //ne -> not equal (remove this product from similer product)
                critera2.setMaxResults(3);
                
                List<Product>productList = critera2.list();
               
                for(Product product1 : productList){
                    product1.getUser().setPassword(null);
                    product1.getUser().setVerification_code(null);
                    product1.getUser().setEmail(null);
                }
                
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("product",gson.toJsonTree(product));
                jsonObject.add("productList", gson.toJsonTree(productList));
                
                 resp.setContentType("application/json");
               resp.getWriter().write(gson.toJson(jsonObject));
              
              }else{
                  
              response_DTO.setContent("Product not found");
               resp.setContentType("application/json");
               resp.getWriter().write(gson.toJson(response_DTO));
              
              }
             
              
               
            }else{
            
             response_DTO.setContent("Can not find Product");
              resp.setContentType("application/json");
               resp.getWriter().write(gson.toJson(response_DTO));
            
            }
            
            
            
        } catch (Exception e) {
              e.printStackTrace();
            response_DTO.setContent("An error occurred while retrieving product data");
             resp.setContentType("application/json");
               resp.getWriter().write(gson.toJson(response_DTO));
               
        }finally {
        session.close(); // Ensure session is closed
    }
        
        
    }

   
    
    
}
