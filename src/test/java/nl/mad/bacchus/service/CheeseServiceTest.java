package nl.mad.bacchus.service;

import com.google.common.collect.Iterables;
import nl.mad.bacchus.AbstractSpringTest;
import nl.mad.bacchus.builder.DataBuilder;
import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.model.Photo;
import nl.mad.bacchus.model.Cheese;
import nl.mad.bacchus.model.meta.MilkType;
import nl.mad.bacchus.model.meta.CheeseType;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static nl.mad.bacchus.service.dto.PhotoDTO.newPhotoDTO;
import static nl.mad.bacchus.service.dto.CheeseDTO.toResultDTO;
import static org.springframework.util.FileCopyUtils.copyToByteArray;

/**
 * Created by fd on 3-10-16.
 */
public class CheeseServiceTest extends AbstractSpringTest{

    @Autowired
    private CheeseService cheeseService;
    @Autowired
    private DataBuilder dataBuilder;

    @Before
    public void setUp() {
        loginWithRoles(Employee.ADMIN);
    }

    @Test
    public void testFindAllEmpty() {
        Iterable<Cheese> result = cheeseService.findAll();
        Assert.assertThat(result, Matchers.emptyIterable());
    }

    @Test
    public void testFindAllWithData() {
        Cheese cheese = dataBuilder.newCheese().withName("Kaas").save();

        Assert.assertEquals(1, Iterables.size(cheeseService.findAll()));
        Assert.assertEquals(cheese.getId(), Iterables.getOnlyElement(cheeseService.findAll()).getId());
    }

    @Test
    public void testFindAllByName() {
        Cheese cheese = dataBuilder.newCheese().withName("Kaas").save();
        dataBuilder.newCheese().withName("Koos").save();

        Cheese result = (Cheese)cheeseService.findById(cheese.getId());
        Assert.assertEquals(cheese.getId(), result.getId());
    }

    @Test
    public void testSave() throws IOException {
        Cheese cheese = dataBuilder.newCheese().withName("Kaas")
                .withSpecs(CheeseType.BLUE_MOLD, MilkType.GOAT)
                .build();

        Cheese result = cheeseService.create(toResultDTO(cheese));
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getId());

        Cheese created = (Cheese)cheeseService.findById(result.getId());
        Assert.assertEquals(CheeseType.BLUE_MOLD, created.getCheeseType());
        Assert.assertEquals(MilkType.GOAT, created.getMilkType());
    }

    @Test
    public void testDelete() {
        Cheese cheese = dataBuilder.newCheese().withName("Kaas").save();
        Cheese result = (Cheese)cheeseService.findById(cheese.getId());
        Assert.assertEquals(cheese.getId(), result.getId());
        cheeseService.delete(cheese.getId());
        Assert.assertNull(cheeseService.findById(cheese.getId()));
    }
    @Test
    public void testDeleteWithPhoto() throws IOException {
        Cheese cheese = dataBuilder.newCheese().withName("Kaas").build();

        byte[] cheesePhoto = copyToByteArray(new ClassPathResource("/cheeses/comte.jpeg").getInputStream());
        MultipartFile file = new MockMultipartFile("name", "originalName", "image/jpeg", cheesePhoto);

        //TODO: Test deletion of a Cheese with a connected Photo...
    }

    @Test
    public void testSaveWithPhoto() throws IOException {
        Cheese cheese = dataBuilder.newCheese().withName("Kaas").withSpecs(CheeseType.BLUE_MOLD, MilkType.GOAT).build();

        byte[] cheesePhoto = copyToByteArray(new ClassPathResource("/cheeses/comte.jpeg").getInputStream());
        MultipartFile file = new MockMultipartFile("name", "originalName", "image/jpeg", cheesePhoto);

        Cheese result = cheeseService.create(toResultDTO(cheese));
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getId());

        cheeseService.savePhotoFor(result, newPhotoDTO(file));
        Photo photo = cheeseService.findPhotoFor(result.getId());
        Assert.assertNotNull(photo);
        Assert.assertNotNull(photo.getId());
        Assert.assertEquals("image/jpeg", photo.getContentType());
        Assert.assertEquals(cheesePhoto.length, photo.getContent().length);

        result = cheeseService.update(result.getId(), toResultDTO(result));
        cheeseService.savePhotoFor(result, newPhotoDTO(new MockMultipartFile("name", "originalName", "image/jpeg", "jaja".getBytes())));
        Photo updatedPhoto = cheeseService.findPhotoFor(result.getId());
        Assert.assertEquals(cheesePhoto.length, photo.getContent().length);
        Assert.assertEquals(4, updatedPhoto.getContent().length);
    }

    @Test
    public void testSearch() {
        dataBuilder.newCheese().withName("Kaas").withSpecs(CheeseType.BLUE_MOLD, MilkType.GOAT).save();
        dataBuilder.newCheese().withName("Koos").withSpecs(CheeseType.HARD, MilkType.COW).save();
        dataBuilder.newCheese().withName("Kees").withSpecs(CheeseType.SOFT, MilkType.EWE).save();
        dataBuilder.newCheese().withName("Kuus").withSpecs(CheeseType.SOFT, MilkType.COW).save();

        //TODO: implement test
    }
}
