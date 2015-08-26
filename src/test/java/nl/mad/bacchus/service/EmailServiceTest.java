package nl.mad.bacchus.service;

import java.util.List;

import nl.mad.bacchus.AbstractSpringTest;
import nl.mad.bacchus.builder.DataBuilder;
import nl.mad.bacchus.model.Email;
import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.service.dto.EmailDTO;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailServiceTest extends AbstractSpringTest {

    @Autowired
    private EmailService emailService;
    @Autowired
    private DataBuilder dataBuilder;

    @Before
    public void setUp() {
        loginWithRoles(Employee.ADMIN);
    }

    @Test
    public void testSendBulkMail() throws Exception {
        dataBuilder.newCustomer().withEmail("customer1@jaja.nl").save();
        dataBuilder.newCustomer().withEmail("customer2@jaja.nl").save();

        Email spam = new Email();
        spam.setMessage("the message");
        spam.setTitle("the title");

        emailService.sendEmailToAllCustomers(EmailDTO.toDetailResultDTO(spam), "employee@jaja.nl");

        Thread.sleep(5000); // wait for the mails to be sent

        List<Email> emails = emailService.findEmailByCustomer("customer1@jaja.nl");
        Assert.assertThat(emails, Matchers.hasSize(1));
        Assert.assertThat(emails.get(0), Matchers.hasProperty("message", Matchers.is("the message")));
        Assert.assertThat(emails.get(0), Matchers.hasProperty("customer", Matchers.hasProperty("email", Matchers.is("customer1@jaja.nl"))));

        Email email = emailService.findById(emails.get(0).getId(), "customer1@jaja.nl");
        Assert.assertNotNull(email);

        email = emailService.findById(emails.get(0).getId(), "customer2@jaja.nl");
        Assert.assertNull(email);
    }
}
