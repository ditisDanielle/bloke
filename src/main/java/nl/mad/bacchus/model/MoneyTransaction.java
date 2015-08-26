/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.model;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 * Represents the transaction of money.
 *
 * @author Jeroen van Schagen
 * @since Jul 6, 2015
 */
@Entity
public class MoneyTransaction extends BaseEntity {
    
    @ManyToOne
    private Customer customer;
    
    @Temporal(TIMESTAMP)
    private Date date;
    
    private BigDecimal amount;
    
    private String description;

    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

}
