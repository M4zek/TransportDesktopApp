package pl.mazi.transportdesktopapp.https.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeResponse {
    private Long idEmployee;
    private String firstName;
    private String lastName;
    private LocalDate hireDate;
    private LocalDate birthDate;
    private String placeOfResidence;
    private String peselNumber;
    private String phoneNumber;
    private Double salary;
    private String employeeType;

    public EmployeeResponse(Long idEmployee, String firstName, String lastName, LocalDate hireDate, LocalDate birthDate, String placeOfResidence, String peselNumber, String phoneNumber, Double salary, String employeeType) {
        this.idEmployee = idEmployee;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hireDate = hireDate;
        this.birthDate = birthDate;
        this.placeOfResidence = placeOfResidence;
        this.peselNumber = peselNumber;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
        this.employeeType = employeeType;
    }

    public EmployeeResponse(){}
}

