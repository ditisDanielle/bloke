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

import nl.mad.bacchus.model.meta.Country;
import nl.mad.bacchus.model.meta.Labeled;
import nl.mad.bacchus.model.meta.WineRegion;
import nl.mad.bacchus.model.meta.WineType;

/**
 * Represents a tasty wine.
 *
 * @author Jeroen van Schagen
 * @since Jun 30, 2015
 */
@Entity
@DiscriminatorValue("wine")
@PrimaryKeyJoinColumn(name = "product_id")
public class Wine extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Country country = Country.UNKNOWN;
    @Enumerated(EnumType.STRING)
    private WineRegion region = WineRegion.UNKNOWN;
    @Enumerated(EnumType.STRING)
    private WineType wineType;
    @Transient
    private Integer year;
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
