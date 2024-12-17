
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements Serializable{
    
     @Id
    @Column(name = "id")
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
     
     @Column(name = "fname",length = 45,nullable = false)
    private String fname;
     
     @Column(name = "lname",length = 45,nullable = false)
    private String lname;
     
     @Column(name = "email",length = 50,nullable = false)
    private String email;
     
     @Column(name = "password",length = 20,nullable = false)
    private String password;
     
     @Column(name = "verification_code",length = 45,nullable = false)
    private String verification_code;
     
     public User(){
     
     }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }
    
}
