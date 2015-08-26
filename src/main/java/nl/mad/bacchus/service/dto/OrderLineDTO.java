/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service.dto;

import javax.validation.constraints.Min;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import nl.mad.bacchus.model.OrderLine;
import nl.mad.bacchus.model.Wine;

/**
 * Creates a product specific order.
 *
 * @author Jeroen van Schagen
 * @since Jul 7, 2015
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderLineDTO {

    private long id;
    private BigDecimal cost;
    private String productName;
    @Min(0)
    private int quantity;
    
    public static OrderLineDTO toDetailResultDTO(OrderLine orderLine) {
        OrderLineDTO dto = new OrderLineDTO();
        dto.cost = orderLine.getCost();
        dto.quantity = orderLine.getQuantity();
        dto.productName = orderLine.getWine().getName();
        return dto;
    }

    public OrderLine createOrderLine(Wine wine) {
        OrderLine line = new OrderLine();
        line.setWine(wine);
        line.setCost(wine.getCost());
        line.setQuantity(quantity);
        return line;
    }

    public long getWineId() {
        return id;
    }
}
