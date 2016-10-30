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
        dto.address = customer.getAddress();
        dto.invoiceAddress = customer.getInvoiceAddress();
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

        Address newAddress = new Address();
        newAddress.setStreet(address.getStreet());
        newAddress.setStreetNumber(address.getStreetNumber());
        newAddress.setPostalCode(address.getPostalCode());
        newAddress.setCity(address.getCity());
        customer.setAddress(address);

        Address newInvoiceAddress = new Address();
        newInvoiceAddress.setStreet(invoiceAddress.getStreet());
        newInvoiceAddress.setStreetNumber(invoiceAddress.getStreetNumber());
        newInvoiceAddress.setPostalCode(invoiceAddress.getPostalCode());
        newInvoiceAddress.setCity(invoiceAddress.getCity());
        customer.setInvoiceAddress(invoiceAddress);

        processPassword(customer);
        return customer;
    }
}

