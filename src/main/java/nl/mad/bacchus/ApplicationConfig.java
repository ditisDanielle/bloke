/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus;

import javax.sql.DataSource;
import javax.validation.Validator;

import java.util.HashMap;
import java.util.Map;

import nl.mad.bacchus.support.ConventionNamingStrategy;
import nl.mad.bacchus.support.JndiBeanLocator;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

/**
 * Default Spring configuration.
 *
 * @author Jeroen van Schagen
 * @since Apr 10, 2015
 */
@Configuration
@ComponentScan(basePackageClasses = ApplicationConfig.class,
        excludeFilters = {
                @Filter({ ControllerAdvice.class, Controller.class, RestController.class }),
                @Filter(value = WebMvcConfig.class, type = FilterType.ASSIGNABLE_TYPE),
                @Filter(value = SecurityConfig.class, type = FilterType.ASSIGNABLE_TYPE)
        })
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = ApplicationConfig.class)
@PropertySources(@PropertySource(value = "classpath:application.properties"))
@EnableAsync
public class ApplicationConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("hibernateDialect")
    private String hibernateDialect;

    @Autowired
    @Qualifier("hibernate2Ddl")
    private String hibernate2Ddl;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan(ApplicationConfig.class.getPackage().getName());
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(false);
        jpaVendorAdapter.setDatabasePlatform(hibernateDialect);
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.ejb.naming_strategy", ConventionNamingStrategy.class.getName());
        jpaProperties.put("hibernate.dialect", hibernateDialect);
        jpaProperties.put("hibernate.hbm2ddl.auto", hibernate2Ddl);
        jpaProperties.put("hibernate.jdbc.use_get_generated_keys", true);
        jpaProperties.put("hibernate.id.new_generator_mappings", true);
        jpaProperties.put("hibernate.generate_statistics", false);
        jpaProperties.put("javax.persistence.validation.factory", validator());

        entityManagerFactoryBean.setJpaPropertyMap(jpaProperties);
        return entityManagerFactoryBean;
    }

    /**
     * Create a new transaction manager.
     *
     * @return JpaTransactionManager
     */
    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    /**
     * Creates a new {@link Validator}.
     *
     * @return the created validator
     */
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Profile("default")
    @Configuration
    public static class DefaultConfig implements EnvironmentAware {

        public static final String DATA_SOURCE_JNDI_NAME = "java:comp/env/bacchus/jdbc";

        public static final String HIBERNATE_DIALECT_NAME = "hibernate.dialect";

        public static final String HIBERNATE_DDL_VALIDATE = "validate";

        private final JndiBeanLocator jndiBeanLocator;

        private Environment environment;

        public DefaultConfig() {
            this(new JndiBeanLocator());
        }

        public DefaultConfig(JndiBeanLocator jndiBeanLocator) {
            this.jndiBeanLocator = jndiBeanLocator;
        }

        /**
         * Create a new DataSource from the JNDI name.
         *
         * @return the data source
         */
        @Bean
        public DataSource dataSource() {
            return (DataSource) jndiBeanLocator.lookupBean(DATA_SOURCE_JNDI_NAME);
        }

        /**
         * Return hibernate dialect retrieved the application properties.
         *
         * @return the hibernate dialect
         */
        @Bean
        public String hibernateDialect() {
            return environment.getProperty(HIBERNATE_DIALECT_NAME, String.class);
        }

        /**
         * Return hibernate DDL as "validate" causing hibernate to
         * make no modifications to the database scheme.
         *
         * @return the hibernate DDL
         */
        @Bean
        public String hibernate2Ddl() {
            return HIBERNATE_DDL_VALIDATE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }

    }

}
