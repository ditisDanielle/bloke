package nl.mad.bacchus.controller;

import java.util.Arrays;

import mockit.Expectations;
import mockit.Injectable;
import nl.mad.bacchus.AbstractControllerTest;
import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Order;
import nl.mad.bacchus.repository.CustomerRepository;
import nl.mad.bacchus.repository.OrderRepository;
import nl.mad.bacchus.service.CustomerService;
import nl.mad.bacchus.service.OrderService;
import nl.mad.bacchus.service.dto.OrderDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class OrderControllerTest extends AbstractControllerTest {

    @Injectable
    private OrderService orderService;
    @Injectable
    private CustomerService customerService;
    @Injectable
    private OrderRepository orderRepository;
    @Injectable
    private CustomerRepository customerRepository;

    private Order testOrder;

    @Before
    public void setUp() {
        initWebClient(new OrderController(orderService, customerService));
        testOrder = new Order();
        testOrder.setId(151L);
        Customer customer = new Customer();
        customer.setFullName("Jantje Koopgraag");
        testOrder.setCustomer(customer);
    }

    @Test
    public void testCreate() throws Exception {

        new Expectations() {
            {
                orderService.create((OrderDTO) any, "jan@42.nl");
                result = testOrder;
            }
        };

        this.webClient.perform(MockMvcRequestBuilders.post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"products\":[]}")
                .principal(new UsernamePasswordAuthenticationToken("jan@42.nl", "")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(151));
    }

    @Test
    public void testComplete() throws Exception {

        new Expectations() {
            {
                orderService.complete(151L);
                result = testOrder;
            }
        };

        this.webClient.perform(MockMvcRequestBuilders.put("/order/151/complete"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(151));
    }

    @Test
    public void testRefund() throws Exception {

        new Expectations() {
            {
                orderService.refund(151L);
                result = testOrder;
            }
        };

        this.webClient.perform(MockMvcRequestBuilders.put("/order/151/refund"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(151));
    }

    @Test
    public void testGetHistoricalOrders() throws Exception {

        new Expectations() {
            {
                orderService.getHistoricOrders();
                result = Arrays.asList(testOrder);
            }
        };

        this.webClient.perform(MockMvcRequestBuilders.get("/order/history"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(151));
    }
}
