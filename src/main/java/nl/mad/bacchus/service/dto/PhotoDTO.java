package nl.mad.bacchus.service.dto;

import java.io.IOException;

import nl.mad.bacchus.model.Photo;
import nl.mad.bacchus.model.Product;
import org.springframework.web.multipart.MultipartFile;

/**
 * Data transfer object for Photo data.
 * 
 * @author bas
 */
public class PhotoDTO {

    private byte[] content;
    private String contentType;

    private PhotoDTO() {
    }

    public static PhotoDTO newPhotoDTO(MultipartFile file) throws IOException {
        PhotoDTO photoDTO = new PhotoDTO();
        if (file != null && !file.isEmpty()) {
            photoDTO.content = file.getBytes();
            photoDTO.contentType = file.getContentType();
        }
        return photoDTO;
    }

    public Photo createPhotoFor(Product product) {
        return new Photo(content, product, contentType);
    }

    public boolean isEmpty() {
        return content == null;
    }
}
