package nl.mad.bacchus.service.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import nl.mad.bacchus.model.Cheese;
import nl.mad.bacchus.model.Match;
import nl.mad.bacchus.model.Wine;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by fd on 24-10-16.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MatchDTO {

    protected Long id;

    @NotEmpty
    private Cheese cheese;
    @NotEmpty
    private Wine wine;

    public MatchDTO(){}

    public static MatchDTO toResultDTO(Match match) {
        MatchDTO result = new MatchDTO();
        result.id = match.getId();
        result.cheese = match.getCheese();
        result.wine = match.getWine();
        return result;
    }

    public Match createMatch() {
        Match match = new Match(wine,cheese);
        return match;
    }

    public Match update(Match match) {
        match.setCheese(cheese);
        match.setWine(wine);
        return match;
    }

}
