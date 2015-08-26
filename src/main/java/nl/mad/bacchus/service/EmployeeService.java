/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service;

import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.repository.EmployeeRepository;
import nl.mad.bacchus.service.dto.EmployeeDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing users.
 *
 * @author Jeroen van Schagen
 * @since Mar 11, 2015
 */
@Service
@Transactional
@Secured(Employee.ADMIN)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee findById(Long id) {
        return employeeRepository.findOne(id);
    }

    public Iterable<Employee> findAll() {
        return employeeRepository.findAll();
    }

    /**
     * Retrieve the employee with a specific email.
     * 
     * @param email the requested email
     * @return the employee with that email, if any
     */
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    /**
     * Creates an Employee with given data.
     * 
     * @param employeeDTO EmployeeDTO
     * @return the saved entity
     */
    public Employee create(EmployeeDTO employeeDTO) {
        return employeeRepository.save(employeeDTO.createEmployee());
    }

    /**
     * Updates an Employee with given data.
     * @param employeeId Long - current Employee
     * @param employeeDTO EmployeeDTO - update data
     * @return Employee
     */
    public Employee update(Long employeeId, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findOne(employeeId);
        return employeeRepository.save(employeeDTO.update(employee));
    }
}
