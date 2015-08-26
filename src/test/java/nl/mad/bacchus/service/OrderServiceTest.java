/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import nl.mad.bacchus.AbstractSpringTest;
import nl.mad.bacchus.builder.DataBuilder;
import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.model.Order;
import nl.mad.bacchus.model.Order.OrderStatus;
import nl.mad.bacchus.model.Wine;
import nl.mad.bacchus.service.dto.OrderDTO;
import nl.mad.bacchus.service.dto.OrderLineDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Order service test.
 *
 * @author Jeroen van Schagen
 * @since Apr 13, 2015
 */
public class OrderServiceTest extends AbstractSpringTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private DataBuilder dataBuilder;

    @Before
    public void setUp() {
        loginWithRoles(Employee.ADMIN);
    }

    @Test
    public void testCreate() {
        Customer customer = dataBuilder.newCustomer().withEmail("jan@42.nl").withFullName("Janne Man").withBalance(new BigDecimal(42)).save();
        Wine wine = dataBuilder.newWine().withName("Bla").withCost(BigDecimal.ONE).save();

        OrderDTO param = new OrderDTO();
        OrderLineDTO product = new OrderLineDTO();
        ReflectionTestUtils.setField(product, "id", wine.getId());
        ReflectionTestUtils.setField(product, "quantity", 10);
        ReflectionTestUtils.setField(param, "lines", Arrays.asList(product));

        Order result = orderService.create(param, customer.getEmail());
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getId());

        Assert.assertEquals(1, result.getLines().size());
        Assert.assertEquals("10.00", result.getCost().toPlainString());
    }

    @Test
    public void testComplete() {
        Customer customer = dataBuilder.newCustomer().withEmail("jan@42.nl").withFullName("Janne Man").withBalance(new BigDecimal(42)).save();
        Order order = dataBuilder.newOrder().withCustomer(customer).save();

        order = orderService.complete(order.getId());

        Assert.assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }

    @Test
    public void testRefund() {
        Customer customer = dataBuilder.newCustomer().withEmail("jan@42.nl").withFullName("Janne Man").withBalance(new BigDecimal(42)).save();
        Order order = dataBuilder.newOrder().withCustomer(customer).save();

        order = orderService.refund(order.getId());

        Assert.assertEquals(OrderStatus.REFUNDED, order.getStatus());
    }

    @Test
    public void testFindByIdAdmin() {
        Customer customer = dataBuilder.newCustomer().withEmail("jan@42.nl").withFullName("Janne Man").withBalance(new BigDecimal(42)).save();
        Order order = dataBuilder.newOrder().withCustomer(customer).save();

        loginWithRoles(Employee.ADMIN);
        Assert.assertEquals(order.getId(), orderService.findById(order.getId()).getId());
    }

    @Test
    public void testFindByIdMine() {
        Customer customer = dataBuilder.newCustomer().withEmail("jan@42.nl").withFullName("Janne Man").withBalance(new BigDecimal(42)).save();
        Order order = dataBuilder.newOrder().withCustomer(customer).save();

        login(customer.getEmail(), Customer.ROLE);
        Assert.assertEquals(order.getId(), orderService.findById(order.getId()).getId());
    }

    @Test(expected = SecurityException.class)
    public void testFindById() {
        Customer customer = dataBuilder.newCustomer().withEmail("jan@42.nl").withFullName("Janne Man").withBalance(new BigDecimal(42)).save();
        Order order = dataBuilder.newOrder().withCustomer(customer).save();

        login("other@42.nl", Customer.ROLE);
        orderService.findById(order.getId());
    }

    @Test
    public void testHistoricOrders() {
        Customer customer = dataBuilder.newCustomer().withEmail("jan@42.nl").withFullName("Janne Man").withBalance(new BigDecimal(42)).save();

        LocalDateTime current = LocalDateTime.now();
        LocalDateTime aLongTimeAgo = current.minusMonths(1);
        LocalDateTime aLongTimeAgo2 = current.minusMonths(4);

        //less than 1 month old
        dataBuilder.newOrder().withDate(current).withCustomer(customer).save();
        //1 month old
        Order order = dataBuilder.newOrder().withDate(aLongTimeAgo).withCustomer(customer).save();
        //4 months old
        Order order1 = dataBuilder.newOrder().withDate(aLongTimeAgo2).withCustomer(customer).save();

        List<Order> orders = orderService.getHistoricOrders();
        Assert.assertEquals(2, orders.size());
        Assert.assertEquals(order.getDate(), orders.get(0).getDate());
        Assert.assertEquals(order1.getDate(), orders.get(1).getDate());
    }
}
