/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.repository;

import java.time.LocalDateTime;
import java.util.List;

import nl.mad.bacchus.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing for orders.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByDateLessThanEqual(LocalDateTime date);
}
