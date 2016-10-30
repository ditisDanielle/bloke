/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.repository;

import java.util.List;

import nl.mad.bacchus.model.Product;
import nl.mad.bacchus.model.Wine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for managing for wines.
 */
public interface WineRepository extends JpaRepository<Wine, Long> {

    /**
     * Retrieve the wine by name.
     * 
     * @param name the name of wine
     * @return the wine
     */
    Wine findByName(String name);



    /**
     * returns all wines matching the given country, regio, type and year
     * @param name Name of the wine to look for, if null any name will be considered valid
     * @param country Country to look for, if null any country will be considered valid 
     * @param type Type to look for, if null any wine type will be considered valid
     * @return list of wines found
     */
    @Query("select wine FROM Wine wine " +
            "where (:name = '' or lower(wine.name) like lower(concat('%', :name, '%'))) " +
            "and (:country = '' or wine.country = :country) " +
            "and (:region = '' or wine.region = :region)" +
            "and (:wineType = '' or wine.wineType = :wineType) ")
    List<Wine> search(@Param("name") String name,
            @Param("country") String country,
                      @Param("region") String region,
            @Param("wineType") String type);

}
