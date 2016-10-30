/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.model;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an order by a customer.
 *
 * @author Jeroen van Schagen
 * @since Jun 30, 2015
 */
@Entity
@Table(name = "\"order\"")
public class Order extends BaseEntity {

    public static enum OrderStatus {
        IN_PROGRESS, COMPLETED, REFUNDED;
    }

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private LocalDateTime date = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.IN_PROGRESS;

    @OneToMany(targetEntity = OrderLine.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private List<OrderLine> lines = new ArrayList<>();

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public void addLine(OrderLine line) {
        lines.add(line);
    }

    public BigDecimal getCost() {
        BigDecimal cost = BigDecimal.ZERO;
        for (OrderLine line : lines) {
            cost = cost.add(line.getCost().multiply(BigDecimal.valueOf(line.getQuantity())));
        }
        return cost;
    }

}
