
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
@Table(name = "city")
public class City {
    
     @Id
    @Column(name = "id")
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
     @Column(name = "name",length = 45,nullable = false)
    private String name;
    
    @ManyToOne
     @JoinColumn(name = "district_district_id")
    private District district;
    
    public City(){
    
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

   
    
}
