/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.controller;

import javax.validation.Valid;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;

import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.service.CustomerService;
import nl.mad.bacchus.service.OrderService;
import nl.mad.bacchus.service.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final CustomerService customerService;

    @Autowired
    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public OrderDTO findById(@PathVariable Long id) {
        return toDetailResultDTO(orderService.findById(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<OrderDTO> get() {
        return stream(orderService.findAll().spliterator(), false)
                .map(order -> toListResultDTO(order))
                .collect(toList());
    }

    @RequestMapping(value = "/mine", method = RequestMethod.GET)
    public List<OrderDTO> mine(Principal principal) {
        Customer customer = customerService.getByEmail(principal.getName());
        return customer.getOrders().stream()
                .map(order -> toListResultDTO(order))
                .collect(toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    public OrderDTO create(@Valid @RequestBody OrderDTO form, Principal principal) {
        return toDetailResultDTO(orderService.create(form, principal.getName()));
    }

    @RequestMapping(value = "/{id}/complete", method = RequestMethod.PUT)
    public OrderDTO complete(@PathVariable Long id) {
        return toDetailResultDTO(orderService.complete(id));
    }

    @RequestMapping(value = "/{id}/refund", method = RequestMethod.PUT)
    public OrderDTO refund(@PathVariable Long id) {
        return toDetailResultDTO(orderService.refund(id));
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public List<OrderDTO> getHistoricOrders() throws ParseException {
        return stream(orderService.getHistoricOrders().spliterator(), false).map(order -> toListResultDTO(order)).collect(toList());
    }
}
