
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import entity.Model;
import entity.ProductColor;
import entity.ProductCondition;
import entity.ProductMaterial;
import entity.ProductStatus;
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


@WebServlet(name = "LoadFeatures", urlPatterns = {"/LoadFeatures"})
public class LoadFeatures extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       
        
        Gson gson = new Gson();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Criteria criteria1 = session.createCriteria(Category.class);
        criteria1.addOrder(Order.asc("name"));
        List<Category> categoryList = criteria1.list();
        
        Criteria criteria2 = session.createCriteria(Model.class);
        criteria2.addOrder(Order.asc("name"));
        List<Model> modelList = criteria2.list();
        
        Criteria criteria3 = session.createCriteria(ProductMaterial.class);
        criteria3.addOrder(Order.asc("value"));
        List<ProductMaterial> materialList = criteria3.list();
        
        Criteria criteria4 = session.createCriteria(ProductCondition.class);
        criteria4.addOrder(Order.asc("name"));
        List<ProductCondition> conditionList = criteria4.list();
        
        Criteria criteria5 = session.createCriteria(ProductColor.class);
        criteria5.addOrder(Order.asc("name"));
        List<ProductColor> colorList = criteria5.list();
        
        JsonObject jsObject = new JsonObject();
        jsObject.add("categoryList", gson.toJsonTree(categoryList));
        jsObject.add("modelList", gson.toJsonTree(modelList));
        jsObject.add("materialList", gson.toJsonTree(materialList));
        jsObject.add("conditionList", gson.toJsonTree(conditionList));
        jsObject.add("colorList", gson.toJsonTree(colorList));
        
         resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(jsObject));
        
        session.close();
        
    }

    
    
}
