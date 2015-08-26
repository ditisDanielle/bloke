/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.model;

import javax.persistence.Entity;
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
    private Wine wine;
    
    private BigDecimal cost;

    private int quantity;

    public Wine getWine() {
        return wine;
    }
    
    public void setWine(Wine wine) {
        this.wine = wine;
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
