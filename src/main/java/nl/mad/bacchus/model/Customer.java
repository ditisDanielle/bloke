/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

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
public class Customer extends User {
    
    public static final String ROLE = "ROLE_customer";
    private BigDecimal balance = BigDecimal.ZERO;

    private String street;
    private String streetNumber;
    private String city;
    private String postalCode;
    
    private String invoiceStreet;
    private String invoiceStreetNumber;
    private String invoiceCity;
    private String invoicePostalCode;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();
    
    public void addOrder(Order order) {
        orders.add(order);
    }
    
    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

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

    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getStreetNumber() {
        return streetNumber;
    }
    
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getInvoiceStreet() {
        return invoiceStreet;
    }
    
    public void setInvoiceStreet(String invoiceStreet) {
        this.invoiceStreet = invoiceStreet;
    }
    
    public String getInvoiceStreetNumber() {
        return invoiceStreetNumber;
    }
    
    public void setInvoiceStreetNumber(String invoiceStreetNumber) {
        this.invoiceStreetNumber = invoiceStreetNumber;
    }
    
    public String getInvoiceCity() {
        return invoiceCity;
    }

    public void setInvoiceCity(String invoiceCity) {
        this.invoiceCity = invoiceCity;
    }

    public String getInvoicePostalCode() {
        return invoicePostalCode;
    }

    public void setInvoicePostalCode(String invoicePostalCode) {
        this.invoicePostalCode = invoicePostalCode;
    }
}
