package nl.mad.bacchus.controller;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Verifications;
import nl.mad.bacchus.AbstractControllerTest;
import nl.mad.bacchus.model.Photo;
import nl.mad.bacchus.model.Cheese;
import nl.mad.bacchus.model.meta.CheeseType;
import nl.mad.bacchus.model.meta.MilkType;
import nl.mad.bacchus.repository.CheeseRepository;
import nl.mad.bacchus.service.CheeseService;
import nl.mad.bacchus.service.dto.CheeseDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;

/**
 * Created by fd on 1-10-16.
 */
public class CheeseControllerTest extends AbstractControllerTest {

    @Injectable
    private CheeseService cheeseService;
    @Injectable
    private CheeseRepository cheeseRepository;

    @Before
    public void setUp() {
        initWebClient(new CheeseController(cheeseService));
    }

    @Test
    public void testFindAll() throws Exception {
        Cheese cheese = new Cheese();
        cheese.setName("Frank");

        new Expectations() {
            {
                cheeseService.findAll();
                result = Arrays.asList(cheese);
            }
        };

        this.webClient.perform(MockMvcRequestBuilders.get("/cheese"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Frank"));
    }


    @Test
    public void testCreate() throws Exception {
        Cheese cheese = new Cheese();
        cheese.setName("Parmazaan");
        cheese.setCost(BigDecimal.valueOf(22.55));

        new Expectations() {
            {
                cheeseService.create((CheeseDTO) any);
                result = cheese;
            }
        };

        MockMultipartFile jsonFile = new MockMultipartFile("form", "", "application/json", "{\"name\":\"Parmazaan\", \"cost\": \"22.55\"}".getBytes());
        this.webClient.perform(MockMvcRequestBuilders.fileUpload("/cheese").file(jsonFile))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Parmazaan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost").value(22.55));
    }

    @Test
    public void testCreateWithPhoto() throws Exception {
        Cheese cheese = new Cheese();
        cheese.setName("Parmazaan");
        cheese.setCost(BigDecimal.valueOf(22.55));

        new Expectations() {
            {
                cheeseService.create((CheeseDTO) any);
                result = cheese;
            }
        };

        MockMultipartFile photoFile = new MockMultipartFile("photo", "parmazaan.jpg", "image/jpg", "photobytes".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("form", "", "application/json", "{\"name\":\"Parmazaan\", \"cost\": \"22.55\"}".getBytes());
        this.webClient.perform(MockMvcRequestBuilders.fileUpload("/cheese").file(jsonFile).file(photoFile))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Parmazaan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost").value(22.55));
    }


    @Test
    public void testCreateValidationError() throws Exception {

        MockMultipartFile jsonFile = new MockMultipartFile("form", "", "application/json", "{\"name\":\"\", \"cost\": \"22.55\"}".getBytes());
        this.webClient.perform(MockMvcRequestBuilders.fileUpload("/cheese").file(jsonFile))
                .andExpect(MockMvcResultMatchers.status().is(422))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].field").value("name"));
    }

    @Test
    public void testDownloadPhoto() throws Exception {
        Cheese cheese = new Cheese();
        new Expectations() {
            {
                cheeseService.findPhotoFor(1L);
                result = new Photo("jaja".getBytes(), cheese, "image/jpg");
            }
        };
        this.webClient.perform(MockMvcRequestBuilders.get("/cheese/1/photo"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "image/jpg"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


    @Test
    public void testUpdate() throws Exception {

        Cheese form = new Cheese();
        form.setName("Parmazaan");
        form.setCost(BigDecimal.valueOf(22.55));

        Cheese cheese = new Cheese();
        cheese.setId(1L);
        cheese.setName("Parmazaan");
        cheese.setCost(BigDecimal.valueOf(22.98));

        new Expectations() {
            {
                cheeseService.update(1L, (CheeseDTO) any);
                result = form;
            }
        };

        MockMultipartFile photoFile = new MockMultipartFile("photo", "parmazaan.jpg", "image/jpg", "photobytes".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("form", "", "application/json", "{\"name\":\"Parmazaan\", \"cost\": \"22.55\"}".getBytes());
        this.webClient.perform(MockMvcRequestBuilders.fileUpload("/cheese/1").file(jsonFile).file(photoFile))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Parmazaan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost").value(22.55));
    }


    @Test
    public void testDelete() throws Exception {

        this.webClient.perform(MockMvcRequestBuilders.delete("/cheese/1"))
                .andExpect(MockMvcResultMatchers.status().is(200));

        new Verifications() {
            {
                cheeseService.delete(1L);
                times = 1;
            }
        };
    }


    @Test
    public void testSearch() throws Exception {
        Cheese cheese = new Cheese();
        cheese.setName("Frank");

        new Expectations() {
            {
                cheeseService.search("Frank", CheeseType.BLUE_MOLD, MilkType.COW);
                result = cheese;
            }
        };

        this.webClient.perform(
                MockMvcRequestBuilders.get("/cheese/search")
                        .param("name", "Frank")
                        .param("cheeseType", "BLUE_MOLD")
                        .param("milkType", "COW"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Frank"));
    }




}
