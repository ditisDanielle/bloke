/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.controller;

import nl.mad.bacchus.model.Photo;
import nl.mad.bacchus.model.Cheese;
import nl.mad.bacchus.model.meta.CheeseType;
import nl.mad.bacchus.model.meta.MilkType;
import nl.mad.bacchus.service.CheeseService;
import nl.mad.bacchus.service.dto.CheeseDTO;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static nl.mad.bacchus.service.dto.PhotoDTO.newPhotoDTO;
import static nl.mad.bacchus.service.dto.CheeseDTO.toResultDTO;
import static org.springframework.util.FileCopyUtils.copy;

/**
 * {@link org.springframework.web.bind.annotation.RestController} for {@link nl.mad.bacchus.model.Cheese} stuff.
 */
@RestController
@RequestMapping("/cheese")
public class CheeseController {


    private final CheeseService cheeseService;

    @Autowired
    public CheeseController(CheeseService cheeseService) {
        this.cheeseService = cheeseService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CheeseDTO findById(@PathVariable Long id) {
        return toResultDTO((Cheese)cheeseService.findById(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CheeseDTO> get() {
        return stream(cheeseService.findAll().spliterator(), false).map(cheese -> toResultDTO((Cheese)cheese)).collect(toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    public CheeseDTO create(@Valid @RequestPart CheeseDTO form, @RequestPart(required = false) MultipartFile photo) throws IOException {
        Cheese cheese = cheeseService.create(form);
        cheeseService.savePhotoFor(cheese, newPhotoDTO(photo));
        return toResultDTO(cheese);
    }

    @RequestMapping(value = "/{cheeseId}", method = RequestMethod.POST)
    public CheeseDTO update(@PathVariable Long cheeseId, @Valid @RequestPart CheeseDTO form, @RequestPart(required = false) MultipartFile photo) throws IOException {
        Cheese cheese = cheeseService.update(cheeseId, form);
        cheeseService.savePhotoFor(cheese, newPhotoDTO(photo));
        return toResultDTO(cheese);
    }

    @RequestMapping(value = "/{cheeseId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long cheeseId) {
        cheeseService.delete(cheeseId);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<CheeseDTO> search(@RequestParam(required = false) String name,
                                @RequestParam(required = false) CheeseType cheeseType,
                                @RequestParam(required = false) MilkType milkType) {
        return cheeseService.search(name, cheeseType, milkType).stream().map(cheese -> toResultDTO(cheese)).collect(toList());
    }

    @RequestMapping(value = "/{cheeseId}/photo", method = RequestMethod.GET)
    public void getPhoto(@PathVariable Long cheeseId, HttpServletResponse response) throws IOException {
        Photo photo = cheeseService.findPhotoFor(cheeseId);
        response.setStatus(HttpStatus.OK.value());
        if (photo != null) {
            response.setContentType(photo.getContentType());
            response.setContentLength(photo.getContent().length);
            copy(photo.getContent(), response.getOutputStream());
        }
    }
}
