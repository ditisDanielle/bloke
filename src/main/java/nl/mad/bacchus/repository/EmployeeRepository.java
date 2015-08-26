/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.repository;

import nl.mad.bacchus.model.Employee;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing employees.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Retrieve the employee by email.
     * 
     * @param email the email of the employee
     * @return the employee
     */
    Employee findByEmail(String email);

}
