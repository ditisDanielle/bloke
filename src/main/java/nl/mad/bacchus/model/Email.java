package nl.mad.bacchus.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import java.time.LocalDateTime;

/**
 * Holds the mails sent to a customer.
 */
@Entity
public class Email extends BaseEntity {

    @Basic(optional = false) // just for DDL generation in local in mem HSQLDB
	private boolean sent = false;

	private LocalDateTime created;

	private LocalDateTime sentOn;

    @Basic(optional = false) // just for DDL generation in local in mem HSQLDB
	private String emailFrom;

	@ManyToOne
	private Customer customer;

    @Basic(optional = false) // just for DDL generation in local in mem HSQLDB
	private String emailTo;

    @Basic(optional = false) // just for DDL generation in local in mem HSQLDB
	private String title;
	
    @Basic(optional = false) // just for DDL generation in local in mem HSQLDB
	private String message;

	public Email() {
		created = LocalDateTime.now();
	}

    public Email(Customer customer) {
        this();
        this.customer = customer;
        this.emailTo = customer.getEmail();
    }

	public boolean isSent() {
		return sent;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public LocalDateTime getSentOn() {
		return sentOn;
	}

	public Customer getCustomer() {
		return customer;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getEmailFrom() {
		return emailFrom;
	}
	
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}
	
	public void markAsSent() {
		if (!sent) {
			sent = true;
			sentOn = LocalDateTime.now();
		}
	}

}
