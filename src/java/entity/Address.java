
package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "address")
public class Address {
    
     @Id
    @Column(name = "id")
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
     @JoinColumn(name = "user_id")
    private User user_id;
    
    
    @Column(name = "line1",nullable = false)
    private String line1;
    
    @Column(name = "line2",nullable = false)
    private String line2;
    
     @Column(name = "postal_code",length = 10,nullable = false)
    private String postal_code;
    
      @Column(name = "mobile",length = 10,nullable = false)
    private String mobile;
    
    @ManyToOne
     @JoinColumn(name = "city_id")
    private City city_id;
    
    public Address(){
    
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public City getCity_id() {
        return city_id;
    }

    public void setCity_id(City city_id) {
        this.city_id = city_id;
    }

   
    
    
}
