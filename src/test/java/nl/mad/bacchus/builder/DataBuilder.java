/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.builder;

import nl.mad.bacchus.builder.CustomerBuilder.CustomerBuildCommand;
import nl.mad.bacchus.builder.EmailBuilder.EmailBuildCommand;
import nl.mad.bacchus.builder.EmployeeBuilder.EmployeeBuildCommand;
import nl.mad.bacchus.builder.OrderBuilder.OrderBuildCommand;
import nl.mad.bacchus.builder.PhotoBuilder.PhotoBuildCommand;
import nl.mad.bacchus.builder.WineBuilder.WineBuildCommand;
import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Wine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Data builder facade.
 *
 * @author Jeroen van Schagen
 * @since Jul 6, 2015
 */
@Service
public class DataBuilder {

	@Autowired
	private CustomerBuilder customerBuilder;

	@Autowired
	private EmployeeBuilder employeeBuilder;

	@Autowired
	private OrderBuilder orderBuilder;

	@Autowired
	private WineBuilder wineBuilder;

	@Autowired
	private PhotoBuilder photoBuilder;

	@Autowired
	private EmailBuilder emailBuilder;

	/**
	 * Start building a new customer.
	 * 
	 * @return the customer build command
	 */
	public CustomerBuildCommand newCustomer() {
		return customerBuilder.newCustomer();
	}

	/**
	 * Start building a new employee.
	 * 
	 * @return the customer build command
	 */
	public EmployeeBuildCommand newEmployee() {
		return employeeBuilder.newEmployee();
	}

	/**
	 * Start building a new order.
	 * 
	 * @return the order build command
	 */
	public OrderBuildCommand newOrder() {
		return orderBuilder.newOrder();
	}

	/**
	 * Start building a new wine.
	 * 
	 * @return the wine build command
	 */
	public WineBuildCommand newWine() {
		return wineBuilder.newWine();
	}

	/**
     * start building a new photo for a wine.
     * 
     * @param wine Wine
     * @return
     */
    public PhotoBuildCommand newPhoto(Wine wine) {
		return photoBuilder.newPhoto(wine);
	}

	public EmailBuildCommand newEmail(Customer customer) {
		return emailBuilder.newEmail(customer);
	}
}
