package nl.mad.bacchus.support;

import javax.naming.NamingException;

import org.springframework.jndi.JndiObjectFactoryBean;

/**
 * Locates beans from the JNDI context.
 *
 * @author Jeroen van Schagen
 * @since Apr 14, 2015
 */
public class JndiBeanLocator {

    /**
     * Retrieves the bean.
     * 
     * @param jndiName the JNDI name
     * @return the retrieved bean
     */
    public Object lookupBean(String jndiName) {
        return lookupBean(jndiName, new JndiObjectFactoryBean());
    }

    /**
     * Retrieves the bean using a specific factory bean.
     * 
     * @param jndiName the JNDI name
     * @param jndiObjectFactoryBean the factory bean
     * @return the retrieved bean
     */
    public Object lookupBean(String jndiName, JndiObjectFactoryBean jndiObjectFactoryBean) {
        try {
            jndiObjectFactoryBean.setJndiName(jndiName);
            jndiObjectFactoryBean.afterPropertiesSet();
            return jndiObjectFactoryBean.getObject();
        } catch (NamingException ne) {
            throw new IllegalStateException(ne);
        }
    }

}
