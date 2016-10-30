package nl.mad.bacchus.controller;

import static java.util.stream.Collectors.toList;
import static nl.mad.bacchus.service.dto.EmailDTO.toDetailResultDTO;
import static nl.mad.bacchus.service.dto.EmailDTO.toListResultDTO;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import nl.mad.bacchus.service.EmailService;
import nl.mad.bacchus.service.dto.EmailDTO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/email")
public class EmailController {

    private EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public EmailDTO getById(@PathVariable Long id, Principal principal) {
        return toDetailResultDTO(emailService.findById(id, principal.getName()));
    }

    @ResponseBody
    @RequestMapping(value = "/customer/current", method = RequestMethod.GET)
    public List<EmailDTO> getByCustomer(Principal principal)
    {
        return emailService.findEmailByCustomer(principal.getName())
                .stream()
                .map(email -> toListResultDTO(email))
                .collect(toList());
    }

    @RequestMapping(value = "/customer/all", method = RequestMethod.POST)
    public void sendEmailToAll(@Valid @RequestBody EmailDTO emailDTO, Principal principal) {
        emailService.sendEmailToAllCustomers(emailDTO, principal.getName());
    }

}
