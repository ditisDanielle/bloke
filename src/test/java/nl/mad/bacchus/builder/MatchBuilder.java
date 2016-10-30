package nl.mad.bacchus.builder;

import nl.mad.bacchus.model.Cheese;
import nl.mad.bacchus.model.Match;
import nl.mad.bacchus.model.Wine;
import org.springframework.stereotype.Component;

/**
 * Created by fd on 24-10-16.
 */
@Component
public class MatchBuilder extends AbstractBuilder{

    public MatchBuildCommand newMatch() {
        return new MatchBuildCommand();
    }

    public class MatchBuildCommand {

        private final Match match = new Match();



        public MatchBuildCommand withSpecs(Wine wine, Cheese cheese) {
            match.setWine(wine);
            match.setCheese(cheese);
            return this;
        }



        public Match build() {
            return match;
        }


        public Match save() {
            return saveWithTransaction(build());
        }

    }

}
