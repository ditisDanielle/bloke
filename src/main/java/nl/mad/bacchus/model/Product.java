package nl.mad.bacchus.model;

import nl.mad.bacchus.model.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;


/**
 * Created by fd on 1-10-16.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name ="TYPE" ,discriminatorType=DiscriminatorType.STRING)
public class Product extends BaseEntity{


    private String name;
    private BigDecimal cost;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
