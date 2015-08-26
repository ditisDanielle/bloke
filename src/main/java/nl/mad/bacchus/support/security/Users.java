/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.support.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utilities for managing users.
 *
 * @author Jeroen van Schagen
 * @since Jul 8, 2015
 */
public class Users {
    
    /**
     * Check if the currently logged in user has a role.
     * @param role the role
     * @return {@code true} if role was found, else {@code false}
     */
    public static boolean hasRole(String role) {
        boolean allowed = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            allowed = AuthorityUtils.authorityListToSet(authentication.getAuthorities()).contains(role);
        }
        return allowed;
    }

    /**
     * @return the name of the currently loggedin user.
     */
	public static String getName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication!=null ? authentication.getName() : null;
	}
    
}
