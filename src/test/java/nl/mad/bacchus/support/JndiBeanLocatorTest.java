/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.support;

import javax.naming.NamingException;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import nl.mad.bacchus.support.JndiBeanLocator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jndi.JndiObjectFactoryBean;

public class JndiBeanLocatorTest {
    
    private static final String JNDI_NAME = "java:comp/env/myvariable";
    
    private JndiBeanLocator jndiBeanLocator;
    
    @Before
    public void setUp() {
        jndiBeanLocator = new JndiBeanLocator();
    }
    
    @Test
    public void testGetBean(@Mocked final JndiObjectFactoryBean jndiObjectFactoryBean) throws NamingException {
        final String value = "Success";
        
        new NonStrictExpectations() {
            {
                jndiObjectFactoryBean.getObject();
                result = value;
            }
        };
        
        Assert.assertEquals(value, jndiBeanLocator.lookupBean(JNDI_NAME, jndiObjectFactoryBean));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testUnknownBean(@Mocked final JndiObjectFactoryBean jndiObjectFactoryBean) throws NamingException {
        new NonStrictExpectations() {
            {
                jndiObjectFactoryBean.afterPropertiesSet();
                result = new NamingException();
            }
        };

        jndiBeanLocator.lookupBean(JNDI_NAME);
    }

}
