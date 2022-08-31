package pl.mazi.transportdesktopapp.Rest;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import pl.mazi.transportdesktopapp.Config.RestConfig;
import pl.mazi.transportdesktopapp.https.request.LoginRequest;
import pl.mazi.transportdesktopapp.https.response.LoginResponse;
import pl.mazi.transportdesktopapp.https.response.MessageResponse;
import pl.mazi.transportdesktopapp.user.User;


public class AuthRest {
    private final static String AUTH_URL = "https://localhost:8443/api/auth/login";
    private final static String RESET_LOGIN_DATE_URL = "https://localhost:8443/api/all/user/resetDate";

    public static String trySignin(LoginRequest loginRequest){
        String message = null;
        ResponseEntity<LoginResponse> response = null;
        try {
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).postForEntity(AUTH_URL, loginRequest, LoginResponse.class);
        } catch(Exception exception){
            if(exception instanceof ResourceAccessException){
                message = "Błąd łączenia z serwerem...\n\nSpróbuj ponownie później lub jeżeli błąd dalej będzię sie pojawiał skontaktuj się z administratorem systemu.";
            } else if (exception instanceof HttpClientErrorException){
                message = "Błędna nazwa użytkownika lub hasło!";
            } else {
                message = exception.getMessage();
            }
        }

        if(response == null || !response.getStatusCode().is2xxSuccessful()){
            return message;
        } else {
            User user = User.getInstance();
            user.setEmail(response.getBody().getEmail());
            user.setFirstName(response.getBody().getFirstName());
            user.setLastName(response.getBody().getLastName());
            user.setId(response.getBody().getId());
            user.setRoles(response.getBody().getRoles());
            user.setJwtToken(response.getBody().getJwtToken());
            user.setUsername(response.getBody().getUsername());
            user.setPhoneNumber(response.getBody().getPhoneNumber());
            user.setPlaceOfResidence(response.getBody().getPlaceOfResidence());
            return "true";
        }
    }

    public String resetUserDate(String email){
        String message = null;
        ResponseEntity<MessageResponse> response = null;
        try{
            response = new RestConfig().restTemplate(new RestTemplateBuilder()).postForEntity(RESET_LOGIN_DATE_URL,email,MessageResponse.class);
            message = response.getBody().getMessage();
        } catch (Exception e){
            message = e.getMessage();
        }
        return message;
    }
}