/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.controller;

import javax.validation.Valid;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import nl.mad.bacchus.service.CustomerService;
import nl.mad.bacchus.service.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static nl.mad.bacchus.service.dto.CustomerDTO.toDetailResultDTO;
import static nl.mad.bacchus.service.dto.CustomerDTO.toListResultDTO;

/**
 * {@link org.springframework.web.bind.annotation.RestController} for {@link nl.mad.bacchus.model.Customer} stuff.
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CustomerDTO findById(@PathVariable Long id) {
        return toDetailResultDTO(customerService.findById(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CustomerDTO> get() {
        return stream(customerService.findAll().spliterator(), false).map(customer -> toListResultDTO(customer)).collect(toList());
    }

    @RequestMapping(value = "/{customerId}", method = RequestMethod.PUT)
    public CustomerDTO update(@PathVariable Long customerId, @Valid @RequestBody CustomerDTO form) {
        return toDetailResultDTO(customerService.update(customerId, form));
    }

    @RequestMapping(method = RequestMethod.POST)
    public CustomerDTO create(@Valid @RequestBody CustomerDTO form) {
        return toDetailResultDTO(customerService.create(form));
    }

    @RequestMapping(value = "/{customerId}/deposit", method = RequestMethod.PUT)
    public Map<String, BigDecimal> deposit(@PathVariable Long customerId, @RequestBody @Valid CustomerBalanceChange body) {
        BigDecimal amount = customerService.deposit(customerId, body.getAmount(), "Deposit by Admin");
        return Collections.singletonMap("result", amount);
    }

    public static class CustomerBalanceChange {

        private BigDecimal amount;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

    }

}
