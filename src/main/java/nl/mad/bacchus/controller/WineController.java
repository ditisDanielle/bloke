/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.controller;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static nl.mad.bacchus.service.dto.PhotoDTO.newPhotoDTO;
import static nl.mad.bacchus.service.dto.WineDTO.toResultDTO;
import static org.springframework.util.FileCopyUtils.copy;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import nl.mad.bacchus.model.Photo;
import nl.mad.bacchus.model.Wine;
import nl.mad.bacchus.model.meta.Country;
import nl.mad.bacchus.model.meta.WineRegion;
import nl.mad.bacchus.model.meta.WineType;
import nl.mad.bacchus.service.WineService;
import nl.mad.bacchus.service.dto.WineDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * {@link RestController} for {@link Wine} stuff.
 */
@RestController
@RequestMapping("/wine")
public class WineController {

    private final WineService wineService;

    @Autowired
    public WineController(WineService wineService) {
        this.wineService = wineService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public WineDTO findById(@PathVariable Long id) {
        return toResultDTO((Wine)wineService.findById(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<WineDTO> get() {
        return stream(wineService.findAll().spliterator(), false).map(wine -> toResultDTO((Wine)wine)).collect(toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    public WineDTO create(@Valid @RequestPart WineDTO form, @RequestPart(required = false) MultipartFile photo) throws IOException {
        Wine wine = wineService.create(form);
        try {
            wineService.savePhotoFor(wine, newPhotoDTO(photo));
        } catch (IllegalArgumentException e) {
            wineService.delete(wine.getId());
            throw e;
        }

        return toResultDTO(wine);
    }

    @RequestMapping(value = "/{wineId}", method = RequestMethod.POST)
    public WineDTO update(@PathVariable Long wineId, @Valid @RequestPart WineDTO form, @RequestPart(required = false) MultipartFile photo) throws IOException {
        Wine wine = wineService.update(wineId, form);
        wineService.savePhotoFor(wine, newPhotoDTO(photo));
        return toResultDTO(wine);
    }

    @RequestMapping(value = "/{wineId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long wineId) {
        wineService.delete(wineId);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<WineDTO> search(@RequestParam(required = false) String name,
            @RequestParam(required = false) Country country,
            @RequestParam(required = false) WineRegion wineRegion,
            @RequestParam(required = false) WineType wineType) {
        return wineService.search(name, country, wineRegion,  wineType).stream().map(wine -> toResultDTO(wine)).collect(toList());
    }

    @RequestMapping(value = "/{wineId}/photo", method = RequestMethod.GET)
    public void getPhoto(@PathVariable Long wineId, HttpServletResponse response) throws IOException {
        Photo photo = wineService.findPhotoFor(wineId);
        response.setStatus(HttpStatus.OK.value());
        if (photo != null) {
            response.setContentType(photo.getContentType());
            response.setContentLength(photo.getContent().length);
            copy(photo.getContent(), response.getOutputStream());
        }
    }
}
