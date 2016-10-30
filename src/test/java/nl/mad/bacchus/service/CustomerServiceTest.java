/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service;

import java.math.BigDecimal;

import com.google.common.collect.Iterables;
import nl.mad.bacchus.AbstractSpringTest;
import nl.mad.bacchus.builder.DataBuilder;
import nl.mad.bacchus.model.Address;
import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.model.User;
import nl.mad.bacchus.service.dto.CustomerDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerServiceTest extends AbstractSpringTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DataBuilder dataBuilder;

    private Customer jan;

    @Before
    public void setUp() {
        jan = dataBuilder.newCustomer().withEmail("jan@42.nl").withFullName("Jan de Man").withBalance(BigDecimal.valueOf(42)).save();
        loginWithRoles(Employee.ADMIN);
    }

    @Test
    public void testWithdraw() {
        Assert.assertEquals("40", customerService.withdraw(jan, BigDecimal.valueOf(2), "Order #42").toPlainString());
    }

    @Test(expected = IllegalStateException.class)
    public void testWithdrawNoMoney() {
        customerService.withdraw(jan, BigDecimal.valueOf(50), "Penalty");
    }

    @Test(expected = IllegalStateException.class)
    public void testWithdrawNegative() {
        customerService.withdraw(jan, BigDecimal.valueOf(-2), "Test");
    }
    
    @Test
    public void testDeposit() {
        Assert.assertEquals("44.00", customerService.deposit(jan.getId(), BigDecimal.valueOf(2), "").toPlainString());
    }

    @Test(expected = IllegalStateException.class)
    public void testDepositNegative() {
        customerService.deposit(jan.getId(), BigDecimal.valueOf(-2), "");
    }

    @Test
    public void testFindAllWithData() {
        Assert.assertEquals(1, Iterables.size(customerService.findAll()));
        Assert.assertEquals(jan.getId(), Iterables.getOnlyElement(customerService.findAll()).getId());
    }

    @Test
    public void testFindAllByEmail() {
        dataBuilder.newCustomer().withEmail("piet@42.nl").withFullName("Piet Je").save();

        User result = customerService.getByEmail("jan@42.nl");
        Assert.assertEquals(jan.getId(), result.getId());
        Assert.assertEquals(Customer.class, result.getClass());
    }

    @Test
    public void testSave() {
        Customer customer = dataBuilder.newCustomer().withEmail("jan@42.nl").withFullName("Janne Man").build();

        User result = customerService.create(CustomerDTO.toDetailResultDTO(customer));
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getId());
        Assert.assertEquals(Customer.class, result.getClass());
    }

    @Test
    public void testSaveWithStreet() {
        String street = "Pinksterbloem";
        Address address = new Address();
        address.setStreet(street);
        Customer customer = dataBuilder.newCustomer().withEmail("jan@42.nl").withFullName("Janne Man").withAddress(address).build();

        Customer result = customerService.create(CustomerDTO.toDetailResultDTO(customer));
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getId());
        Assert.assertEquals(result.getAddress().getStreet(), street);
        Assert.assertEquals(Customer.class, result.getClass());
    }

    @Test
    public void testSaveWithInvoiceAdress() {
        String invoiceStreet = "Bleiswijkseweg";

        Address invoiceAddress = new Address();
        invoiceAddress.setStreet(invoiceStreet);

        Customer customer = dataBuilder.newCustomer().withEmail("jan@42.nl").withFullName("Janne Man").withInvoiceAddress(invoiceAddress).build();

        Customer result = customerService.create(CustomerDTO.toDetailResultDTO(customer));
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getId());
        Assert.assertEquals(result.getInvoiceAddress().getStreet(), invoiceStreet);
        Assert.assertEquals(Customer.class, result.getClass());
    }

}

