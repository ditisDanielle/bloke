/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.repository;

import nl.mad.bacchus.model.MoneyTransaction;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing money transactions.
 */
public interface MoneyTransactionRepository extends JpaRepository<MoneyTransaction, Long> {

}
