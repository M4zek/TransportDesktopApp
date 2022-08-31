package pl.mazi.transportdesktopapp.Rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import pl.mazi.transportdesktopapp.Config.RestConfig;
import pl.mazi.transportdesktopapp.https.request.SignupRequest;
import pl.mazi.transportdesktopapp.https.response.DriverResponse;
import pl.mazi.transportdesktopapp.https.response.EmployeeResponse;
import pl.mazi.transportdesktopapp.https.response.EmployeeTypeResponse;
import pl.mazi.transportdesktopapp.https.response.MessageResponse;
import pl.mazi.transportdesktopapp.user.User;

import java.util.Arrays;
import java.util.List;


public class EmployeeRest {

    private static final String GET_ALL_EMPLOYEES_URL = "https://localhost:8443/api/admin/employee/all";
    private static final String DELETE_EMPLOYEE_URL = "https://localhost:8443/api/admin/employee/delete";
    private final static String POST_ADD_SYSTEM_USER_URL = "https://localhost:8443/api/admin/employee/addNewEmployeeOperator";
    private final static String POST_ADD_NEW_EMPLOYEE_URL = "https://localhost:8443/api/admin/employee/addNewEmployee";
    private final static String GET_ALL_EMPLOYEE_TYPE_URL = "https://localhost:8443/api/admin/employee/getAllEmployeeType";
    private final static String POST_EMPLOYEE = "https://localhost:8443/api/admin/employee/getEmployee";
    private static final String POST_UPDATE_EMPLOYEE = "https://localhost:8443/api/admin/employee/updateEmployee";
    private static final String POST_ADD_NEW_EMPLOYEE_TYPE = "https://localhost:8443/api/admin/employee/addEmployeeType";
    private static final String DELETE_EMPLOYEE_TYPE = "https://localhost:8443/api/admin/employee/deleteEmployeeType";
    private static final String GET_ALL_DRIVERS = "https://localhost:8443/api/admin/employee/getAllDrivers";

    private static final User user = User.getInstance();

    /*
            EMPLOYEE TYPE
     */

    public String addEmployeeType(String type){
        String message = null;
        String json = null;
        ResponseEntity<MessageResponse> response = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.findAndRegisterModules();
            json = mapper.writeValueAsString(type);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer " + user.getJwtToken());
            HttpEntity<String> entity = new HttpEntity<>(json.toString(),headers);
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(POST_ADD_NEW_EMPLOYEE_TYPE,HttpMethod.POST,entity,MessageResponse.class);
        } catch (Exception e) {
            message = e.getMessage();
        }
        message = response.getBody().getMessage();
        return message ;
    }

    public List<EmployeeTypeResponse> getAllEmployeType(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer " + user.getJwtToken());
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<EmployeeTypeResponse[]> response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(GET_ALL_EMPLOYEE_TYPE_URL, HttpMethod.GET,entity,EmployeeTypeResponse[].class);
        return  Arrays.asList(response.getBody());
    }

    public String deleteEmployeeType(String type){
        String json;
        ResponseEntity<MessageResponse> response = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.findAndRegisterModules();
            json = mapper.writeValueAsString(type);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer " + user.getJwtToken());
            HttpEntity<String> entity = new HttpEntity<>(json.toString(),headers);
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(DELETE_EMPLOYEE_TYPE,HttpMethod.DELETE,entity,MessageResponse.class);
        } catch (Exception e) {
            return e.getMessage();
        }
        return response.getBody().getMessage();
    }

    /*
            EMPLOYEE
     */
    public List<EmployeeResponse> getEmployees(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer " + user.getJwtToken());
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<EmployeeResponse[]> response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(GET_ALL_EMPLOYEES_URL, HttpMethod.GET,entity,EmployeeResponse[].class);
        return Arrays.asList(response.getBody());
    }

    public List<DriverResponse> getDrivers(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer " + user.getJwtToken());
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<DriverResponse[]> response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(GET_ALL_DRIVERS, HttpMethod.GET,entity,DriverResponse[].class);
        return Arrays.asList(response.getBody());
    }

    public EmployeeResponse getEmployee(Long idEmployee){
        String json = null;
        ResponseEntity<EmployeeResponse> response = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.findAndRegisterModules();
            json = mapper.writeValueAsString(idEmployee.toString());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer " + user.getJwtToken());
            HttpEntity<String> entity = new HttpEntity<>(json.toString(),headers);
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(POST_EMPLOYEE,HttpMethod.POST,entity,EmployeeResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.getBody();
    }

    public String tryAddNewEmployeeSystemUser(SignupRequest signupRequest){
        String message = null;
        String json = null;
        ResponseEntity<MessageResponse> response = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.findAndRegisterModules();
            json = mapper.writeValueAsString(signupRequest);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer " + user.getJwtToken());
            HttpEntity<String> entity = new HttpEntity<>(json.toString(),headers);
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(POST_ADD_SYSTEM_USER_URL,HttpMethod.POST,entity,MessageResponse.class);
        } catch (Exception e) {
            message = e.getMessage();
        }
        if(response == null || !response.getStatusCode().is2xxSuccessful()){
            return message;
        } else {
            return response.getBody().getMessage();
        }
    }

    public String tryAddNewEmployee(SignupRequest signupRequest){
        String message = null;
        String json = null;
        ResponseEntity<MessageResponse> response = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.findAndRegisterModules();
            json = mapper.writeValueAsString(signupRequest);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer " + user.getJwtToken());
            HttpEntity<String> entity = new HttpEntity<>(json.toString(),headers);
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(POST_ADD_NEW_EMPLOYEE_URL,HttpMethod.POST,entity,MessageResponse.class);
        } catch (Exception e) {
            message = e.getMessage();
        }
        if(response == null || !response.getStatusCode().is2xxSuccessful()){
            return message;
        } else {
            return response.getBody().getMessage();
        }
    }

    public static String deleteEmployee(Long id){
        String json;
        ResponseEntity<MessageResponse> response = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.registerModule(new JavaTimeModule());
            json = mapper.writeValueAsString(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer " + user.getJwtToken());
            HttpEntity<String> entity = new HttpEntity<>(json.toString(),headers);
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(DELETE_EMPLOYEE_URL,HttpMethod.DELETE,entity,MessageResponse.class);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
        if(response == null || !response.getStatusCode().is2xxSuccessful()){
            return "Error";
        } else {
            return response.getBody().getMessage();
        }
    }

    public String updateEmployee(EmployeeResponse employeeResponse){
        String message = null;
        String json = null;
        ResponseEntity<MessageResponse> response = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.findAndRegisterModules();
            json = mapper.writeValueAsString(employeeResponse);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer " + user.getJwtToken());
            HttpEntity<String> entity = new HttpEntity<>(json.toString(),headers);
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(POST_UPDATE_EMPLOYEE,HttpMethod.POST,entity,MessageResponse.class);
        } catch (Exception e) {
            message = e.getMessage();
        }
        if(response == null || !response.getStatusCode().is2xxSuccessful()){
            return message;
        } else {
            return response.getBody().getMessage();
        }
    }

}