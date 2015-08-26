/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.support.security;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

/**
 * Filter that performs authentication. 
 *
 * @author Jeroen van Schagen
 * @since Jun 30, 2015
 */
public class AuthenticationFilter extends GenericFilterBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
    
    private final AntPathRequestMatcher matcher;
    
    private final AuthenticationManager authenticationManager;
    
    private final ObjectMapper objectMapper;
    
    public AuthenticationFilter(AntPathRequestMatcher matcher, AuthenticationManager authenticationManager) {
        this(matcher, authenticationManager, new ObjectMapper());
    }

    public AuthenticationFilter(AntPathRequestMatcher matcher, AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        this.matcher = matcher;
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && matcher.matches((HttpServletRequest) request)) {
            AuthenticateCommand command = objectMapper.readValue(request.getInputStream(), AuthenticateCommand.class);
            
            final String username = command.getUsername();
            final String password = defaultIfBlank(command.getPassword(), "");

            try {
                Preconditions.checkState(isNotBlank(username), "Username is required");
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                ((HttpServletRequest) request).getSession(true).setMaxInactiveInterval(21600);
            } catch (AuthenticationException ae) {
                ((HttpServletResponse) response).setStatus(HttpStatus.FORBIDDEN.value());
                LOGGER.debug("Could not authenticate.", ae);
            }
        }
        chain.doFilter(request, response);
    }
    
    /**
     * Command that performs authentication.
     *
     * @author Jeroen van Schagen
     * @since Jun 30, 2015
     */
    public static class AuthenticateCommand {
        
        private String username;
        
        private String password;
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
        
    }

}
