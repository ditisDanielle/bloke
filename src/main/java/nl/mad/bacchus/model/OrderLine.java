/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.math.BigDecimal;

/**
 * Represents a line in an order.
 *
 * @author Jeroen van Schagen
 * @since Jun 30, 2015
 */
@Entity
public class OrderLine extends BaseEntity {
    
    @ManyToOne
    private Product product;

/*
    @ManyToOne
    @JoinColumn(name ="customer_id")
    private Customer customer;

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    } */

    
    private BigDecimal cost;

    private int quantity;

    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public BigDecimal getCost() {
        return cost;
    }
    
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
