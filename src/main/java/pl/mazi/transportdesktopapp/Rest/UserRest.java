package pl.mazi.transportdesktopapp.Rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import pl.mazi.transportdesktopapp.Config.RestConfig;
import pl.mazi.transportdesktopapp.https.request.UserUpdateRequest;
import pl.mazi.transportdesktopapp.https.response.MessageResponse;
import pl.mazi.transportdesktopapp.user.User;
import pl.mazi.transportdesktopapp.https.request.UserUpdateRequest;
import pl.mazi.transportdesktopapp.https.response.MessageResponse;
import pl.mazi.transportdesktopapp.user.User;

public class UserRest {

    private User user = User.getInstance();

    private static final String UPDATE_USER = "https://localhost:8443/api/admin/user/update";

    public String tryUserUpdate(UserUpdateRequest userUpdateRequest) {
        String message = null;
        String json = null;
        ResponseEntity<MessageResponse> response = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.registerModule(new JavaTimeModule());
            json = mapper.writeValueAsString(userUpdateRequest);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + user.getJwtToken());
            HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).exchange(UPDATE_USER, HttpMethod.POST, entity, MessageResponse.class);
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
