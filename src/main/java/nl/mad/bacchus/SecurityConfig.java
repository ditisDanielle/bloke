/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus;

import nl.mad.bacchus.support.security.AuthenticationFilter;
import nl.mad.bacchus.support.security.HttpOkLogoutSuccessHandler;
import nl.mad.bacchus.support.security.SpringUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.core.context.SecurityContextHolder.MODE_INHERITABLETHREADLOCAL;

/**
 * Default security configuration.
 *
 * @author Jeroen van Schagen
 * @since Jun 30, 2015
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends GlobalMethodSecurityConfiguration {
    
    /**
     * Initializes the SecurityContextHolder to share the Authentication object with child threads.
     * This is needed when making use of @Async methods that are also annotated with @Secured.
     */
    static {
        SecurityContextHolder.setStrategyName(MODE_INHERITABLETHREADLOCAL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
            //.passwordEncoder(passwordEncoder());
    }
    
    /**
     * Create a new spring user details service.
     */
    @Bean
    public SpringUserDetailService userDetailsService() {
        return new SpringUserDetailService();
    }

    /**
     * Publish the authentication manager.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
    
    /**
     * Web security configuration.
     *
     * @author Jeroen van Schagen
     * @since Jun 30, 2015
     */
    @Configuration
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().and()
                .addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                .and()
                    .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/authentication", HttpMethod.DELETE.name()))
                        .logoutSuccessHandler(new HttpOkLogoutSuccessHandler())
                .and().csrf().disable();
        }

        private AuthenticationFilter authenticationFilter() {
            AntPathRequestMatcher matcher = new AntPathRequestMatcher("/authentication", HttpMethod.POST.name());
            return new AuthenticationFilter(matcher, authenticationManager);
        }

    }
    
}
