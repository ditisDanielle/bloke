/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Employee that provides support.
 *
 * @author Jeroen van Schagen
 * @since Jun 30, 2015
 */
@Entity
@DiscriminatorValue("employee")
public class Employee extends User {

    public static final String ADMIN = "ROLE_admin";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRole() {
        return ADMIN;
    }

}
