/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.builder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nl.mad.bacchus.model.BaseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Template for data builders.
 *
 * @author Jeroen van Schagen
 * @since Jun 30, 2015
 */
public abstract class AbstractBuilder {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private TransactionTemplate transactionTemplate;

    /**
     * Save the entity in a new transaction.
     * @param entity the entity to save
     * @return the saved entity
     */
    protected <T extends BaseEntity> T saveWithTransaction(final T entity) {
        return transactionTemplate.execute(new TransactionCallback<T>() {

            @Override
            public T doInTransaction(TransactionStatus status) {
                entityManager.persist(entity);
                return entity;
            }
            
        });
    }

    @Autowired
    public void setPlatformTransactionManager(PlatformTransactionManager transactionManager) {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }

}
