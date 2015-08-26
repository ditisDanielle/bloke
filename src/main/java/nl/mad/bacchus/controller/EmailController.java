package nl.mad.bacchus.controller;

import static java.util.stream.Collectors.toList;
import static nl.mad.bacchus.service.dto.EmailDTO.toDetailResultDTO;
import static nl.mad.bacchus.service.dto.EmailDTO.toListResultDTO;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.mad.bacchus.service.EmailService;
import nl.mad.bacchus.service.dto.EmailDTO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/email")
public class EmailController {

	private EmailService emailService;
    
    private ObjectMapper objectMapper;

	@Autowired
    public EmailController(EmailService emailService, ObjectMapper objectMapper) {
		this.emailService = emailService;
        this.objectMapper = objectMapper;
	}
	
    @RequestMapping(value = "/**/*")
    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Principal principal = request.getUserPrincipal();
        if (request.getMethod().equals(HttpMethod.POST.name())) {
            // Parse the email values from our request body
            EmailDTO emailDTO = objectMapper.readValue(request.getReader(), EmailDTO.class);
            sendEmailToAll(emailDTO, principal);
        } else {
            String pathInfo = request.getPathInfo();
            Object result = null;
            if ("/email/customer/current".equals(pathInfo)) {
                result = getByCustomer(principal);
            } else {
                Long id = Long.parseLong(StringUtils.substringAfterLast(pathInfo, "/"));
                result = getById(id, principal);
            }
            // Print the service result to our response body
            objectMapper.writeValue(response.getWriter(), result);
        }
        // Mark the response as successful
        response.setStatus(HttpStatus.OK.value());
    }
    
    private EmailDTO getById(Long id, Principal principal) {
        return toDetailResultDTO(emailService.findById(id, principal.getName()));
    }

    private List<EmailDTO> getByCustomer(Principal principal) {
        return emailService.findEmailByCustomer(principal.getName())
                .stream()
                .map(email -> toListResultDTO(email))
                .collect(toList());
	}

    private void sendEmailToAll(EmailDTO emailDTO, Principal principal) {
        emailService.sendEmailToAllCustomers(emailDTO, principal.getName());
	}

}
