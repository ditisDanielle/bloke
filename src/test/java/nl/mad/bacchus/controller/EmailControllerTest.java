package nl.mad.bacchus.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.security.Principal;
import java.util.Arrays;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Verifications;
import nl.mad.bacchus.AbstractControllerTest;
import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Email;
import nl.mad.bacchus.service.EmailService;
import nl.mad.bacchus.service.dto.EmailDTO;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EmailControllerTest extends AbstractControllerTest {

    @Injectable
    private EmailService emailService;
    
    private ObjectMapper objectMapper = new ObjectMapper();

    private Principal principal;

    @Before
    public void setUp() {
        initWebClient(new EmailController(emailService, objectMapper));
        principal = new Principal() {
            @Override
            public String getName() {
                return "user@jaja.nl";
            }
        };
    }

    @Test
    public void testFindById() throws Exception {
        Email email = new Email();
        email.setMessage("jaja");

        new Expectations() {
            {
                emailService.findById(1L, "user@jaja.nl");
                result = email;
            }
        };

        this.webClient.perform(MockMvcRequestBuilders.get("/email/1").principal(principal))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("jaja"));
    }

    @Test
    public void testFindByCustomer() throws Exception {
        Email email = new Email();
        email.setId(1L);
        email.setTitle("the mail");
        email.setMessage("jaja");

        new Expectations() {
            {
                emailService.findEmailByCustomer("user@jaja.nl");
                result = Arrays.asList(email);
            }
        };

        this.webClient.perform(MockMvcRequestBuilders.get("/email/customer/current").principal(principal))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("the mail"));

    }

    @Test
    public void testSendBulkMail() throws Exception {

        this.webClient.perform(MockMvcRequestBuilders.post("/email/customer/all").principal(principal).content("{"
                + "\"title\":\"the mail subject\", "
                + "\"message\":\"the mail body\""
                + "}").contentType(APPLICATION_JSON))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200));

        new Verifications() {
            {
                EmailDTO dto;
                emailService.sendEmailToAllCustomers(dto = withCapture(), "user@jaja.nl");
                times = 1;
                Email email = dto.createEmailFor(new Customer(), "from@jaja.nl");
                Assert.assertThat(email, Matchers.hasProperty("title", Matchers.equalTo("the mail subject")));
                Assert.assertThat(email, Matchers.hasProperty("message", Matchers.equalTo("the mail body")));
            }
        };
    }
}
