/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service;

import nl.mad.bacchus.model.User;
import nl.mad.bacchus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing users.
 *
 * @author Jeroen van Schagen
 * @since Mar 11, 2015
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieve the user with a specific email.
     * 
     * @param email the requested email
     * @return the user with that email, if any
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
