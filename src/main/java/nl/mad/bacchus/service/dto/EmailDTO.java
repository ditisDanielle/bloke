package nl.mad.bacchus.service.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Email;

/**
 * Data transfer object for Email fields.
 * @author bas
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class EmailDTO {

    private Long id;
    private String message;
    private String title;
    private String emailFrom;
    private String emailTo;

    private EmailDTO() {
    }

    public static EmailDTO toListResultDTO(Email email) {
        EmailDTO dto = new EmailDTO();
        dto.id = email.getId();
        dto.title = email.getTitle();
        return dto;
    }

    public static EmailDTO toDetailResultDTO(Email email) {
        EmailDTO dto = new EmailDTO();
        dto.title = email.getTitle();
        dto.message = email.getMessage();
        dto.emailFrom = email.getEmailFrom();
        dto.emailTo = email.getEmailTo();
        return dto;
    }

    public Email createEmailFor(Customer customer, String from) {
        Email email = new Email(customer);
        email.setMessage(message);
        email.setTitle(title);
        email.setEmailFrom(from);
        return email;
    }
}
