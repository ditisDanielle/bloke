/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.support.security;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

import nl.mad.bacchus.model.User;
import nl.mad.bacchus.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Connects our user domain to the spring security interface.
 *
 * @author Jeroen van Schagen
 * @since Jun 30, 2015
 */
public class SpringUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    /**
     * Retrieve the user that is related to our principal.
     * @param principal the principal
     * @return the user, if any
     */
    public User findUserByPrincipal(Principal principal) {
        if (principal == null) {
            return null;
        }
        return userService.findByEmail(principal.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user with email: " + email);
        }
        return new UserDetailsAdapter(user);
    }

    public final static class UserDetailsAdapter implements UserDetails {

        private final User user;

        private UserDetailsAdapter(User user) {
            this.user = user;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getPassword() {
            return user.getPassword();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getUsername() {
            return user.getEmail();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isEnabled() {
            return user.isActive();
        }

    }

}
