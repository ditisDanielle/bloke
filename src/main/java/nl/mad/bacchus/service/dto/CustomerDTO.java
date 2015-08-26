package nl.mad.bacchus.service.dto;

import java.math.BigDecimal;

import nl.mad.bacchus.model.Address;
import nl.mad.bacchus.model.Customer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDTO extends UserDTO {

    private Address address;
    private Address invoiceAddress;
    private BigDecimal balance;

    public static CustomerDTO toListResultDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.id = customer.getId();
        dto.fullName = customer.getFullName();
        return dto;
    }

    public static CustomerDTO toDetailResultDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.id = customer.getId();
        dto.email = customer.getEmail();
        dto.fullName = customer.getFullName();
        
        dto.address = new Address();
        dto.address.setStreet(customer.getStreet());
        dto.address.setStreetNumber(customer.getStreetNumber());
        dto.address.setPostalCode(customer.getPostalCode());
        dto.address.setCity(customer.getCity());
        
        dto.invoiceAddress = new Address();
        dto.invoiceAddress.setStreet(customer.getInvoiceStreet());
        dto.invoiceAddress.setStreetNumber(customer.getInvoiceStreetNumber());
        dto.invoiceAddress.setPostalCode(customer.getInvoicePostalCode());
        dto.invoiceAddress.setCity(customer.getInvoiceCity());

        dto.balance = customer.getBalance();
        dto.active = customer.isActive();
        dto.role = customer.getRole();
        return dto;
    }

    public Customer createCustomer() {
        return writeFields(new Customer());
    }

    public Customer update(Customer customer) {
        return writeFields(customer);
    }

    private Customer writeFields(Customer customer) {
        customer.setEmail(email);
        customer.setFullName(fullName);
        
        customer.setStreet(address.getStreet());
        customer.setStreetNumber(address.getStreetNumber());
        customer.setPostalCode(address.getPostalCode());
        customer.setCity(address.getCity());
        
        customer.setInvoiceStreet(invoiceAddress.getStreet());
        customer.setInvoiceStreetNumber(invoiceAddress.getStreetNumber());
        customer.setInvoicePostalCode(invoiceAddress.getPostalCode());
        customer.setInvoiceCity(invoiceAddress.getCity());

        processPassword(customer);
        return customer;
    }
}
