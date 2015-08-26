package nl.mad.bacchus.repository;

import java.util.List;

import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {

	/**
	 * find the emails sent to a specific customer.
	 * @param customer the customer.
	 * @return the emails.
	 */
	List<Email> findByCustomer(Customer customer);
	
	/**
     * Finds the email with given id if the mail belongs to given Customer.
     * @param id Long
     * @param customer Customer
     * @return Email
     */
    Email findByIdAndCustomer(Long id, Customer customer);

    /**
     * @return an unsent email.
     */
	Email findFirstBySentFalseOrderById();
	
}
