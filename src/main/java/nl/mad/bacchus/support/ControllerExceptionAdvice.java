package nl.mad.bacchus.support;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Advice for handling exceptions in controllers.
 *
 * @author Jeroen van Schagen
 * @since Mar 27, 2015
 */
@ControllerAdvice
public class ControllerExceptionAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionAdvice.class);

    /**
     * Handle {@link MethodArgumentNotValidException}, shows the field errors.
     * 
     * @param ex the exception
     * @return the field errors as JSON
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public List<FieldErrorMessage> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream().map(fe -> new FieldErrorMessage(fe)).collect(Collectors.toList());
    }

    @ExceptionHandler({ TypeMismatchException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionDescription handleMethodArgumentNotValid(TypeMismatchException ex) {
        return new ExceptionDescription(ex);
    }

    @ExceptionHandler({ IllegalStateException.class })
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    @ResponseBody
    public ExceptionDescription handlePayment_Required(Exception ex) {
        return new ExceptionDescription(ex);
    }


    /**
     * Handle {@link AccessDeniedException}, shows nothing.
     */
    @ExceptionHandler({ AccessDeniedException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleAccessDeniedException() {
    }

    /**
     * Handle {@link NotImplementedException}, shows nothing.
     */
    @ExceptionHandler({ NotImplementedException.class })
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void handleNotImplementedException() {
    }
    
    /**
     * Handles all other exceptions. Causing the exception to be logged and returns a description.
     * 
     * @param request the request
     * @param handler the handler method
     * @param ex the exception
     * @return the result JSON
     */
    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ExceptionDescription handleOther(HttpServletRequest request, Object handler, Exception ex) {
        LOG.error("Handling request, for [" + handler + "], resulted in the following exception.", ex);
        return new ExceptionDescription(ex);
    }

    /**
     * Error message on field level.
     *
     * @author Jeroen van Schagen
     * @since Mar 27, 2015
     */
    public static class FieldErrorMessage {

        private final String field;

        private final String code;

        private final String message;

        public FieldErrorMessage(FieldError error) {
            this.field = error.getField();
            this.code = error.getCode();
            this.message = error.getDefaultMessage();
        }

        public String getField() {
            return field;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

    }
    
    /**
     * Description of the exception.
     *
     * @author Jeroen van Schagen
     * @since Mar 27, 2015
     */
    public static class ExceptionDescription {
        
        private final Class<?> type;
        
        private final String message;
        
        private final String rootCauseMessage;

        public ExceptionDescription(Exception ex) {
            this.type = ex.getClass();
            this.message = ex.getMessage();
            
            Throwable rootCause = ExceptionUtils.getRootCause(ex);
            this.rootCauseMessage = rootCause != null ? rootCause.getMessage() : null;
        }

        public Class<?> getType() {
            return type;
        }
        
        public String getMessage() {
            return message;
        }

        public String getRootCauseMessage() {
            return rootCauseMessage;
        }

    }

}
