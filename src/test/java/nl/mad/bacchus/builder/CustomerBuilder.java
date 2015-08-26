/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.builder;

import java.math.BigDecimal;

import nl.mad.bacchus.model.Customer;

import org.springframework.stereotype.Component;

/**
 * Builder of customers.
 *
 * @author Jeroen van Schagen
 * @since Apr 13, 2015
 */
@Component
public class CustomerBuilder extends AbstractBuilder {

    /**
     * Start building a custom customer.
     * 
     * @return the build command
     */
    public CustomerBuildCommand newCustomer() {
        return new CustomerBuildCommand();
    }

    /**
     * Allows building custom customer.
     *
     * @author Jeroen van Schagen
     * @since Apr 14, 2015
     */
    public class CustomerBuildCommand {
        
        private final Customer customer = new Customer();

        public CustomerBuildCommand() {
            withPassword("****");
        }

        /**
         * Set a full name.
         * @param fullName the new full name
         * @return this builder, for chaining
         */
        public CustomerBuildCommand withFullName(String fullName) {
            customer.setFullName(fullName);
            return this;
        }
        
        /**
         * Set an email adress.
         * @param email the new email
         * @return this builder, for chaining
         */
        public CustomerBuildCommand withEmail(String email) {
            customer.setEmail(email);
            return this;
        }
        
        /**
         * Set a balance.
         * @param balance the new balance
         * @return this builder, for chaining
         */
        public CustomerBuildCommand withBalance(BigDecimal balance) {
            customer.setBalance(balance);
            return this;
        }
        
        /**
         * Set a password.
         * @param password the new password
         * @return this builder, for chaining
         */
        public CustomerBuildCommand withPassword(String password) {
            customer.setPassword(password);
            return this;
        }

        /**
         * Build the customer.
         * @return the created customer
         */
        public Customer build() {
            return customer;
        }

        /**
         * Persists the customer.
         * @return the persisted customer
         */
        public Customer save() {
            return saveWithTransaction(build());
        }

    }

}
