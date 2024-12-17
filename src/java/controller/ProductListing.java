
package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Category;
import entity.Model;
import entity.Product;
import entity.ProductColor;
import entity.ProductCondition;
import entity.ProductMaterial;
import entity.ProductStatus;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "ProductListing", urlPatterns = {"/ProductListing"})
public class ProductListing extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        
         Gson gson = new Gson();
        
        String categoryId = req.getParameter("categoryId");
        String modelId = req.getParameter("modelId");
        String titleId = req.getParameter("titleId");
        String descriptionId = req.getParameter("descriptionId");
        String materialId = req.getParameter("materialId");
        String colorId = req.getParameter("colorId");
        String conditionId = req.getParameter("conditionId");
        String priceId = req.getParameter("priceId");
        String qtyId = req.getParameter("qtyId");
        
        Part image1 = req.getPart("image1");
        Part image2 = req.getPart("image2");
        Part image3 = req.getPart("image3");
       
        Response_DTO response_DTO = new Response_DTO();
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        
         if(!Validation.isInteger(categoryId)){
            response_DTO.setContent("Invalide Category");
            
        }else  if(!Validation.isInteger(modelId)){
            response_DTO.setContent("Invalide Model");
            
        }else  if(!Validation.isInteger(materialId)){
            response_DTO.setContent("Invalide material");
            
        }else  if(!Validation.isInteger(colorId)){
            response_DTO.setContent("Invalide Color");
            
        }else  if(!Validation.isInteger(conditionId)){
            response_DTO.setContent("Invalide Condition");
            
        }else if(titleId.isEmpty()){
            response_DTO.setContent("Please Fill Text");
            
        }else if(descriptionId.isEmpty()){
            response_DTO.setContent("Please Fill description");
            
        }else if(priceId.isEmpty()){
            response_DTO.setContent("Please Fill Price");
            
        }else if(!Validation.isDouble(priceId)){
            response_DTO.setContent("Invalide Price");
            
        }else if(Double.parseDouble(priceId)<=0){
            response_DTO.setContent("Invalide Price");
            
        }else if(qtyId.isEmpty()){
            response_DTO.setContent("Please Fill Quantity");
            
        }else if(!Validation.isInteger(qtyId)){
            response_DTO.setContent("Invalide Quantity");
            
        }else if(Integer.parseInt(qtyId)<=0){
            response_DTO.setContent("Invalide Quantity");
            
        }else if (image1.getSize() == 0) {
                response_DTO.setContent("Please Upload Image 1");
                
        } else if (image2.getSize() == 0) {
                response_DTO.setContent("Please Upload Image 2");
                
        } else if (image3.getSize() == 0) {
                response_DTO.setContent("Please Upload Image 3");
                
        } else{
        
            Category category = (Category) session.get(Category.class, Integer.parseInt(categoryId));
          
            if(category == null){
            
                response_DTO.setContent("Please Select a Valide category");   
                
            }else{
              
                Model model = (Model) session.get(Model.class, Integer.parseInt(modelId));
                
                if(model == null){
                    
                     response_DTO.setContent("Please Select a Valide Model");
                     
                }else{
                
                    ProductMaterial material =(ProductMaterial) session.get(ProductMaterial.class, Integer.parseInt(materialId));
                    
                    
                    if(material == null){
                      
                        response_DTO.setContent("Please Select a Valide Material");
                        
                    }else{
                    
                        ProductColor color = (ProductColor) session.get(ProductColor.class, Integer.parseInt(colorId));
                        
                        if(color == null){
                        
                            response_DTO.setContent("Please Select a Valide Color");
                        
                        }else{
                        
                        
                            ProductCondition pcondition = (ProductCondition) session.get(ProductCondition.class, Integer.parseInt(conditionId));
                        
                            if(pcondition == null){
                            
                                response_DTO.setContent("Please Select a Valide Condition");
                            
                            }else{
                            
                             //Success
                            
                                Product product = new Product();
                                product.setColor(color);
                                product.setCondition_id(pcondition);
                                product.setDate_time(new Date());
                                product.setDescription(descriptionId);
                                product.setMaterial(material);
                                product.setModel(model);
                                product.setPrice(Double.parseDouble(priceId));
                                product.setQty(Integer.parseInt(qtyId));
                                
                                ProductStatus productStatus = (ProductStatus) session.get(ProductStatus.class, 1);
                              
                                product.setStatus(productStatus);
                                product.setTitle(titleId);
                                
                                User_DTO user_TDO =(User_DTO) req.getSession().getAttribute("user");
                               Criteria criteria1 =  session.createCriteria(User.class);
                               criteria1.add(Restrictions.eq("email", user_TDO.getEmail()));
                              User user = (User) criteria1.uniqueResult();
                              product.setUser(user);
                              
                              int pid = (int) session.save(product);
                              session.beginTransaction().commit();
                              
                              
                              
                                 
                               String applicationPath =  req.getServletContext().getRealPath("");
                            String  newApplicationPath = applicationPath.replace("build"+File.separator +"web", "web");
                            System.out.println("Image path: " + newApplicationPath);
                                File folder = new File(newApplicationPath+"//product-images//"+pid);
                              
                                    folder.mkdir();
                                
                                
                                try {
                                    InputStream inputStream1 = image1.getInputStream();
                                    File file1 = new File(folder, "image1.jpg");
                                    Files.copy(inputStream1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    System.out.println("Image1 saved successfully.");
                                    
                                } catch (IOException e) {
                                    System.err.println("Error saving image1: " + e.getMessage());
                                }
                                
                                 try {
                                    InputStream inputStream2 = image2.getInputStream();
                                    File file2 = new File(folder, "image2.jpg");
                                    Files.copy(inputStream2, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    System.out.println("Image1 saved successfully.");
                                    
                                } catch (IOException e) {
                                    System.err.println("Error saving image2: " + e.getMessage());
                                }
                                 
                                  try {
                                    InputStream inputStream3 = image3.getInputStream();
                                    File file3 = new File(folder, "image3.jpg");
                                    Files.copy(inputStream3, file3.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    System.out.println("Image1 saved successfully.");
                                    
                                } catch (IOException e) {
                                    System.err.println("Error saving image3: " + e.getMessage());
                                }
                                
                                
                                  response_DTO.setSuccess(true);
                                 response_DTO.setContent("New Product Added");
                            
                            }
                        
                        }
                    
                    
                    }
                
                
                }
            
            }
        
        }
        
        
         resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));
         
         
         
         
    }

   
    
    
}
