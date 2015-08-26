/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.repository;

import nl.mad.bacchus.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing users.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieve the user by email.
     * 
     * @param email the email of the user
     * @return the user
     */
    User findByEmail(String email);

}
