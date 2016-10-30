/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;
import javax.transaction.Transactional;

import nl.mad.bacchus.model.meta.*;

/**
 * Represents a tasty wine.
 *
 * @author Jeroen van Schagen
 * @since Jun 30, 2015
 */
@Entity
@PrimaryKeyJoinColumn(name = "product_id")
public class Wine extends Product{

    @Enumerated(EnumType.STRING)
    private Country country = Country.UNKNOWN;
    @Enumerated(EnumType.STRING)
    private WineRegion region = WineRegion.UNKNOWN;
    @Enumerated(EnumType.STRING)
    private WineType wineType;

    private Integer year;


    public WineRegion getRegion() {
        return region;
    }

    public void setRegion(WineRegion region) {
        this.region = region;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public WineType getWineType() {
        return wineType;
    }

    public void setWineType(WineType wineType) {
        this.wineType = wineType;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public static <T extends Enum & Labeled> Map<String, T[]> getEnumValues() {
        Map<String, T[]> result = new HashMap<>();
        result.put("country", (T[]) Country.values());
        result.put("wineRegion", (T[]) WineRegion.values());
        result.put("wineType", (T[]) WineType.values());
        return result;
    }
}
