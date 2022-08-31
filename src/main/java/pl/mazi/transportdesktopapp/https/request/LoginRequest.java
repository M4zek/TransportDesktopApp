package pl.mazi.transportdesktopapp.https.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;
    private final String role = "ROLE_OPERATOR";

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
