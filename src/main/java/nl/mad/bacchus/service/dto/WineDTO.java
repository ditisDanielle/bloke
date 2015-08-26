package nl.mad.bacchus.service.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import nl.mad.bacchus.model.Wine;
import nl.mad.bacchus.model.meta.Country;
import nl.mad.bacchus.model.meta.WineRegion;
import nl.mad.bacchus.model.meta.WineType;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Data transfer object for Wine data.
 * 
 * @author bas
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class WineDTO {

    protected Long id;
    @NotEmpty
    protected String name;
    protected BigDecimal cost;
    private Country country = Country.UNKNOWN;
    private WineRegion region = WineRegion.UNKNOWN;
    private WineType wineType;
    private Integer year;

    private WineDTO() {
    }

    public static WineDTO toResultDTO(Wine wine) {
        WineDTO result = new WineDTO();
        result.id = wine.getId();
        result.cost = wine.getCost();
        result.name = wine.getName();
        result.region = wine.getRegion();
        result.wineType = wine.getWineType();
        result.year = wine.getYear();
        result.country = wine.getCountry();
        return result;
    }

    public Wine createWine() {
        Wine wine = new Wine();
        wine.setCost(cost);
        wine.setName(name);
        wine.setRegion(region);
        wine.setWineType(wineType);
        wine.setYear(year);
        wine.setCountry(country);
        return wine;
    }

    public Wine update(Wine wine) {
        wine.setCost(cost);
        wine.setName(name);
        wine.setRegion(region);
        wine.setWineType(wineType);
        wine.setYear(year);
        wine.setCountry(country);
        return wine;
    }

}
