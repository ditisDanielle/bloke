package nl.mad.bacchus.controller;

import nl.mad.bacchus.AbstractControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;

public class EnumControllerTest extends AbstractControllerTest {

    @Before
    public void setUp() {
        initWebClient(new EnumController());
    }

    @Test
    public void testGetWineEnums() throws Exception {
        this.webClient.perform(MockMvcRequestBuilders.get("/enum/wine"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.country", hasSize(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country[0].id").value("UNKNOWN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country[0].label").value("Onbekend"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.wineRegion").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.wineRegion", hasSize(14)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wineRegion[1].id").value("BORDEAUX"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wineRegion[1].label").value("Bordeaux"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.wineType").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.wineType", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wineType[0].id").value("RED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wineType[0].label").value("Rood"));
    }

}
