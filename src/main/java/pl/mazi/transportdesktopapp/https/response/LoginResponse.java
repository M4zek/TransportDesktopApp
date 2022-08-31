package pl.mazi.transportdesktopapp.https.response;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    private Long id;
    private String jwtToken;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String placeOfResidence;
    private List<String> roles;
}
