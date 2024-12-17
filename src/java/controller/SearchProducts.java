
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import entity.Model;
import entity.Product;
import entity.ProductColor;
import entity.ProductCondition;
import entity.ProductMaterial;
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


@WebServlet(name = "SearchProducts", urlPatterns = {"/SearchProducts"})
public class SearchProducts extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       
        Gson gson = new Gson();
        
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);
        
        
        //get request data
        JsonObject requestJsonObject = gson.fromJson(req.getReader(), JsonObject.class);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        //Search all Products
        Criteria criteria1 = session.createCriteria(Product.class);
        
        
        
        //Add Category fillter
        if(requestJsonObject.has("category_name")){
              //Category selected
              String categery_name = requestJsonObject.get("category_name").getAsString();
              //get Category list
              Criteria criteria2 = session.createCriteria(Category.class);
              criteria2.add(Restrictions.eq("name", categery_name));
              Category category = (Category)criteria2.uniqueResult();
              //Get Model List
              Criteria criteria3 = session.createCriteria(Model.class);
              criteria3.add(Restrictions.eq("category", category));
              List<Model> modelList = criteria3.list();
              //Filter Prodcts By model
              criteria1.add(Restrictions.in("model", modelList));
        }
        
        if(requestJsonObject.has("condition_name")){
               //Condition Selected
                String condition_name = requestJsonObject.get("condition_name").getAsString();
                
                Criteria criteria4 = session.createCriteria(ProductCondition.class);
                criteria4.add(Restrictions.eq("name", condition_name));
                ProductCondition product_condition = (ProductCondition)criteria4.uniqueResult();
                
                //Filter Products by condition
                criteria1.add(Restrictions.eq("condition_id", product_condition));
        }
        
        if(requestJsonObject.has("color_name")){
                 //Color selected
                String color_name = requestJsonObject.get("color_name").getAsString();
                
                Criteria criteria5 = session.createCriteria(ProductColor.class);
                criteria5.add(Restrictions.eq("name", color_name));
                ProductColor color =(ProductColor) criteria5.uniqueResult();
                
                 //Filter Products by color
                 criteria1.add(Restrictions.eq("color", color));
        }
        
        if(requestJsonObject.has("material_name")){
                //material Selected
                String material_name = requestJsonObject.get("material_name").getAsString();
                
                Criteria criteria6 = session.createCriteria(ProductMaterial.class);
                criteria6.add(Restrictions.eq("value", material_name));
                ProductMaterial material = (ProductMaterial) criteria6.uniqueResult();
                
                 //Filter Products by material
                 criteria1.add(Restrictions.eq("material", material));
        }
        
        
       
         
         
        
         //Filter Products By Sorting Options
          String sort_text = requestJsonObject.get("sort_text").getAsString();
          
          if(sort_text.equals("Short by Latest")){
             criteria1.addOrder(Order.desc("id"));
             
          } else if(sort_text.equals("Short by Oldest")){
             criteria1.addOrder(Order.asc("id"));
             
          } else if(sort_text.equals("Short by Name")){
             criteria1.addOrder(Order.asc("title"));
             
          } else  if(sort_text.equals("Short by Price")){
             criteria1.addOrder(Order.asc("price"));
             
          }

          //get all product count
          responseJsonObject.addProperty("allProductCount", criteria1.list().size());
          
          //set product range
         int firstResult = requestJsonObject.get("firstResult").getAsInt();
          criteria1.setFirstResult(firstResult);
          criteria1.setMaxResults(6);
          
          //get product list
         List<Product>productList = criteria1.list();
         
         //remove users from Product
         for(Product product: productList){
           product.setUser(null);
         }
         
         
        responseJsonObject.addProperty("success", true);
        responseJsonObject.add("ProductList", gson.toJsonTree(productList));
        
        //send response
         resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJsonObject));
        
        
    }

    

}
