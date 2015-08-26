/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus;

import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

/**
 * Creates a dispatcher servlet on "/", using our configured Spring mappings.
 * This is the entry point of our application, from the Tomcat servlet container.
 * During startup this initializer is automatically detected by Spring, because
 * it implements the {@code WebApplicationInitializer} interface.
 *
 * @author Jeroen van Schagen
 */
public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static final int KILO_BYTE = 1024;

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { ApplicationConfig.class, SecurityConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebMvcConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/*" };
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] { new MultipartFilter() };
    }

    @Override
    protected void customizeRegistration(Dynamic registration) {
        registration.setInitParameter("dispatchOptionsRequest", "true");
        registration.setMultipartConfig(new MultipartConfigElement(null, 10000 * KILO_BYTE, 10000 * KILO_BYTE, 10000 * KILO_BYTE));
        super.customizeRegistration(registration);
    }

}
