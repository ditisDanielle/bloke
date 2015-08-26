/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.controller;

import javax.validation.Valid;

import java.util.List;

import nl.mad.bacchus.service.EmployeeService;
import nl.mad.bacchus.service.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static nl.mad.bacchus.service.dto.EmployeeDTO.toDetailResultDTO;
import static nl.mad.bacchus.service.dto.EmployeeDTO.toListResultDTO;

/**
 * {@link org.springframework.web.bind.annotation.RestController} for {@link nl.mad.bacchus.model.Customer} stuff.
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public EmployeeDTO findById(@PathVariable Long id) {
        return toDetailResultDTO(employeeService.findById(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<EmployeeDTO> get() {
        return stream(employeeService.findAll().spliterator(), false).map(employee -> toListResultDTO(employee)).collect(toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public EmployeeDTO update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO form) {
        return toDetailResultDTO(employeeService.update(id, form));
    }

    @RequestMapping(method = RequestMethod.POST)
    public EmployeeDTO create(@Valid @RequestBody EmployeeDTO form) {
        return toDetailResultDTO(employeeService.create(form));
    }
}
