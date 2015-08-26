package nl.mad.bacchus.controller;

import java.util.Arrays;

import mockit.Expectations;
import mockit.Injectable;
import nl.mad.bacchus.AbstractControllerTest;
import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class EmployeeControllerTest extends AbstractControllerTest {

    @Injectable
    private EmployeeService employeeService;

    private EmployeeController controller;

    @Before
    public void setUp() {
        controller = new EmployeeController(employeeService);
        initWebClient(controller);
    }

    @Test
    public void testFindById() throws Exception {
        Employee user = new Employee();
        user.setFullName("R2-D2");

        new Expectations() {
            {
                employeeService.findById(1L);
                result = user;
            }
        };

        this.webClient.perform(MockMvcRequestBuilders.get("/employee/1"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("R2-D2"));
    }

    @Test
    public void testFindAll() throws Exception {
        Employee user = new Employee();
        user.setFullName("Darth Vader");

        new Expectations() {
            {
                employeeService.findAll();
                result = Arrays.asList(user);
            }
        };

        this.webClient.perform(MockMvcRequestBuilders.get("/employee"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fullName").value("Darth Vader"));
    }

}
