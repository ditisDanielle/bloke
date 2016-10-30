package nl.mad.bacchus.service.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import nl.mad.bacchus.model.Cheese;
import nl.mad.bacchus.model.meta.CheeseType;
import nl.mad.bacchus.model.meta.MilkType;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;

/**
 * Created by fd on 1-10-16.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CheeseDTO {

    protected Long id;
    @NotEmpty
    protected String name;
    protected BigDecimal cost;
    private CheeseType cheeseType;
    private MilkType milkType;

    private CheeseDTO() {
    }

    public static CheeseDTO toResultDTO(Cheese cheese) {
        CheeseDTO result = new CheeseDTO();
        result.id = cheese.getId();
        result.cost = cheese.getCost();
        result.name = cheese.getName();
        result.milkType = cheese.getMilkType();
        result.cheeseType = cheese.getCheeseType();
        return result;
    }

    public Cheese createCheese() {
        Cheese cheese = new Cheese();
        cheese.setCost(cost);
        cheese.setName(name);
        cheese.setMilkType(milkType);
        cheese.setCheeseType(cheeseType);
        return cheese;
    }

    public Cheese update(Cheese cheese) {
        cheese.setCost(cost);
        cheese.setName(name);
        cheese.setMilkType(milkType);
        cheese.setCheeseType(cheeseType);
        return cheese;
    }

}
