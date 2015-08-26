/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service;

import nl.mad.bacchus.model.BaseEntity;

import org.springframework.data.repository.CrudRepository;

/**
 * 
 *
 * @author Jeroen van Schagen
 * @since Jul 7, 2015
 */
public class ReadService<T extends BaseEntity> {
    
    private final CrudRepository<T, Long> repository;
    
    /**
     * Create a new read service.
     * 
     * @param repository the repository
     */
    public ReadService(CrudRepository<T, Long> repository) {
        this.repository = repository;
    }

    /**
     * Retrieve an entity by identifier.
     * 
     * @param id the identifier
     * @return the entity
     */
    public T findById(Long id) {
        return repository.findOne(id);
    }

    /**
     * Returns a list of all entities
     * @return iterable containing all entities
     */
    public Iterable<T> findAll() {
        return repository.findAll();
    }

}
