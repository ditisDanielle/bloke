/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service;

import com.google.common.collect.Iterables;
import nl.mad.bacchus.AbstractSpringTest;
import nl.mad.bacchus.builder.DataBuilder;
import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.model.User;
import nl.mad.bacchus.service.dto.EmployeeDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

public class EmployeeServiceTest extends AbstractSpringTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DataBuilder dataBuilder;

    private Employee defaultEmployee;

    @Before
    public void setUp() {
        defaultEmployee = dataBuilder.newEmployee().withEmail("jan@42.nl").withFullName("Jan de Man").save();
        loginWithRoles(Employee.ADMIN);
    }

    @Test
    public void testFindAllWithData() {
        Assert.assertEquals(1, Iterables.size(employeeService.findAll()));
        Assert.assertEquals(defaultEmployee.getId(), Iterables.getOnlyElement(employeeService.findAll()).getId());
    }

    @Test
    public void testFindByEmail() {
        dataBuilder.newCustomer().withEmail("piet@42.nl").withFullName("Piet Je").save();

        User result = employeeService.findByEmail("jan@42.nl");
        Assert.assertEquals(defaultEmployee.getId(), result.getId());
        Assert.assertEquals(Employee.class, result.getClass());
    }

    @Test
    public void testSave() {
        EmployeeDTO emp = new EmployeeDTO();
        ReflectionTestUtils.setField(emp, "password", "jaja");
        ReflectionTestUtils.setField(emp, "email", "jan@42.nl");
        ReflectionTestUtils.setField(emp, "fullName", "Janne Man");

        User result = employeeService.create(emp);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getId());
        Assert.assertEquals(Employee.class, result.getClass());
        Assert.assertEquals("Janne Man", result.getFullName());

        ReflectionTestUtils.setField(emp, "fullName", "Janne Mann");

        result = employeeService.update(result.getId(), emp);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getId());
        Assert.assertEquals(Employee.class, result.getClass());
        Assert.assertEquals("Janne Mann", result.getFullName());
    }

}
