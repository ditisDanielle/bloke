/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Preconditions;
import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.model.Order;
import nl.mad.bacchus.model.Order.OrderStatus;
import nl.mad.bacchus.model.Wine;
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

    @Autowired
    public OrderService(OrderRepository orderRepository,
            CustomerService customerService, WineService wineService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.wineService = wineService;
    }

    /**
     * Returns all Orders.
     * @return Iterable<Order>
     */
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
            Wine wine = wineService.findById(orderLineDTO.getWineId());
            order.addLine(orderLineDTO.createOrderLine(wine));
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
}
