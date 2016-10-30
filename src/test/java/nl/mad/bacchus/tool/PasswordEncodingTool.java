/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.tool;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.postgresql.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Encodes the passwords of all users.
 *
 * @author Jeroen van Schagen
 * @since Aug 13, 2015
 */
public class PasswordEncodingTool {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordEncodingTool.class);

    public static void main(String[] args) {
        PasswordEncodingTool tool = new PasswordEncodingTool();
        tool.encodeAllUsers(dataSource());
    }

    private static DataSource dataSource() {
        String url = "jdbc:postgresql://localhost:5432/bacchus";
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setValidationQuery("SELECT version()");
        return dataSource;
    }
    
    private void encodeAllUsers(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String, Object>> users = jdbcTemplate.queryForList("SELECT * FROM \"user\"");
        for (Map<String, Object> user : users) {
            Long id = (Long) user.get("id");
            String password = (String) user.get("password");
            if (!password.startsWith("$")) {
                String encodedPassword = encode(password);
                LOGGER.info("Encoding password for {} from '{}' to '{}'", id, password, encodedPassword);
                jdbcTemplate.execute("UPDATE \"user\" SET password = '" + encodedPassword + "' where id = " + id);
            } else {
                LOGGER.warn("User {} is already encoded!", id);
            }
        }
    }
    
    private String encode(String password) {
        return password; // TODO: Implement this
    }

}
