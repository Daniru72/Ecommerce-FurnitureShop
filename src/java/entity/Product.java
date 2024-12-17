
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product implements Serializable{
    
     @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
     
    @Column(name = "title",length = 45,nullable = false)
    private String title;
     
    @Column(name = "qty",nullable = false)
    private int qty;
      
    @Column(name = "price",nullable = false)
    private double price;
       
    @Column(name = "description",nullable = false)
    private String description;
        
    @Column(name = "date_time_added",nullable = false)
    private Date date_time;
   
    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "product_condition_id")
    private ProductCondition condition_id;
    
    @ManyToOne
    @JoinColumn(name = "status_id")
    private ProductStatus status;
    
    @ManyToOne
    @JoinColumn(name = "material_id")
    private ProductMaterial material;
    
    @ManyToOne
    @JoinColumn(name = "Product_color_id")
    private ProductColor color;
    
    
    public Product(){
    
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate_time() {
        return date_time;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }


    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProductCondition getCondition_id() {
        return condition_id;
    }

    public void setCondition_id(ProductCondition condition_id) {
        this.condition_id = condition_id;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public ProductMaterial getMaterial() {
        return material;
    }

    public void setMaterial(ProductMaterial material) {
        this.material = material;
    }

    public ProductColor getColor() {
        return color;
    }

    public void setColor(ProductColor color) {
        this.color = color;
    }
    
}
