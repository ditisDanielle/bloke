/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Preconditions;
import nl.mad.bacchus.model.*;
import nl.mad.bacchus.model.Order.OrderStatus;
import nl.mad.bacchus.repository.OrderRepository;
import nl.mad.bacchus.service.dto.OrderDTO;
import nl.mad.bacchus.service.dto.OrderLineDTO;
import nl.mad.bacchus.support.security.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing orders.
 *
 * @author Jeroen van Schagen
 * @since Jul 6, 2015
 */
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final WineService wineService;
    private final CheeseService cheeseService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
            CustomerService customerService, WineService wineService, CheeseService cheeseService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.wineService = wineService;
        this.cheeseService = cheeseService;
    }

    /**
     * Returns all Orders.
     * @return Iterable<Order>
     */
    @Secured(Employee.ADMIN)
    public Iterable<Order> findAll() {
        return orderRepository.findAll();
    }

    /**
     * Returns the Order for given id.
     * @param id Long
     * @return Order
     */
    public Order findById(Long id) {
        Order order = orderRepository.findOne(id);
        if (!Users.hasRole(Employee.ADMIN) && !Objects.equals(Users.getName(), order.getCustomer().getEmail())) {
            throw new SecurityException("Not allowed to access this order.");
        }
        return order;
    }
    
    /**
     * Create an order.
     * 
     * @param orderDTO the order to create
     * @param currentUserEmail the customer's email
     * @return the created order
     */
    public Order create(OrderDTO orderDTO, String currentUserEmail) {

        Customer customer = customerService.getByEmail(currentUserEmail);
        Order order = orderDTO.createOrderFor(customer);
        for (OrderLineDTO orderLineDTO : orderDTO.getLines()) {
            Product product;
            if(cheeseService.findById(orderLineDTO.getProductId()) == null)
            {
                product = wineService.findById(orderLineDTO.getProductId());
            }
            else
            {
                product = cheeseService.findById(orderLineDTO.getProductId());
            }


            order.addLine(orderLineDTO.createOrderLine(product));
        }
        customerService.withdraw(order.getCustomer(), order.getCost(), "Order #" + order.getId());
        return orderRepository.save(order);
    }

    /**
     * Completes an order with given id.
     * 
     * @param orderId the order to complete
     * @return the complete order
     */
    @Secured(Employee.ADMIN)
    public Order complete(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        Preconditions.checkState(OrderStatus.IN_PROGRESS.equals(order.getStatus()), "Can only complete in progress orders.");
        order.setStatus(OrderStatus.COMPLETED);
        return orderRepository.save(order);
    }

    /**
     * Refund an order with given Id.
     * 
     * @param order the order to refund
     * @return the refunded order
     */
    @Secured(Employee.ADMIN)
    public Order refund(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        Preconditions.checkState(!OrderStatus.REFUNDED.equals(order.getStatus()), "Order was already refunded.");
        order.setStatus(OrderStatus.REFUNDED);
        customerService.deposit(order.getCustomer().getId(), order.getCost(), "Refund order #" + order.getId());
        return orderRepository.save(order);
    }

    /**
     * Returns all orders from before or on the given date
     * @param date Date to use as limit
     * @return list of orders
     */
    @Secured(Employee.ADMIN)
    public List<Order> getOrdersBeforeOrOnDate(LocalDateTime date) {
        return orderRepository.findAllByDateLessThanEqual(date);
    }

    /**
     * Returns all orders that are at least a month old
     * @return list of orders older than a month
     */
    @Secured(Employee.ADMIN)
    public List<Order> getHistoricOrders() {
        return this.getOrdersBeforeOrOnDate(LocalDateTime.now().minusMonths(1));
    }

    public Customer getCustomerByEmail(String email)
    {
        Customer result = customerService.getByEmail(email);
        return Preconditions.checkNotNull(result, "No customer with email: " + email);
    }

    public List<Order> getOrdersFromCustomer(Customer customer)
    {
        List<Order> result =  orderRepository.findAllByCustomerId(customer.getId());
        return Preconditions.checkNotNull(result, "No orders for customer: " + customer.getFullName());
    }

}
