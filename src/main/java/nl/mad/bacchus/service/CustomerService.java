/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.transaction.Transactional;

import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.model.MoneyTransaction;
import nl.mad.bacchus.repository.CustomerRepository;
import nl.mad.bacchus.repository.MoneyTransactionRepository;
import nl.mad.bacchus.service.dto.CustomerDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

/**
 * Service for managing customers.
 *
 * @author Jeroen van Schagen
 * @since Jul 6, 2015
 */
@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final MoneyTransactionRepository transactionRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, MoneyTransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    @Secured(Employee.ADMIN)
    public Customer findById(Long id) {
        return customerRepository.findOne(id);
    }

    @Secured(Employee.ADMIN)
    public Iterable<Customer> findAll() {
        return customerRepository.findAll();
    }

    /**
     * Retrieve the customer by an email adress.
     * @param email the email
     * @return the customer
     */
    public Customer getByEmail(String email) {
        Customer result = customerRepository.findByEmail(email);
        return Preconditions.checkNotNull(result, "No customer with email: " + email);
    }
    
    /**
     * Subtract money from the customer.
     * @param email the customer's email
     * @param amount the amount of money to subtract
     * @return the new customer balance
     */
    BigDecimal withdraw(Customer customer, BigDecimal amount, String description) {
        Preconditions.checkState(amount.compareTo(BigDecimal.ZERO) >= 0, "Amount cannot be negative.");
        Preconditions.checkState(customer.getBalance().compareTo(amount) >= 0, "Insufficient funds.");

        customer.setBalance(customer.getBalance().subtract(amount));
        createTransaction(customer, BigDecimal.valueOf(-1).multiply(amount), description);
        customerRepository.save(customer);
        return customer.getBalance();
    }

    /**
     * Saves given Customer data. 
     * @param customerDTO CustomerDTO
     * @return Customer
     */
    @Secured(Employee.ADMIN)
    public Customer create(CustomerDTO customerDTO) {
        return customerRepository.save(customerDTO.createCustomer());
    }

    /**
     * Updates Customer with given id with given data.
     * @param customerId Long
     * @param customerDTO CustomerDTO
     * @return Customer
     */
    @Secured(Employee.ADMIN)
    public Customer update(Long customerId, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findOne(customerId);
        return customerRepository.save(customerDTO.update(customer));
    }

    /**
     * Provide money to the customer.
     * 
     * @param customer Customer
     * @param amount the amount of money to provide
     * @param description the description
     * @return the new customer balance
     */
    @Secured(Employee.ADMIN)
    public BigDecimal deposit(Long customerId, BigDecimal amount, String description) {
        Preconditions.checkState(amount.compareTo(BigDecimal.ZERO) >= 0, "Amount cannot be negative.");
        Customer customer = customerRepository.findOne(customerId);
        customer.setBalance(customer.getBalance().add(amount));
        createTransaction(customer, amount, description);
        customerRepository.save(customer);
        return customer.getBalance();
    }

    private void createTransaction(Customer customer, BigDecimal amount, String description) {
        MoneyTransaction transaction = new MoneyTransaction();
        transaction.setCustomer(customer);
        transaction.setAmount(amount);
        transaction.setDate(new Date());
        transaction.setDescription(description);
        transactionRepository.save(transaction);
    }

}
