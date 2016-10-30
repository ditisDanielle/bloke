package nl.mad.bacchus.model;

import nl.mad.bacchus.model.BaseEntity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;


import nl.mad.bacchus.model.meta.*;

/**
 * Created by fr on 1-10-16.
 */
@Entity
@Table(name ="cheese")
//@DiscriminatorValue("cheese")
@PrimaryKeyJoinColumn(name = "product_id")
public class Cheese extends Product{

    @Enumerated(EnumType.STRING)
    private CheeseType cheeseType;
    @Enumerated(EnumType.STRING)
    private MilkType milkType;


    public CheeseType getCheeseType() {
        return cheeseType;
    }

    public void setCheeseType(CheeseType cheeseType) {
        this.cheeseType = cheeseType;
    }

    public MilkType getMilkType() {
        return milkType;
    }

    public void setMilkType(MilkType milkType) {
        this.milkType = milkType;
    }

    public static <T extends Enum & Labeled> Map<String, T[]> getEnumValues() {
        Map<String, T[]> result = new HashMap<>();
        result.put("milkType", (T[]) MilkType.values());
        result.put("cheeseType", (T[]) CheeseType.values());
        return result;
    }
}
