/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Order;
import nl.mad.bacchus.model.Order.OrderStatus;
import nl.mad.bacchus.model.OrderLine;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * c
 * DTO that holds the data for an Order.
 *
 * @author Bas de Vos
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDTO {

    private Long id;
    private LocalDateTime date;
    private OrderStatus status;
    private String customerFullname;
    private BigDecimal cost;
    private List<OrderLineDTO> lines = new ArrayList<>();
    
    public static OrderDTO toListResultDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.id = order.getId();
        dto.date = order.getDate();
        dto.status = order.getStatus();
        dto.customerFullname = order.getCustomer().getFullName();
        dto.lines = null;
        return dto;
    }

    public static OrderDTO toDetailResultDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.id = order.getId();
        dto.date = order.getDate();
        dto.status = order.getStatus();
        dto.cost = order.getCost();
        dto.customerFullname = order.getCustomer().getFullName();
        for (OrderLine orderLine : order.getLines()) {
            dto.lines.add(OrderLineDTO.toDetailResultDTO(orderLine));
        }
        return dto;
    }

    public Order createOrderFor(Customer customer) {
        Order order = new Order();
        order.setCustomer(customer);
        return order;
    }

    public List<OrderLineDTO> getLines() {
        return lines;
    }
}
