/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus;

import javax.sql.DataSource;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Base class for Spring tests.
 *
 * @author Jeroen van Schagen
 * @since Sep 30, 2014
 */
@ActiveProfiles({ "test", "hsql-mem" })
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class, SecurityConfig.class })
public abstract class AbstractSpringTest {
    
    private JdbcTemplate jdbcTemplate;
    
    @Before
    @After
    public void clearAll() {
        jdbcTemplate.execute("TRUNCATE SCHEMA public AND COMMIT");
    }
    
    @After
    public void clearAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
    
    /**
     * Login as the "test" user with certain roles.
     * @param roles the roles of the user
     */
    protected void loginWithRoles(String... roles) {
        login("test", roles);
    }
    
    /**
     * Login as a specific user with roles.
     * @param userName the user name
     * @param roles the roles of the user
     */
    protected void login(String userName, String... roles) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roles);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, "", authorities);
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
