package nl.mad.bacchus.service.dto;

import nl.mad.bacchus.model.Employee;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDTO extends UserDTO {

    public static EmployeeDTO toListResultDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.id = employee.getId();
        dto.fullName = employee.getFullName();
        return dto;
    }

    public static EmployeeDTO toDetailResultDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.id = employee.getId();
        dto.email = employee.getEmail();
        dto.fullName = employee.getFullName();
        dto.active = employee.isActive();
        dto.role = employee.getRole();
        return dto;
    }

    public Employee createEmployee() {
        return writeFields(new Employee());
    }

    public Employee update(Employee employee) {
        return writeFields(employee);
    }

    private Employee writeFields(Employee employee) {
        employee.setEmail(email);
        employee.setFullName(fullName);
        processPassword(employee);
        return employee;
    }
}
