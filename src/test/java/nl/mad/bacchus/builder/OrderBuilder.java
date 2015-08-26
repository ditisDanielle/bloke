/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.builder;

import java.time.LocalDateTime;

import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Order;
import nl.mad.bacchus.model.OrderLine;
import nl.mad.bacchus.model.Wine;
import org.springframework.stereotype.Component;

/**
 * Builder of orders.
 *
 * @author Jeroen van Schagen
 * @since Apr 13, 2015
 */
@Component
public class OrderBuilder extends AbstractBuilder {

    /**
     * Start building a custom order.
     * 
     * @return the build command
     */
    public OrderBuildCommand newOrder() {
        return new OrderBuildCommand();
    }

    /**
     * Allows building custom order.
     *
     * @author Jeroen van Schagen
     * @since Apr 14, 2015
     */
    public class OrderBuildCommand {

        private final Order order = new Order();

        /**
         * Set a customer.
         * @param customer the new customer
         * @return this builder, for chaining
         */
        public OrderBuildCommand withCustomer(Customer customer) {
            order.setCustomer(customer);
            return this;
        }

        /**
         * adds an orderline.
         */
        public OrderBuildCommand withLine(int quantity, Wine wine) {
            OrderLine orderLine = new OrderLine();
            orderLine.setQuantity(quantity);
            orderLine.setWine(wine);
            orderLine.setCost(wine.getCost());
            order.addLine(orderLine);
            return this;
        }

        public OrderBuildCommand withDate(LocalDateTime date) {
            order.setDate(date);
            return this;
        }

        /**
         * Build the order.
         * @return the created order
         */
        public Order build() {
            return order;
        }

        /**
         * Persists the order.
         * @return the persisted order
         */
        public Order save() {
            return saveWithTransaction(build());
        }

    }

}
