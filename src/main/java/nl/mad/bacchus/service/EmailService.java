package nl.mad.bacchus.service;

import javax.transaction.Transactional;

import java.util.List;

import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Email;
import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.repository.EmailRepository;
import nl.mad.bacchus.service.dto.EmailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EmailService {

    public static final Integer DEFAULT_MAIL_SEND_DURATION_MILLIS = 2000;
	private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);
	private EmailRepository repo;
    private CustomerService customerService;

	@Autowired
    public EmailService(EmailRepository repo, CustomerService customerService) {
		this.repo = repo;
        this.customerService = customerService;
	}

    public Email findById(Long id, String customerEmail) {
        return repo.findByIdAndCustomer(id, customerService.getByEmail(customerEmail));
    }

    public List<Email> findEmailByCustomer(String customerEmail) {
        return repo.findByCustomer(customerService.getByEmail(customerEmail));
	}

	@Async
    @Secured(Employee.ADMIN)
    public void sendEmailToAllCustomers(EmailDTO emailDTO, String fromEmail) {
        Iterable<Customer> customers = customerService.findAll();
        for (Customer cust : customers) {
            sendEmail(emailDTO.createEmailFor(cust, fromEmail));
		}
	}

    private void sendEmail(Email email) {
        // sending email takes a few seconds...
        try {
            Thread.sleep(DEFAULT_MAIL_SEND_DURATION_MILLIS);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        email.markAsSent();
        repo.save(email);
        LOG.info("Sent email '" + email.getId() + "' to '" + email.getEmailTo() + "'");
	}

}
