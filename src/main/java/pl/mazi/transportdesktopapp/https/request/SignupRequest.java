package pl.mazi.transportdesktopapp.https.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private Set<String> role;

    private String peselNumber;
    private String placeOfResidence;
    private String phoneNumber;

    private String firstName;
    private String lastName;
    private LocalDate hireDate;
    private LocalDate birthDate;
    private String employeType;
    private Double salary;

    @Override
    public String toString() {
        return "username='" + username + ',' +
                "email='" + email + ',' +
                "password='" + password + ',' +
                "role=" + role +
                "peselNumber='" + peselNumber + ',' +
                "placeOfResidence='" + placeOfResidence + ',' +
                "phoneNumber='" + phoneNumber + ',' +
                "firstName='" + firstName + ',' +
                "lastName='" + lastName + ',' +
                "hireDate=" + hireDate + ',' +
                "birthDate=" + birthDate + ',' +
                "employeType='" + employeType + ',' +
                "salary=" + salary;
    }
}
