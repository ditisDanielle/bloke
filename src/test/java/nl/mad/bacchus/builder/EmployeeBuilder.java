/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.builder;

import nl.mad.bacchus.model.Employee;

import org.springframework.stereotype.Component;

/**
 * Builder of employees.
 *
 * @author Jeroen van Schagen
 * @since Apr 13, 2015
 */
@Component
public class EmployeeBuilder extends AbstractBuilder {

    /**
     * Start building a custom employee.
     * 
     * @return the build command
     */
    public EmployeeBuildCommand newEmployee() {
        return new EmployeeBuildCommand();
    }

    /**
     * Allows building custom employee.
     *
     * @author Jeroen van Schagen
     * @since Apr 14, 2015
     */
    public class EmployeeBuildCommand {
        
        private final Employee employee = new Employee();

        public EmployeeBuildCommand() {
            withPassword("****");
        }

        /**
         * Set a full name.
         * @param fullName the new full name
         * @return this builder, for chaining
         */
        public EmployeeBuildCommand withFullName(String fullName) {
            employee.setFullName(fullName);
            return this;
        }
        
        /**
         * Set an email adress.
         * @param email the new email
         * @return this builder, for chaining
         */
        public EmployeeBuildCommand withEmail(String email) {
            employee.setEmail(email);
            return this;
        }

        /**
         * Set a password.
         * @param password the new password
         * @return this builder, for chaining
         */
        public EmployeeBuildCommand withPassword(String password) {
            employee.setPassword(password);
            return this;
        }

        /**
         * Build the employee.
         * @return the created employee
         */
        public Employee build() {
            return employee;
        }

        /**
         * Persists the employee.
         * @return the persisted employee
         */
        public Employee save() {
            return saveWithTransaction(build());
        }

    }

}
