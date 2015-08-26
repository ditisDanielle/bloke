/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service;

import nl.mad.bacchus.model.Photo;
import nl.mad.bacchus.repository.PhotoRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
     * Deletes the current Photo of the given Photo's Wine if any exists.
     * Saves the given Photo afterwards
     * @param photo Photo
     * @return the saved photo
     */
    Photo save(Photo photo) {
        if (!"image/jpeg".equals(photo.getContentType())) {
            throw new IllegalArgumentException("Invalid content type, should be JPEG!");
        }

        Photo currentPhoto = photoRepository.findByWineId(photo.getWine().getId());
        if (currentPhoto != null) {
            photoRepository.delete(currentPhoto);
        }
        return photoRepository.save(photo);
    }
    
    /**
     * Finds photo for given Wine id.
     * @param wineId Long
     * @return Photo
     */
    Photo findPhotoFor(Long wineId) {
        return photoRepository.findByWineId(wineId);
    }

}
