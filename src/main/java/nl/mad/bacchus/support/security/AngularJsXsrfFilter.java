package nl.mad.bacchus.support.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * Implements XSRF support for AngularJS based on its default settings.
 * The server should set a cookie holding the token and the AngularJS will take
 * this value and set it in each POST, PUT etc request.
 * 
 * Note that the Spring Security default XSRF implementation does not support AngularJS 
 * as it works using request parameters.
 */
public class AngularJsXsrfFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AngularJsXsrfFilter.class);

    private static final String SESSION_ATTR = "_xsrf_token";

    private static final String HEADER_XSRF_TOKEN = "X-XSRF-TOKEN";

    private static final String COOKIE_XSRF_TOKEN = "XSRF-TOKEN";

    private final List<String> safeMethods = Arrays.asList(new String[] { "HEAD", "GET", "OPTIONS" });

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (req.getSession().getAttribute(SESSION_ATTR) == null) {
            String token = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(COOKIE_XSRF_TOKEN, token);
            cookie.setPath("/");
            resp.addCookie(cookie);
            req.getSession().setAttribute(SESSION_ATTR, token);
            LOGGER.info("assigned {} to session {}", token, req.getSession().getId());
        }
        if (!isSafe(req.getMethod())) {
            String hdr = req.getHeader(HEADER_XSRF_TOKEN);
            if ((hdr == null) || (!req.getSession().getAttribute(SESSION_ATTR).equals(hdr))) {
                LOGGER.warn("session {} failed XSRF token check ({})", req.getSession().getId(), (hdr == null ? "null" : hdr));
                resp.setStatus(HttpStatus.BAD_REQUEST.value());
            } else {
                LOGGER.info("session {} passed XSRF token check", req.getSession().getId());
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isSafe(String method) {
        return safeMethods.contains(method);
    }

    @Override
    public void destroy() {
    }

}