/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.controller;

import javax.validation.Valid;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;

import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.service.CustomerService;
import nl.mad.bacchus.service.OrderService;
import nl.mad.bacchus.service.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static nl.mad.bacchus.service.dto.OrderDTO.toDetailResultDTO;
import static nl.mad.bacchus.service.dto.OrderDTO.toListResultDTO;

/**
 * {@link org.springframework.web.bind.annotation.RestController} for {@link nl.mad.bacchus.model.Order} stuff.
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService /*,CustomerService customerService*/) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public OrderDTO findById(@PathVariable Long id) {
        return toDetailResultDTO(orderService.findById(id));
    }

    @Secured({Employee.ADMIN})
    @RequestMapping(method = RequestMethod.GET)
    public List<OrderDTO> get() {
        return stream(orderService.findAll().spliterator(), false)
                .map(order -> toListResultDTO(order))
                .collect(toList());
    }

    @Secured({Customer.ROLE})
    @RequestMapping(value = "/mine", method = RequestMethod.GET)
    public List<OrderDTO> mine(Principal principal) {

        Customer customer = orderService.getCustomerByEmail(principal.getName());
        return orderService.getOrdersFromCustomer(customer).stream().map(order -> toListResultDTO(order)).collect(toList());
    }

    @Secured({Customer.ROLE, Employee.ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public OrderDTO create(@Valid @RequestBody OrderDTO form, Principal principal) {
        return toDetailResultDTO(orderService.create(form, principal.getName()));
    }

    @Secured({Employee.ADMIN})
    @RequestMapping(value = "/{id}/complete", method = RequestMethod.PUT)
    public OrderDTO complete(@PathVariable Long id) {
        return toDetailResultDTO(orderService.complete(id));
    }

    @Secured({Employee.ADMIN})
    @RequestMapping(value = "/{id}/refund", method = RequestMethod.PUT)
    public OrderDTO refund(@PathVariable Long id) {
        return toDetailResultDTO(orderService.refund(id));
    }

    @Secured({Customer.ROLE})
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public List<OrderDTO> getHistoricOrders() throws ParseException {
        return stream(orderService.getHistoricOrders().spliterator(), false).map(order -> toListResultDTO(order)).collect(toList());
    }
}
