/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus;

import javax.sql.DataSource;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import nl.mad.bacchus.ApplicationConfig.DefaultConfig;
import nl.mad.bacchus.support.JndiBeanLocator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.Environment;

/**
 * 
 *
 * @author Jeroen van Schagen
 * @since Apr 14, 2015
 */
public class ApplicationConfigTest {
    
    private DefaultConfig defaultConfig;
    
    @Injectable
    private JndiBeanLocator jndiBeanLocator;
    
    @Injectable
    private Environment environment;

    @Before
    public void setUp() {
        defaultConfig = new DefaultConfig(jndiBeanLocator);
        defaultConfig.setEnvironment(environment);
    }
    
    @Test
    public void testDataSource(@Mocked DataSource dataSource) {
        new Expectations() {
            {
                jndiBeanLocator.lookupBean(DefaultConfig.DATA_SOURCE_JNDI_NAME);
                result = dataSource;
            }
        };
        
        Assert.assertEquals(dataSource, defaultConfig.dataSource());
    }
    
    @Test
    public void testHibernateDialect() {
        final String hibernateDialect = "MyDialect";
        
        new Expectations() {
            {
                environment.getProperty(DefaultConfig.HIBERNATE_DIALECT_NAME, String.class);
                result = hibernateDialect;
            }
        };
        
        Assert.assertEquals(hibernateDialect, defaultConfig.hibernateDialect());
    }

}
