/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service;

import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.model.Photo;
import nl.mad.bacchus.repository.PhotoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

/**
 * Service for managing photos.
 *
 * @author Jeroen van Schagen
 * @since Jul 10, 2015
 */
@Service
public class PhotoService {
    
    private final PhotoRepository photoRepository;
    
    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    /**
     * Saves the given Photo afterwards
     * @param photo Photo
     * @return the saved photo
     */
    Photo save(Photo photo) {
        if (!"image/jpeg".equals(photo.getContentType())) {
            throw new IllegalArgumentException("Invalid content type, should be JPEG!");
        }

        Photo currentPhoto = photoRepository.findByProductId(photo.getProduct().getId());
        if (currentPhoto != null) {
            photoRepository.delete(currentPhoto);
        }
        return photoRepository.save(photo);
    }

    /**
     * Finds photo for given Wine id.
     * @param  productId Long
     * @return Photo
     */
    Photo findPhotoFor(Long productId) {
        return photoRepository.findByProductId(productId);
    }

    /**
     * Deletes the current Photo of the given Photo's Wine id if any exists.
     * @param productId
     */
    void delete(Long productId){
        Photo photo = photoRepository.findByProductId(productId);
        if(photo != null){
            photoRepository.delete(photo.getId());
        }

    }

}
