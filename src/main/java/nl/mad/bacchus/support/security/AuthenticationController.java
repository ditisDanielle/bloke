/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.support.security;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.model.User;
import nl.mad.bacchus.service.dto.CustomerDTO;
import nl.mad.bacchus.service.dto.EmployeeDTO;
import nl.mad.bacchus.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Authentication controller.
 *
 * @author Jeroen van Schagen
 * @since Jun 30, 2015
 */
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final SpringUserDetailService userDetailsService;

    @Autowired
    public AuthenticationController(SpringUserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(value = "/team", method = RequestMethod.GET)
    public Map<String, String> getTeamName() {
        return Collections.singletonMap("teamName", "Team05 rules!");
    }

    @RequestMapping(value = "/logintime", method = RequestMethod.GET)
    public Map<String, LocalDateTime> getLoggedInSince(HttpServletRequest request) {
        return Collections.singletonMap("loginTime", (LocalDateTime) request.getSession().getAttribute("loginname"));
    }

    @RequestMapping(method = RequestMethod.GET)
    public UserDTO authenticate(Principal principal) {
        return findUserByPrincipal(principal);
    }

    @RequestMapping(method = RequestMethod.POST)
    public UserDTO login(HttpServletRequest request, Principal principal) {
        request.getSession().setAttribute("loginname", LocalDateTime.now());
        return findUserByPrincipal(principal);
    }

    private UserDTO findUserByPrincipal(Principal principal) {
        User user = userDetailsService.findUserByPrincipal(principal);
        if (user instanceof Customer) {
            return CustomerDTO.toDetailResultDTO((Customer) user);
        } else if (user instanceof Employee) {
            return EmployeeDTO.toDetailResultDTO((Employee) user);
        } else {
            return null;
        }
    }
}
