/*
 * (C) 2013 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus;

import java.lang.reflect.Method;
import java.util.Arrays;

import nl.mad.bacchus.support.ControllerExceptionAdvice;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import static org.springframework.util.Assert.isTrue;

/**
 * @author Bas de Vos
 */
public abstract class AbstractControllerTest {

    private WebMvcConfig config = new WebMvcConfig();
    
    protected MockMvc webClient;

    /**
     * Use this method in a subclass in a @Before annotated method to initialize the MockMvc webClient for the controller under test.
     * With MockMvc it is possible to issue get- and post requests instead of invoking a controller method directly.
     * 
     * More information on the usage of the MockMvc API:
     * http://static.springsource.org/spring/docs/3.2.x/spring-framework-reference/html/testing.html
     * 
     * @param controller the controller under test.
     */
    protected void initWebClient(Object controller) {
        isTrue(isController(controller), "Given controller must be annotated with @Controller");

        this.webClient = MockMvcBuilders.standaloneSetup(controller)
                .setHandlerExceptionResolvers(buildHandlerExceptionResolver())
                .setMessageConverters(config.mappingJackson2HttpMessageConverter())
                .setConversionService(new FormattingConversionService())
                .build();
    }

    private boolean isController(Object controller) {
        final Class<? extends Object> controllerClass = controller.getClass();
        return controllerClass.getAnnotation(Controller.class) != null || controllerClass.getAnnotation(RestController.class) != null;
    }

    private ExceptionHandlerExceptionResolver buildHandlerExceptionResolver() {
        ExceptionHandlerExceptionResolver handlerExceptionResolver = new ExceptionHandlerExceptionResolver() {
            
            @Override
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(ControllerExceptionAdvice.class).resolveMethod(exception);
                return new ServletInvocableHandlerMethod(new ControllerExceptionAdvice(), method);
            }

        };
        handlerExceptionResolver.setMessageConverters(Arrays.asList(config.mappingJackson2HttpMessageConverter()));
        handlerExceptionResolver.afterPropertiesSet();
        return handlerExceptionResolver;
    }
}
