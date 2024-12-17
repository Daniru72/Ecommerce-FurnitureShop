
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import entity.Model;
import entity.Product;
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


@WebServlet(name = "LoadQuickMenue", urlPatterns = {"/LoadQuickMenue"})
public class LoadQuickMenue extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
              Gson gson = new Gson();
             JsonObject jsObject = new JsonObject();      
             Session session = HibernateUtil.getSessionFactory().openSession();
            
        Criteria criteria1 = session.createCriteria(Category.class);
        criteria1.addOrder(Order.asc("id"));
        List<Category> categoryList = criteria1.list();
        
        Criteria criteria2 = session.createCriteria(Model.class);
        criteria2.addOrder(Order.asc("name"));
        List<Model> modelList = criteria2.list();
        
//        living room products
        
                 Criteria criteria5 = session.createCriteria(Category.class);
                criteria5.add(Restrictions.eq("name", "Living Room"));
                Category category =(Category) criteria5.uniqueResult();
        
                Criteria critera3 = session.createCriteria(Model.class);
                critera3.add(Restrictions.eq("category", category));
                List<Model>pmodelList = critera3.list();
                
                 Criteria criteria4 = session.createCriteria(Product.class);
                criteria4.add(Restrictions.in("model", pmodelList));
                criteria4.setMaxResults(3);
        
                  List<Product>productList = criteria4.list();
               
                for(Product product1 : productList){
                    product1.getUser().setPassword(null);
                    product1.getUser().setVerification_code(null);
                    product1.getUser().setEmail(null);
                }
                
 //        living room products
 
 //        kitchen room products
        
                 Criteria criteria6 = session.createCriteria(Category.class);
                criteria6.add(Restrictions.eq("name", "Kitchen Room"));
                Category kcategory =(Category) criteria6.uniqueResult();
        
                Criteria critera7 = session.createCriteria(Model.class);
                critera7.add(Restrictions.eq("category", kcategory));
                List<Model>kmodelList = critera7.list();
                
                 Criteria criteria8 = session.createCriteria(Product.class);
                criteria8.add(Restrictions.in("model", kmodelList));
                criteria8.setMaxResults(3);
        
                  List<Product>kitchenproductList = criteria8.list();
               
                for(Product product2 : kitchenproductList){
                    product2.getUser().setPassword(null);
                    product2.getUser().setVerification_code(null);
                    product2.getUser().setEmail(null);
                }
                
 //        kitchen room products
        
        jsObject.add("categoryList", gson.toJsonTree(categoryList));
        jsObject.add("modelList", gson.toJsonTree(modelList));
        jsObject.add("productList", gson.toJsonTree(productList));
        jsObject.add("kitchenproductList", gson.toJsonTree(kitchenproductList));
        
         resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(jsObject));
        
    }

    
    
    
   
}
