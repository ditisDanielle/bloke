/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus;

import javax.sql.DataSource;

import nl.mad.bacchus.builder.scenario.DemoDataBuilder;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

@Profile("test")
@Configuration
public class TestApplicationConfig {

    @Configuration
    @Profile("hsql-mem")
    public static class InMemoryHsqlConfig {

        @Bean
        public DataSource dataSource() {
            EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
            builder.setType(HSQL).setName("dev");
            return builder.build();
        }

        @Bean
        public String hibernateDialect() {
            return HSQLDialect.class.getName();
        }

        @Bean
        public String hibernate2Ddl() {
            return "create-drop";
        }
    }

    @Profile("postgres")
    @Configuration
    public static class PostgresqlConfig {

        public static final String HOST = "localhost";
        public static final String PORT = "5432";
        public static final String DB_NAME = "bacchusdb";
        public static final String USERNAME = "bacchus";
        public static final String PASSWORD = "B@cch45";

        @Bean(destroyMethod = "close")
        public DataSource dataSource() {
            String url = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setUrl(url);
            dataSource.setUsername(USERNAME);
            dataSource.setPassword(PASSWORD);
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setValidationQuery("SELECT version()");
            return dataSource;
        }

        @Bean
        public String hibernateDialect() {
            return PostgreSQL9Dialect.class.getName();
        }

        @Bean
        public String hibernate2Ddl() {
            return "validate";
        }

    }

    @Configuration
    @Profile("demo-data")
    public static class DemoDataConfig {

        @Bean
        public DemoDataBuilder demoDataBuilder() {
            return new DemoDataBuilder();
        }
    }

}
