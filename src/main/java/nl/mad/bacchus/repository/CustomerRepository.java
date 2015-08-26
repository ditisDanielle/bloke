/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.repository;

import nl.mad.bacchus.model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing customers.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Retrieve the customer by email.
     * 
     * @param email the email of the customer
     * @return the customer
     */
    Customer findByEmail(String email);

}
