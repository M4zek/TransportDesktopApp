package pl.mazi.transportdesktopapp.https.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private Long idUser;
    private String username;
    private String email;
    private String password;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String placeOfResidence;

}
