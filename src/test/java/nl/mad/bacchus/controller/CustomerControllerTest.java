package nl.mad.bacchus.controller;

import java.util.Arrays;

import mockit.Expectations;
import mockit.Injectable;
import nl.mad.bacchus.AbstractControllerTest;
import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.User;
import nl.mad.bacchus.repository.CustomerRepository;
import nl.mad.bacchus.service.CustomerService;
import nl.mad.bacchus.service.dto.CustomerDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class CustomerControllerTest extends AbstractControllerTest {

    @Injectable
    private CustomerService customerService;
    @Injectable
    private CustomerRepository customerRepository;

    @Before
    public void setUp() {
        initWebClient(new CustomerController(customerService));
    }

    @Test
    public void testFindById() throws Exception {
        User user = new Customer();
        user.setFullName("R2-D2");

        new Expectations() {
            {
                customerService.findById(1L);
                result = user;
            }
        };

        this.webClient.perform(MockMvcRequestBuilders.get("/customer/1"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("R2-D2"));
    }

    @Test
    public void testFindAll() throws Exception {
        User user = new Customer();
        user.setFullName("Darth Vader");

        new Expectations() {
            {
                customerService.findAll();
                result = Arrays.asList(user);
            }
        };

        this.webClient.perform(MockMvcRequestBuilders.get("/customer"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fullName").value("Darth Vader"));
    }

    @Test
    public void testUpdate() throws Exception {

        Customer newCustomer = new Customer();
        newCustomer.setFullName("Behold your new overlord");

        new Expectations() {
            {
                customerService.update(1L, (CustomerDTO) any);
                result = newCustomer;
            }
        };

        this.webClient
                .perform(
                        MockMvcRequestBuilders
                                .put("/customer/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"email\":\"ex@plosie.nl\", \"fullName\":\"Behold your new overlord\", \"password\":\"wachtwoord\", \"balance\":100}"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Behold your new overlord"));
    }

    @Test
    public void testCreate() throws Exception {
        Customer newCustomer = new Customer();
        newCustomer.setFullName("Gabe Newell");
        newCustomer.setEmail("no@hl3.fyi");

        new Expectations() {
            {
                customerService.create((CustomerDTO) any);
                result = newCustomer;
            }
        };

        this.webClient
                .perform(
                        MockMvcRequestBuilders
                                .post("/customer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"email\":\"no@hl3.fyi\", \"fullName\":\"Gabe Newell\", \"password\":\"wachtwoord\", \"balance\":100}"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Gabe Newell"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("no@hl3.fyi"));
    }
}
