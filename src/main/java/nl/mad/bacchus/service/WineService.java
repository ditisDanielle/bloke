/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service;

import java.util.List;

import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.model.Photo;
import nl.mad.bacchus.model.Product;
import nl.mad.bacchus.model.Wine;
import nl.mad.bacchus.model.meta.Country;
import nl.mad.bacchus.model.meta.WineRegion;
import nl.mad.bacchus.model.meta.WineType;
import nl.mad.bacchus.repository.PhotoRepository;
import nl.mad.bacchus.repository.WineRepository;
import nl.mad.bacchus.service.dto.PhotoDTO;
import nl.mad.bacchus.service.dto.WineDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing wines.
 *
 * @author Jeroen van Schagen
 * @since Mar 11, 2015
 */
@Service
public class WineService extends ReadService<Wine> {

    private final WineRepository wineRepository;
    private final PhotoService photoService;

    @Autowired
    public WineService(WineRepository wineRepository, PhotoService photoService) {
        super(wineRepository);
        this.photoService = photoService;
        this.wineRepository = wineRepository;
    }


    /**
     * Searches for wines that match the given name, country, region, wine type and year.
     * @param name name of the wine to look for, if empty any name is considered valid
     * @param country Country to look for, if null any country is considered valid
     * @param wineType Wine type to look for, if null any type is considered valid
     * @return List of all wines found
     */
    public List<Wine> search(String name, Country country, WineRegion wineRegion, WineType wineType) {
        String wineName = (name != null) ? name : "";
        String countryName = (country != null) ? country.name() : "";
        String wineRegionName = (wineRegion != null) ? wineRegion.name() : "";
        String typeName = (wineType != null) ? wineType.name() : "";
        return wineRepository.search(wineName, countryName,wineRegionName, typeName);
    }

    /**
     * Creates a Wine with given wine- and photo data.
     * @param wineDTO the wine data
     *
     * @return Wine
     */
    @Secured(Employee.ADMIN)
    public Wine create(WineDTO wineDTO) {
        return save(wineDTO.createWine());
    }

    /**
     * Updates the Wine with given wineId with given wine- and photo data.
     * @param wineId the id of the Wine to update 
     * @param wineDTO the wine data
     * @return Wine
     */
    @Secured(Employee.ADMIN)
    public Wine update(Long wineId, WineDTO wineDTO) {
        Wine wine = (Wine)wineRepository.findOne(wineId);
        return save(wineDTO.update(wine));
    }

    private Wine save(Wine wine) {
        return wineRepository.save(wine);
    }

    /**
     * Deletes the given wine with photo.
     * @param wineId Long
     */
    @Secured(Employee.ADMIN)
    public void delete(Long wineId) {
        photoService.delete(wineId);
        wineRepository.delete(wineId);
    }
    
    /**
     * Saves the photo for our wine.
     * 
     * @param wine the wine
     * @param photoDTO the photo DTO
     */
    public void savePhotoFor(Wine wine, PhotoDTO photoDTO) {
        if (!photoDTO.isEmpty()) {
            photoService.save(photoDTO.createPhotoFor(wine));
        }
    }

    /**
     * Finds photo for given Wine.
     * @param wine Wine
     * @return Photo
     */
    public Photo findPhotoFor(Long wineId) {
        return photoService.findPhotoFor(wineId);
    }

    
}
