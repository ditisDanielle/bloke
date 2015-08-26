/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service;

import java.io.IOException;

import com.google.common.collect.Iterables;
import nl.mad.bacchus.AbstractSpringTest;
import nl.mad.bacchus.builder.DataBuilder;
import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.model.Photo;
import nl.mad.bacchus.model.Wine;
import nl.mad.bacchus.model.meta.WineRegion;
import nl.mad.bacchus.model.meta.WineType;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static nl.mad.bacchus.service.dto.PhotoDTO.newPhotoDTO;
import static nl.mad.bacchus.service.dto.WineDTO.toResultDTO;
import static org.springframework.util.FileCopyUtils.copyToByteArray;

/**
 * Wine service test.
 *
 * @author Jeroen van Schagen
 * @since Apr 13, 2015
 */
public class WineServiceTest extends AbstractSpringTest {

    @Autowired
    private WineService wineService;
    @Autowired
    private DataBuilder dataBuilder;

    @Before
    public void setUp() {
        loginWithRoles(Employee.ADMIN);
    }

    @Test
    public void testFindAllEmpty() {
        Iterable<Wine> result = wineService.findAll();
        Assert.assertThat(result, Matchers.emptyIterable());
    }

    @Test
    public void testFindAllWithData() {
        Wine wine = dataBuilder.newWine().withName("Chateau d’Yquem Sauternes").save();

        Assert.assertEquals(1, Iterables.size(wineService.findAll()));
        Assert.assertEquals(wine.getId(), Iterables.getOnlyElement(wineService.findAll()).getId());
    }

    @Test
    public void testFindAllByName() {
        Wine wine = dataBuilder.newWine().withName("Chateau Haut Brion").save();
        dataBuilder.newWine().withName("Chateau Latour").save();

        Wine result = wineService.findById(wine.getId());
        Assert.assertEquals(wine.getId(), result.getId());
    }

    @Test
    public void testSave() throws IOException {
        Wine wine = dataBuilder.newWine().withName("Chateau Latour")
                                         .withSpecs(WineType.RED, WineRegion.BORDEAUX, 2012)
                                             .build();

        Wine result = wineService.create(toResultDTO(wine));
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getId());
        
        Wine created = wineService.findById(result.getId());
        Assert.assertEquals(WineType.RED, created.getWineType());
        Assert.assertEquals(WineRegion.BORDEAUX, created.getRegion());
    }

    @Test
    public void testDelete() {
        Wine wine = dataBuilder.newWine().withName("Chateau Haut Brion").save();
        Wine result = wineService.findById(wine.getId());
        Assert.assertEquals(wine.getId(), result.getId());
        wineService.delete(wine.getId());
        Assert.assertNull(wineService.findById(wine.getId()));
    }
    @Test
    public void testDeleteWithPhoto() throws IOException {
        Wine wine = dataBuilder.newWine().withName("Chateau Latour").build();

        byte[] winePhoto = copyToByteArray(new ClassPathResource("/wines/latache.jpeg").getInputStream());
        MultipartFile file = new MockMultipartFile("name", "originalName", "image/jpeg", winePhoto);

        //TODO: Test deletion of a Wine with a connected Photo...
    }

    @Test
    public void testSaveWithPhoto() throws IOException {
        Wine wine = dataBuilder.newWine().withName("Chateau Latour").withSpecs(WineType.RED, WineRegion.BORDEAUX).build();

        byte[] winePhoto = copyToByteArray(new ClassPathResource("/wines/latache.jpeg").getInputStream());
        MultipartFile file = new MockMultipartFile("name", "originalName", "image/jpeg", winePhoto);

        Wine result = wineService.create(toResultDTO(wine));
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getId());

        wineService.savePhotoFor(result, newPhotoDTO(file));
        Photo photo = wineService.findPhotoFor(result.getId());
        Assert.assertNotNull(photo);
        Assert.assertNotNull(photo.getId());
        Assert.assertEquals("image/jpeg", photo.getContentType());
        Assert.assertEquals(winePhoto.length, photo.getContent().length);
        
        result = wineService.update(result.getId(), toResultDTO(result));
        wineService.savePhotoFor(result, newPhotoDTO(new MockMultipartFile("name", "originalName", "image/jpeg", "jaja".getBytes())));
        Photo updatedPhoto = wineService.findPhotoFor(result.getId());
        Assert.assertEquals(winePhoto.length, photo.getContent().length);
        Assert.assertEquals(4, updatedPhoto.getContent().length);
    }

    @Test
    public void testSearch() {
        dataBuilder.newWine().withName("Geralt").withSpecs(WineType.ROSE, WineRegion.BORDEAUX, 2010).save();
        dataBuilder.newWine().withName("Yennefer").withSpecs(WineType.RED, WineRegion.BORDEAUX, 2010).save();
        dataBuilder.newWine().withName("Ciri").withSpecs(WineType.ROSE, WineRegion.BORDEAUX, 2009).save();
        dataBuilder.newWine().withName("Triss").withSpecs(WineType.RED, WineRegion.MCLAREN_VALE, 2010).save();

        //TODO: implement test
    }
}
