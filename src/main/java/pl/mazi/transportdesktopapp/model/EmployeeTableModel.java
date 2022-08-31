package pl.mazi.transportdesktopapp.model;

import pl.mazi.transportdesktopapp.https.response.EmployeeResponse;

import java.time.LocalDate;

public class EmployeeTableModel {

    private Long idEmployee;
    private String firstName;
    private String lastName;
    private Double salary;
    private String placeOfResidence;
    private String peselNumber;
    private String phoneNumber;
    private LocalDate birthDate;
    private LocalDate hireDate;
    private String employeeType;


    public EmployeeTableModel(Long idEmployee, String firstName, String lastName, Double salary, String placeOfResidence, String peselNumber, String phoneNumber, LocalDate birthDate, LocalDate hireDate, String employeeType) {
        this.idEmployee = idEmployee;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.placeOfResidence = placeOfResidence;
        this.peselNumber = peselNumber;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
        this.employeeType = employeeType;
    }

    public static EmployeeTableModel of(EmployeeResponse employeeResponse){
        return new EmployeeTableModel(
                employeeResponse.getIdEmployee(),
                employeeResponse.getFirstName(),
                employeeResponse.getLastName(),
                employeeResponse.getSalary(),
                employeeResponse.getPlaceOfResidence(),
                employeeResponse.getPeselNumber(),
                employeeResponse.getPhoneNumber(),
                employeeResponse.getBirthDate(),
                employeeResponse.getHireDate(),
                employeeResponse.getEmployeeType());
    }

    /*
    Getters and setters \\\///
     */

    public void setPlaceOfResidence(String placeOfResidence) {
        this.placeOfResidence = placeOfResidence;
    }

    public void setPeselNumber(String peselNumber) {
        this.peselNumber = peselNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlaceOfResidence() {
        return placeOfResidence;
    }

    public String getPeselNumber() {
        return peselNumber;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    @Override
    public String toString() {
        return "EmployeeTableModel{" +
                "idEmployee=" + idEmployee +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", placeOfResidence='" + placeOfResidence + '\'' +
                ", peselNumber='" + peselNumber + '\'' +
                ", birthDate=" + birthDate +
                ", hireDate=" + hireDate +
                ", employeeType='" + employeeType + '\'' +
                '}';
    }
}
