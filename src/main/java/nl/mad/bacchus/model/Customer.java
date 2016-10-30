/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.model;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Customer that can buy products.
 *
 * @author Jeroen van Schagen
 * @since Jun 30, 2015
 */
@Entity
@DiscriminatorValue("customer")
@Access(AccessType.FIELD)
public class Customer extends User {
    
    public static final String ROLE = "ROLE_customer";
    private BigDecimal balance = BigDecimal.ZERO;

    @Embedded
    private Address address = new Address();


    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="street", column = @Column(name = "invoice_street")),
        @AttributeOverride(name="streetNumber", column = @Column(name = "invoice_street_number")),
        @AttributeOverride(name="city", column = @Column(name = "invoice_city")),
        @AttributeOverride(name="postalCode", column = @Column(name = "invoice_postal_code")),
    })
    private Address invoiceAddress = new Address();


    /**
     * {@inheritDoc}
     */
    @Override
    public String getRole() {
        return ROLE;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(Address invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }
}

