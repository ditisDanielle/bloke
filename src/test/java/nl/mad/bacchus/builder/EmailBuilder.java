package nl.mad.bacchus.builder;

import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Email;
import org.springframework.stereotype.Component;

@Component
public class EmailBuilder extends AbstractBuilder {

    public EmailBuildCommand newEmail(Customer cust) {
        return new EmailBuildCommand(cust);
    }

    public class EmailBuildCommand {
        
        private final Email email;

        public EmailBuildCommand(Customer cust) {
            this.email = new Email(cust);
        }

        public EmailBuildCommand withEmailFrom(String from) {
        	email.setEmailFrom(from);
        	return this;
        }
         
        public EmailBuildCommand withMessage(String title, String msg) {
        	email.setTitle(title);
        	email.setMessage(msg);
        	return this;
        }
        
        /**
         * Build the email.
         * @return the created email
         */
        public Email build() {
            return email;
        }

        /**
         * Persists the email.
         * @return the persisted email
         */
        public Email save() {
            return saveWithTransaction(build());
        }

        /**
         * Persists and sends the email.
         * @return the persisted email
         */
        public Email saveAndSend() {
            Email build = build();
            build.markAsSent();
			return saveWithTransaction(build);
        }

    }


}
