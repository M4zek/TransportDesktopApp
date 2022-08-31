package pl.mazi.transportdesktopapp.Rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import pl.mazi.transportdesktopapp.Config.RestConfig;
import pl.mazi.transportdesktopapp.https.request.EmployeeToDriverRequest;
import pl.mazi.transportdesktopapp.https.request.VehicleRequest;
import pl.mazi.transportdesktopapp.https.response.MessageResponse;
import pl.mazi.transportdesktopapp.https.response.VehicleResponse;
import pl.mazi.transportdesktopapp.https.response.VehicleTypeResponse;
import pl.mazi.transportdesktopapp.user.User;

import java.util.Arrays;
import java.util.List;

public class VehicleRest {

    private static final User user = User.getInstance();
    public static final String GET_ALL_VEHICLE = "https://localhost:8443/api/admin/vehicle/getAllVehicle";
    public static final String GET_ALL_VEHICLE_TYPE = "https://localhost:8443/api/admin/vehicle/getAllVehicleType";
    public static final String POST_NEW_VEHICLE = "https://localhost:8443/api/admin/vehicle/addVehicle";
    public static final String POST_NEW_VEHICLE_TYPE = "https://localhost:8443/api/admin/vehicle/addVehicleType";
    public static final String DELETE_VEHICLE = "https://localhost:8443/api/admin/vehicle/deleteVehicle";
    public static final String DELETE_VEHICLE_TYPE = "https://localhost:8443/api/admin/vehicle/deleteVehicleType";
    public static final String POST_SET_EMPLOYEE_TO_VEHICLE = "https://localhost:8443/api/admin/vehicle/setEmployeeToVehicle";
    public static final String POST_VEHICLE = "https://localhost:8443/api/admin/vehicle/getVehicle";
    public static final String POST_UPDATE_VEHICLE = "https://localhost:8443/api/admin/vehicle/updateVehicle";

    public List<VehicleResponse> getAllVehicle(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + user.getJwtToken());
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<VehicleResponse[]> response  = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(GET_ALL_VEHICLE, HttpMethod.GET, entity, VehicleResponse[].class);
        return Arrays.asList(response.getBody());
    }

    public List<VehicleTypeResponse> getAllVehicleType(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + user.getJwtToken());
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<VehicleTypeResponse[]> response  = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(GET_ALL_VEHICLE_TYPE, HttpMethod.GET, entity, VehicleTypeResponse[].class);
        return Arrays.asList(response.getBody());
    }

    public VehicleResponse getVehicle(Long idVehicle){
        String json = null;
        ResponseEntity<VehicleResponse> response = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            HttpHeaders headers = new HttpHeaders();
            mapper.registerModule(new JavaTimeModule());
            mapper.findAndRegisterModules();
            json = mapper.writeValueAsString(idVehicle);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer " + user.getJwtToken());
            HttpEntity<String> entity = new HttpEntity<>(json.toString(),headers);
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(POST_VEHICLE,HttpMethod.POST,entity,VehicleResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.getBody();
    }

    public String addVehicle(VehicleRequest vehicleRequest){
        String message = null;
        String json = null;
        ResponseEntity<MessageResponse> response = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.findAndRegisterModules();
            json = mapper.writeValueAsString(vehicleRequest);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + user.getJwtToken());
            HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(POST_NEW_VEHICLE, HttpMethod.POST, entity, MessageResponse.class);
        } catch (Exception e) {
            message = e.getMessage();
        }
        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            return message;
        } else {
            return response.getBody().getMessage();
        }
    }

    public String addVehicleType(String type){
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
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(POST_NEW_VEHICLE_TYPE,HttpMethod.POST,entity,MessageResponse.class);
        } catch (Exception e) {
            message = e.getMessage();
        }
        message = response.getBody().getMessage();
        return message ;
    }

    public String deleteVehicleType(String type){
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
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(DELETE_VEHICLE_TYPE,HttpMethod.DELETE,entity,MessageResponse.class);
        } catch (Exception e) {
            return e.getMessage();
        }
        return response.getBody().getMessage();
    }

    public String deleteVehicle(Long idVehicle){
        String json;
        ResponseEntity<MessageResponse> response = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            json = mapper.writeValueAsString(idVehicle);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer " + user.getJwtToken());
            HttpEntity<String> entity = new HttpEntity<>(json.toString(),headers);
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(DELETE_VEHICLE,HttpMethod.DELETE,entity,MessageResponse.class);
        } catch (Exception e) {
            return e.getMessage();
        }
        return response.getBody().getMessage();
    }

    public String setEmployeeToVehicle(EmployeeToDriverRequest employeeToDriverRequest){
        String json;
        ResponseEntity<MessageResponse> response = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            json = mapper.writeValueAsString(employeeToDriverRequest);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer " + user.getJwtToken());
            HttpEntity<String> entity = new HttpEntity<>(json.toString(),headers);
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(POST_SET_EMPLOYEE_TO_VEHICLE,HttpMethod.POST,entity,MessageResponse.class);
        } catch (Exception e) {
            return e.getMessage();
        }
        return response.getBody().getMessage();
    }

    public String updateVehicle(VehicleResponse vehicleResponse){
        String message = null;
        String json = null;
        ResponseEntity<MessageResponse> response = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.findAndRegisterModules();
            json = mapper.writeValueAsString(vehicleResponse);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + user.getJwtToken());
            HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(POST_UPDATE_VEHICLE, HttpMethod.POST, entity, MessageResponse.class);
        } catch (Exception e) {
            message = e.getMessage();
        }
        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            return message;
        } else {
            return response.getBody().getMessage();
        }
    }
}