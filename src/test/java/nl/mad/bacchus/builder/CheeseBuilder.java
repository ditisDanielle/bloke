package nl.mad.bacchus.builder;

import nl.mad.bacchus.model.Cheese;
import nl.mad.bacchus.model.meta.CheeseType;
import nl.mad.bacchus.model.meta.MilkType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by fd on 1-10-16.
 */
@Component
public class CheeseBuilder extends AbstractBuilder {


    public CheeseBuildCommand newCheese() {
        return new CheeseBuildCommand();
    }


    public class CheeseBuildCommand {

        private final Cheese cheese = new Cheese();


        public CheeseBuildCommand withName(String name) {
            cheese.setName(name);
            return this;
        }


        public CheeseBuildCommand withCost(BigDecimal cost) {
            cheese.setCost(cost);
            return this;
        }



        public CheeseBuildCommand withSpecs(CheeseType cheeseType, MilkType milkType) {
            cheese.setCheeseType(cheeseType);
            cheese.setMilkType(milkType);
            return this;
        }



        public Cheese build() {
            return cheese;
        }


        public Cheese save() {
            return saveWithTransaction(build());
        }

    }

}
