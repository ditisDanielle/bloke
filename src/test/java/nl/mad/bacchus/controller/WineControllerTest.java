package nl.mad.bacchus.controller;

import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;
import java.util.Arrays;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Verifications;
import nl.mad.bacchus.AbstractControllerTest;
import nl.mad.bacchus.model.Photo;
import nl.mad.bacchus.model.Wine;
import nl.mad.bacchus.model.meta.Country;
import nl.mad.bacchus.model.meta.WineType;
import nl.mad.bacchus.repository.WineRepository;
import nl.mad.bacchus.service.WineService;
import nl.mad.bacchus.service.dto.WineDTO;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class WineControllerTest extends AbstractControllerTest {

    @Injectable
    private WineService wineService;
    @Injectable
    private WineRepository wineRepository;

    @Before
    public void setUp() {
        initWebClient(new WineController(wineService));
    }

    @Test
    public void testFindAll() throws Exception {
        Wine wine = new Wine();
        wine.setName("Jan");

        new Expectations() {
            {
                wineService.findAll();
                result = Arrays.asList(wine);
            }
        };

        this.webClient.perform(MockMvcRequestBuilders.get("/wine"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Jan"));
    }

    @Test
    public void testCreate() throws Exception {
        Wine wine = new Wine();
        wine.setName("Bordeaux");
        wine.setCost(BigDecimal.valueOf(10.55));

        new Expectations() {
            {
                wineService.create((WineDTO) any);
                result = wine;
            }
        };

        MockMultipartFile jsonFile = new MockMultipartFile("form", "", "application/json", "{\"name\":\"Bordeaux\", \"cost\": \"10.55\"}".getBytes());
        this.webClient.perform(MockMvcRequestBuilders.fileUpload("/wine").file(jsonFile))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Bordeaux"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost").value(10.55));
    }

    @Test
    public void testCreateWithPhoto() throws Exception {
        Wine wine = new Wine();
        wine.setName("Bordeaux");
        wine.setCost(BigDecimal.valueOf(10.55));

        new Expectations() {
            {
                wineService.create((WineDTO) any);
                result = wine;
            }
        };

        MockMultipartFile photoFile = new MockMultipartFile("photo", "bordeaux.jpg", "image/jpg", "photobytes".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("form", "", "application/json", "{\"name\":\"Bordeaux\", \"cost\": \"10.55\"}".getBytes());
        this.webClient.perform(MockMvcRequestBuilders.fileUpload("/wine").file(jsonFile).file(photoFile))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Bordeaux"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost").value(10.55));
    }

    @Test
    public void testCreateValidationError() throws Exception {

        MockMultipartFile jsonFile = new MockMultipartFile("form", "", "application/json", "{\"name\":\"\", \"cost\": \"10.55\"}".getBytes());
        this.webClient.perform(MockMvcRequestBuilders.fileUpload("/wine").file(jsonFile))
                .andExpect(MockMvcResultMatchers.status().is(422))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].field").value("name"));
    }

    @Test
    public void testDownloadPhoto() throws Exception {
        Wine wine = new Wine();
        new Expectations() {
            {
                wineService.findPhotoFor(1L);
                result = new Photo("jaja".getBytes(), wine, "image/jpg");
            }
        };
        this.webClient.perform(MockMvcRequestBuilders.get("/wine/1/photo"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "image/jpg"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testUpdate() throws Exception {

        Wine form = new Wine();
        form.setName("Bordeaux");
        form.setCost(BigDecimal.valueOf(10.55));

        Wine wine = new Wine();
        wine.setId(1L);
        wine.setName("Bordeaux");
        wine.setCost(BigDecimal.valueOf(12.98));

        new Expectations() {
            {
                wineService.update(1L, (WineDTO) any);
                result = form;
            }
        };

        MockMultipartFile photoFile = new MockMultipartFile("photo", "bordeaux.jpg", "image/jpg", "photobytes".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("form", "", "application/json", "{\"name\":\"Bordeaux\", \"cost\": \"10.55\"}".getBytes());
        this.webClient.perform(MockMvcRequestBuilders.fileUpload("/wine/1").file(jsonFile).file(photoFile))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Bordeaux"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost").value(10.55));
    }

    @Test
    public void testDelete() throws Exception {

        this.webClient.perform(MockMvcRequestBuilders.delete("/wine/1"))
                .andExpect(MockMvcResultMatchers.status().is(200));

        new Verifications() {
            {
                wineService.delete(1L);
                times = 1;
            }
        };
    }

    @Test
    public void testSearch() throws Exception {
        Wine wine = new Wine();
        wine.setName("Pieters");

        new Expectations() {
            {
                wineService.search("Pieters", Country.FRANCE, WineType.RED);
                result = wine;
            }
        };

        this.webClient.perform(
                MockMvcRequestBuilders.get("/wine/search")
                        .param("name", "Pieters")
                        .param("country", "FRANCE")
                        .param("wineRegion", "BORDEAUX")
                        .param("wineType", "RED"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Pieters"));
    }
}
