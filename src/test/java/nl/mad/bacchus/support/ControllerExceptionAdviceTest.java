package nl.mad.bacchus.support;

import java.util.Arrays;
import java.util.List;

import mockit.Capturing;
import mockit.Expectations;
import mockit.Injectable;
import nl.mad.bacchus.AbstractControllerTest;
import nl.mad.bacchus.controller.WineController;
import nl.mad.bacchus.service.WineService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class ControllerExceptionAdviceTest extends AbstractControllerTest {

    @Injectable
    private WineService wineService;

    @Before
    public void setUp() {
        initWebClient(new WineController(wineService));
    }

    @Test
    public void testGeneralException() throws Exception {
        new Expectations() {
            {
                wineService.findAll();
                result = new Exception("Some message", new Exception("Root message"));
            }
        };
        
        this.webClient.perform(MockMvcRequestBuilders.get("/wine"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.jsonPath("type").value(Exception.class.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Some message"))
                .andExpect(MockMvcResultMatchers.jsonPath("rootCauseMessage").value("Root message"));
    }



    @Test
    public void testIllegalStateException() throws IllegalStateException
    {

    }

    @Test
    public void testInvalidRequest(@Capturing MethodArgumentNotValidException ex, @Capturing BindingResult binding) throws Exception {
        List<FieldError> errors = Arrays.asList(new FieldError("object", "field", "message"));
        
        new Expectations() {
            {
                wineService.findAll();
                result = ex;
                
                ex.getBindingResult();
                result = binding;
                
                binding.getFieldErrors();
                result = errors;
            }
        };
        
        this.webClient.perform(MockMvcRequestBuilders.get("/wine"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(422));
    }

    @Test
    public void testUnsupportedMethod() throws Exception {
        this.webClient.perform(MockMvcRequestBuilders.post("/winezz").contentType(MediaType.APPLICATION_JSON))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

}
