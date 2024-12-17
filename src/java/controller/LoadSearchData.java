
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


@WebServlet(name = "LoadSearchData", urlPatterns = {"/LoadSearchData"})
public class LoadSearchData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);
        
        Gson gson = new Gson();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        
        //get Category list
        Criteria criteria1 = session.createCriteria(Category.class);
        criteria1.addOrder(Order.asc("id"));
        List<Category> categoryList = criteria1.list();
        jsonObject.add("categoryList", gson.toJsonTree(categoryList));
        
        
        
         //get Condition list
        Criteria criteria2 = session.createCriteria(ProductCondition.class);
        criteria2.addOrder(Order.asc("id"));
        List<ProductCondition> conditionList = criteria2.list();
        jsonObject.add("conditionList", gson.toJsonTree(conditionList));
        
         //get Color list
        Criteria criteria3 = session.createCriteria(ProductColor.class);
        criteria3.addOrder(Order.asc("id"));
        List<ProductColor> colorList = criteria3.list();
        jsonObject.add("colorList", gson.toJsonTree(colorList));
        
           //get Material list
        Criteria criteria4 = session.createCriteria(ProductMaterial.class);
        criteria4.addOrder(Order.asc("id"));
        List<ProductMaterial> MaterialList = criteria4.list();
        jsonObject.add("MaterialList", gson.toJsonTree(MaterialList));
        
           //get Product list
        Criteria criteria5 = session.createCriteria(Product.class);
        criteria5.addOrder(Order.desc("id")); 
         jsonObject.addProperty("allProductCount", criteria5.list().size()); //all product count
         
        criteria5.setFirstResult(0); //first result to 6 result
        criteria5.setMaxResults(6);
        
        List<Product> ProductList = criteria5.list();
        //remove user from product
        for(Product product : ProductList){
            product.setUser(null);
        }
        jsonObject.add("ProductList", gson.toJsonTree(ProductList));
        
         //get Model list
        Criteria criteria6 = session.createCriteria(Model.class);
        criteria6.addOrder(Order.asc("name"));
        List<Model> modelList = criteria6.list();
        jsonObject.add("modelList", gson.toJsonTree(modelList));
        
        jsonObject.addProperty("success", true);
        
          resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(jsonObject));
        
        
    }

    
    
    
}
