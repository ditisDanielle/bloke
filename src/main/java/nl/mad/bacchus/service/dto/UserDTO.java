package nl.mad.bacchus.service.dto;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import nl.mad.bacchus.model.User;

import org.hibernate.validator.constraints.Email;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class UserDTO {

    protected Long id;
    @Email
    protected String email;
    protected String fullName;
    protected String password;
    protected String role;
    protected Boolean active;

    protected void processPassword(User user) {
        if (isNotBlank(password)) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(password);
            user.setPassword(hashedPassword);
        }
    }

}
